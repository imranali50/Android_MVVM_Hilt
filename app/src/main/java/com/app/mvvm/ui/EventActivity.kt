package com.app.mvvm.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvm.MyApplication
import com.app.mvvm.R
import com.app.mvvm.adapter.CountryCodeAdapter
import com.app.mvvm.adapter.EventAdapter
import com.app.mvvm.databinding.ActivityCountryAcitivityBinding
import com.app.mvvm.databinding.ActivityEventBinding
import com.app.mvvm.model.request.EventRequest
import com.app.mvvm.model.response.CountryCodes
import com.app.mvvm.model.response.EventResponse
import com.app.mvvm.model.viewModel.MainViewModel
import com.app.mvvm.util.NetworkResult
import com.app.mvvm.util.Utils
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventActivity : AppCompatActivity() {
    val b: ActivityEventBinding by lazy { ActivityEventBinding.inflate(layoutInflater) }
    var search = ""
    private val mainViewModel by viewModels<MainViewModel>()

    lateinit var eventAdapter: EventAdapter
    lateinit var utils: Utils
    var pageNum = 1
    var totalPage = 0
    var isClearList = true
    val list: ArrayList<EventResponse.Data> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(b.root)
        utils = Utils(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        extra()
    }

    private fun extra() {
        eventApi()
        b.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (b.etSearch.text.toString().trim().isNotEmpty()) {
                        pageNum = 1
                        isClearList = true
                        search = b.etSearch.text.toString().trim()
                        eventApi()
                        utils.hideKeyBoardFromView()
                    }
                    return true
                }
                return false
            }
        })

        b.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s.toString().trim())) {
//                    pageNum = 1
//                    isClearList = true
                    search = ""
                    eventApi()
                }
            }
        })

        b.rvCountryCode.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((b.rvCountryCode.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == list.size - 1 && pageNum != totalPage.toInt()) {
                    pageNum++
                    isClearList = false
                    eventApi()
                }
            }
        })
//        b.nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
//            val view = b.nestedScrollView.getChildAt(b.nestedScrollView.childCount - 1) as View
//            val diff: Int = view.bottom - (b.nestedScrollView.height + b.nestedScrollView.scrollY)
//            if (diff == 0 && pageNum != totalPage) {
//                pageNum++
//                isClearList = false
//                eventApi()
//            }
//        }
        eventAdapter = EventAdapter(this, list)
        b.rvCountryCode.adapter = eventAdapter

        observer()
    }

    private fun eventApi() {
        Log.e("TAG", "eventApi: ")
        val request = EventRequest(
            EventRequest.Data(
                "1",
                "12",
                "6",
                "1",
                "5",
                pageNum.toString(),
                search,
                "iXKAc2h51KkIizxN7RNn8GLEiE6HIo3BbifxbQ7h3FYlwDonNs29wLs7mVPTPMgnvg9vjcqd77XN2RzTJaFKgpDwshfHuE2iJvIMVm2LuqB7H73eXWY5u5Gt"
            )
        )
        mainViewModel.eventList(request)
    }

    private fun observer() {
        mainViewModel.eventResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    MyApplication.progressBar.dismiss()
                    if (isClearList) {
                        list.clear()
                    }
                    totalPage = response.data?.totalPages?.toInt()!!
                    list.addAll(response.data.data)
                    eventAdapter.notifyDataSetChanged()
                }

                is NetworkResult.Error -> {
                    // Handle the error case
                    MyApplication.progressBar.dismiss()
                }

                is NetworkResult.Loading -> {
                    // Handle the loading case
                    MyApplication.progressBar.show(this)
                }

                else -> {
                    // Handle other cases
                    MyApplication.progressBar.dismiss()
                }
            }
        }

    }


}
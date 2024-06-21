package com.app.mvvm.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.mvvm.R
import com.app.mvvm.adapter.CountryCodeAdapter
import com.app.mvvm.databinding.ActivityCountryAcitivityBinding
import com.app.mvvm.model.response.CountryCodes
import com.app.mvvm.util.Utils
import com.google.gson.Gson

class CountryActivity : AppCompatActivity() {
    val b : ActivityCountryAcitivityBinding by lazy { ActivityCountryAcitivityBinding.inflate(layoutInflater) }
    var search = ""

    var list :ArrayList<CountryCodes.CountryCodesItem> = ArrayList()
    lateinit var countryCodeAdapter: CountryCodeAdapter
    lateinit var utils: Utils

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

        list = Gson().fromJson(
            utils.readJsonAsset("country_codes.json", this),
            CountryCodes::class.java
        )

        countryCodeAdapter = CountryCodeAdapter(this,list)
        b.rvCountryCode.adapter = countryCodeAdapter

        b.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    search = b.etSearch.text.toString().trim { it <= ' ' }
                    filter(search)
                }else{
                    b.rvCountryCode.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun filter(text: String) {
        Log.e("TAG", "filter: "+search )
        // creating a new array list to filter our data.
        val filterList: ArrayList<CountryCodes.CountryCodesItem> = ArrayList()

        // running a for loop to compare elements.
        for (i in list.indices) {
            if (list[i].name.toLowerCase().contains(text.toLowerCase())||list[i].code.toLowerCase().contains(text)||list[i].dial_code.contains(text)) {
                Log.e("TAG", "filter: "+list[i] )
                filterList.add(list[i])
            }
        }
        if (filterList.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            b.rvCountryCode.visibility = View.GONE
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            b.rvCountryCode.visibility = View.VISIBLE
            countryCodeAdapter.filterList(filterList)
        }
    }

}
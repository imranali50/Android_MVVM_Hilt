package com.app.mvvm.model.response

class CountryCodes : ArrayList<CountryCodes.CountryCodesItem>(){
    data class CountryCodesItem(
        val name: String,
        val dial_code: String,
        val code: String
    )
}
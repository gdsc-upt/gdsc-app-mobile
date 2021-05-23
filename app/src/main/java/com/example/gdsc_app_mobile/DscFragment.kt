package com.example.gdsc_app_mobile

import androidx.fragment.app.Fragment



//
open class DscFragment : Fragment {
    private var  fragmentName : String

    constructor(fragmentName : String){
        this.fragmentName = fragmentName
    }
    
    fun saveLastFragment(){
        val editor = activity!!.getSharedPreferences("save", 0).edit()
        editor.putString("lastFragment",fragmentName)
        editor.apply()
    }
}
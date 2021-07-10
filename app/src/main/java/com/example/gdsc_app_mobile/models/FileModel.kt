package com.example.gdsc_app_mobile.models

class FileModel(var id : String, var created : String, var updated : String,
                var name: String, var path : String, var extension : String, var size : Int) {
    override fun toString(): String {
        return "$id,$created,$updated,'$name,$path,$extension,$size"
    }

}
package com.example.gdsc_app_mobile.models

class FileModel(
    var id: String, var name: String, var path: String) {
    override fun toString(): String {
        return "$id,,$name,$path"
    }

}
package com.example.gdsc_app_mobile

class UserResponse (
    var id: Int,
    var url : String,
    var username: String,
    var first_name: String,
    var last_name: String,
    var email: String,
    var is_active: Boolean,
    var date_joined: String){


    override fun toString(): String {
        return "UserResponse{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", is_active=" + is_active +
                ", date_joined='" + date_joined + '\'' +
                '}'
    }
}
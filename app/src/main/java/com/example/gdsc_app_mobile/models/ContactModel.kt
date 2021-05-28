package com.example.gdsc_app_mobile.models

import java.util.*

class ContactModel(var id: String, var created: String, var updated: String, var name: String, var email: String, var subject: String, var text: String) {
    override fun toString(): String {
        return "ContactModel(id='$id', created=$created, updated=$updated, name='$name', email='$email', subject='$subject', text='$text')"
    }
}
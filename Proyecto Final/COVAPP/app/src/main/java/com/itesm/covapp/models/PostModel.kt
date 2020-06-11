package com.itesm.covapp.models

import java.io.Serializable

class PostModel:Serializable {
    var lastName: String? = ""
    var username: String? = ""
    var postContent: String? = ""
    var title: String? = ""
    var state: String? = ""
    var topic: String? = ""
    var date: String? = ""
    var latitude:String? = ""
    var longitude:String? = ""
}
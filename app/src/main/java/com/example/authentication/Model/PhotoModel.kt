package com.example.authentication.Model

import android.net.Uri
import java.io.Serializable

data class PhotoModel(
    var photoUri: String? = null,
    var userName :String?=null,
    var userComment :String?=null,
    var uploadDate: Long = 0
): Serializable

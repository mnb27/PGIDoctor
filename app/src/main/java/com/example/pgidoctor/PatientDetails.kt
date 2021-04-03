package com.example.pgidoctor

import java.io.Serializable

data class PatientDetails(
    val name: String = "",
    val fathername: String = "",
    val date: String = "",
    val crno: String = "",
    val mobile: String = "",
    val age: String = "",
    val gender: String = "",
    var profileImageUrl: String = "",
    var hospitalText: String="",
    var unitText: String = ""
): Serializable

package com.example.pgidoctor

import java.io.Serializable

data class User(val id: String = "",
                val name: String = "",
                val mobile: String ="",
                val email: String = "",
                var type: String = "",
                var hospital: String = "",
                var unit: String = "") :Serializable
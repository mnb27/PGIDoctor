package com.example.pgidoctor

data class PendingList(val name: String = "",
                            val email: String = "",
                            val hospitalName: String = "", val unitName: String = "", val category: String ="", val gender: String = "", val dob: String = "",
                            val mobile: String = "", var status: String ="")
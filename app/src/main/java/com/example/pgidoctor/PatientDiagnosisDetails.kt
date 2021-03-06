package com.example.pgidoctor

import java.io.Serializable

data class PatientDiagnosisDetails(
    val name: String = "",
    var hospitalText: String="",
    var unitText: String = "",
    var date: String = "",
    var weight: String = "",
    var height: String = "",
    var smoking: String = "",
    var alcohole: String = "",
    var comorbidities: String = "",
    var familyho: String = "",
    var bonescan: String = "",
    var mri: String = "",
    var psmapet: String = "",
    var type :String = "",
    var doctorRemarks :String = "",
    var medicines : String = "",
    var id: String = ""
): Serializable
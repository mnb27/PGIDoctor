class DiagnosisForm {
    constructor(id,userid,alcohole,bonescan,comorbidities,date,doctorRemarks,familyho,height,hospitalText,medicines,mri,name,psmapet,smoking,type,unitText,weight ) {
            this.alcohole = alcohole
            this.userid = userid
            this.bonescan = bonescan
            this.name = name
            this.comorbidities = comorbidities
            this.doctorRemarks = doctorRemarks
            this.familyho = familyho
            this.date = date
            this.height = height
            this.medicines = medicines
            this.hospitalText = hospitalText
            this.mri = mri
            this.psmapet = psmapet
            this.smoking = smoking
            this.type = type
            this.weight = weight
            this.unitText = unitText
            this.id = id
            
    }
}

module.exports = DiagnosisForm;
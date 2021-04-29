class PatientDetails {
    constructor(age,crno,date,email,fathername,gender,hospitalText,id,isImportant,isSevere,isStarred,mobile,name,profileImageUrl,unitText ) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.mobile = mobile;
            this.age = age
            this.crno = crno
            this.date = date
            this.fathername = fathername
            this.gender = gender
            this.hospitalText = hospitalText
            this.id = id
            this.isImportant = isImportant
            this.isSevere = isSevere
            this.isStarred = isStarred
            this.isSevere = isSevere
            this.unitText = unitText
            this.profileImageUrl = profileImageUrl
    }
}

module.exports = PatientDetails;
'use strict';

const firebase = require('../db');

const Student = require('../models/student');
const User = require('../models/PatientDetails');
const firestore = firebase.firestore();

const lib = require("../globals/global.js");
const PatientDetails = require('../models/PatientDetails');
const DiagnosisForm = require('../models/DiagnosisForm');


const getAllUsers = async (req, res, next) => {
    try {
        var user = firebase.auth().currentUser;
        var email,hospitalText,unitText;
        if(user!=null){
            email = user.email;
            
            const userDetail = await firestore.collection('Users');
            const data1 = await userDetail.get();
            data1.forEach(doc => {
                if(email == doc.data().email){
                    hospitalText = doc.data().hospital
                    unitText = doc.data().unit
                    
                }

            })
        }

        const users = await firestore.collection('PatientDetails');
        const data = await users.get();
        const userArray = [];
        if(data.empty) {
            res.status(404).send('No student record found');
        }else {
            data.forEach(doc => {
                if(doc.data().hospitalText == hospitalText && doc.data().unitText == unitText){
                    const user = new PatientDetails(
                        doc.data().age,
                        doc.data().crno,
                        doc.data().date,
                        doc.data().email,
                        doc.data().fathername,
                        doc.data().gender,
                        doc.data().hospitalText,
                        doc.data().id,
                        doc.data().isImportant,
                        doc.data().isSevere,
                        doc.data().isStarred,
                        doc.data().mobile,
                        doc.data().name,
                        doc.data().profileImageUrl,
                        doc.data().unitText
                    );
                    userArray.push(user);
                }    
            });
            res.render("../views/users.ejs",{userArray})
        }
    } catch (error) {
        res.status(400).send(error.message);
    }
}


const getBloodReports = async(req,res,next) => {
    try{

        const id = req.params.id;
        const bloodReports = await firestore.collection('PatientDetails').doc(id).collection('DiagnosisForm');
        const data = await bloodReports.get()
        const ReportsArray = [];
        data.forEach(doc => {
            if(doc.data().type == "blood"){
                const form = new DiagnosisForm(
                    doc.id,
                    id,
                    doc.data().alcohole,
                    doc.data().bonescan,
                    doc.data().comorbidities,
                    doc.data().date,
                    doc.data().doctorRemarks,
                    doc.data().familyho,
                    doc.data().height,
                    doc.data().hospitalText,
                    doc.data().medicines,
                    doc.data().mri,
                    doc.data().name,
                    doc.data().psmapet,
                    doc.data().smoking,
                    doc.data().type,
                    doc.data().unitText,
                    doc.data().weight
                );
                ReportsArray.push(form);
            }
        });
        res.render('../views/showBloodReports.ejs',{ReportsArray})
    } catch (error) {
        res.status(400).send(error.message);
    }
}
const getUrineReports = async(req,res,next) => {
    try{

        const id = req.params.id;
        const bloodReports = await firestore.collection('PatientDetails').doc(id).collection('DiagnosisForm');
        const data = await bloodReports.get()
        const ReportsArray = [];
        data.forEach(doc => {
            if(doc.data().type == "urine"){
                const form = new DiagnosisForm(
                    doc.id,
                    doc.data().alcohole,
                    doc.data().bonescan,
                    doc.data().comorbidities,
                    doc.data().date,
                    doc.data().doctorRemarks,
                    doc.data().familyho,
                    doc.data().height,
                    doc.data().hospitalText,
                    doc.data().medicines,
                    doc.data().mri,
                    doc.data().name,
                    doc.data().psmapet,
                    doc.data().smoking,
                    doc.data().type,
                    doc.data().unitText,
                    doc.data().weight
                );
                ReportsArray.push(form);
            }
        });
        res.render('../views/showUrineReports.ejs',{ReportsArray})
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getThyroidReports = async(req,res,next) => {
    try{

        const id = req.params.id;
        const bloodReports = await firestore.collection('PatientDetails').doc(id).collection('DiagnosisForm');
        const data = await bloodReports.get()
        const ReportsArray = [];
        data.forEach(doc => {
            if(doc.data().type == "thyroid"){
                const form = new DiagnosisForm(
                    doc.id,
                    doc.data().alcohole,
                    doc.data().bonescan,
                    doc.data().comorbidities,
                    doc.data().date,
                    doc.data().doctorRemarks,
                    doc.data().familyho,
                    doc.data().height,
                    doc.data().hospitalText,
                    doc.data().medicines,
                    doc.data().mri,
                    doc.data().name,
                    doc.data().psmapet,
                    doc.data().smoking,
                    doc.data().type,
                    doc.data().unitText,
                    doc.data().weight
                );
                ReportsArray.push(form);
            }
        });
        res.render('../views/showThyroidReports.ejs',{ReportsArray})
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getCholestrolReports = async(req,res,next) => {
    try{

        const id = req.params.id;
        const bloodReports = await firestore.collection('PatientDetails').doc(id).collection('DiagnosisForm');
        const data = await bloodReports.get()
        const ReportsArray = [];
        data.forEach(doc => {
            if(doc.data().type == "cholestrol"){
                const form = new DiagnosisForm(
                    doc.id,
                    doc.data().alcohole,
                    doc.data().bonescan,
                    doc.data().comorbidities,
                    doc.data().date,
                    doc.data().doctorRemarks,
                    doc.data().familyho,
                    doc.data().height,
                    doc.data().hospitalText,
                    doc.data().medicines,
                    doc.data().mri,
                    doc.data().name,
                    doc.data().psmapet,
                    doc.data().smoking,
                    doc.data().type,
                    doc.data().unitText,
                    doc.data().weight
                );
                ReportsArray.push(form);
            }
        });
        res.render('../views/showCholestrolReports.ejs',{ReportsArray})
    } catch (error) {
        res.status(400).send(error.message);
    }
}








module.exports = {
    getAllUsers,
    getBloodReports,
    getUrineReports,
    getCholestrolReports,
    getThyroidReports
}
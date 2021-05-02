'use strict';

const firebase = require('../db');

const Student = require('../models/student');
const User = require('../models/PatientDetails');
const firestore = firebase.firestore();


const PatientDetails = require('../models/PatientDetails');
const DiagnosisForm = require('../models/DiagnosisForm');

const getAllUsers = async (req, res, next) => {
    try {
        firebase.auth().currentUser.getIdToken(true)
            .then(function(idToken){
                res.send(idToken)
            })
        
        var user = firebase.auth().currentUser;
        var email,hospitalText,unitText;
        if(user!=null){
            email = user.email;
            res.send(email)
            const userDetail = await firestore.collection('Users');
            const data1 = await userDetail.get();
            data1.forEach(doc => {
                if(email == doc.data().email){
                    hospitalText = doc.data().hospital
                    unitText = doc.data().unit
                    
                }

            })
        }
        else{
            res.send("null")
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
            
        }
    } catch (error) {
        res.status(400).send(error.message);
    }
}


module.exports = {
    getAllUsers,
}
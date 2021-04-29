const express = require('express');

const firebase = require('../db');
const firestore = firebase.firestore();
const firebaseAuth = firebase.auth();

const lib = require("../globals/global.js");
const {
       
       getAllUsers,
       getBloodReports,
       getCholestrolReports,
       getUrineReports,
       getThyroidReports,

      
       
      } = require('../controllers/controller');

const router = express.Router();

router.get('/users/:id/bloodReports',getBloodReports)
router.get('/users/:id/urineReports',getUrineReports)
router.get('/users/:id/thyroidReports',getThyroidReports)
router.get('/users/:id/cholestrolReports',getCholestrolReports)
router.get('/',(req,res) => {
  
  var user = firebaseAuth.currentUser;
  if(user){
    res.render('../views/home.ejs')
  }
  else{
    res.redirect('/login')
  }
  
    
})

router.get('/login',(req,res) => {
  var user = firebaseAuth.currentUser;
  if(user){
    res.render("../views/home.ejs")
  }
  else{
    res.render("../views/login.ejs")
  }
  
  
})

router.post('/getlogin',function(req,res,next){
  

  var username = req.body.exampleInputEmail1;
  var password = req.body.exampleInputPassword1;

  console.log('start');
  console.log(username);
  console.log(password);
  
  
  firebaseAuth.signInWithEmailAndPassword(username,password)
    .then((userCredential) => {
      lib.login = "true"
      res.render("../views/home.ejs");
    })

})


router.get('/logout',(req,res)=>{
  firebaseAuth.signOut()
  res.render("../views/login.ejs")
})






router.get('/users',getAllUsers)



module.exports = {
    routes: router
}
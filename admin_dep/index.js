const cookieParser = require("cookie-parser");
const csrf = require("csurf");
const cors = require("cors");
const bodyParser = require("body-parser");
const express = require("express");
const config = require('./config');
const firebase = require('./db');
const firestore = firebase.firestore();
const firebaseAuth = firebase.auth();
const admin = require("firebase-admin");
const {
       
  getAllUsers,
 
  
 } = require('./controllers/controller');


const serviceAccount = require("./serviceAccountKey.json");
const PatientDetails = require("./models/PatientDetails");
const DiagnosisForm = require("./models/DiagnosisForm");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://server-auth-41acc.firebaseio.com",
});

const csrfMiddleware = csrf({ cookie: true });

const PORT = process.env.PORT || 3000;
const app = express();

app.engine("html", require("ejs").renderFile);
app.use(express.static("static"));
app.use(express.static("public"));

app.use(bodyParser.json());
app.use(cookieParser());
app.use(csrfMiddleware);
app.use(express.json());
app.use(cors());
app.use(express.urlencoded({extended: true}))

app.all("*", (req, res, next) => {
  res.cookie("XSRF-TOKEN", req.csrfToken());
  next();
});

app.get("/login", function (req, res) {
  res.render("login.ejs");
});

app.get("/signup", function (req, res) {
  res.render("signup.html");
});

app.get("/", function (req, res) {
  const sessionCookie = req.cookies.session || "";

  admin
    .auth()
    
    .verifySessionCookie(sessionCookie, true /** checkRevoked */)
    .then(() => {
      res.render("profile.ejs");
    })
    .catch((error) => {
      res.redirect("/login");
    });
});

app.get("/", function (req, res) {
  res.render("index.html");
});

app.post("/sessionLogin", (req, res) => {
  const idToken = req.body.idToken.toString();

  const expiresIn = 60 * 60 * 24 * 5 * 1000;

  admin
    .auth()
    .createSessionCookie(idToken, { expiresIn })
    .then(
      (sessionCookie) => {
        const options = { maxAge: expiresIn, httpOnly: true };
        res.cookie("session", sessionCookie, options);
        res.end(JSON.stringify({ status: "success" }));
      },
      (error) => {
        res.status(401).send("UNAUTHORIZED REQUEST!");
      }
    );
});

app.get("/sessionLogout", (req, res) => {
  res.clearCookie("session");
  res.redirect("/login");
});

app.get("/users",async (req,res) => {
  try {
    const sessionCookie= req.cookies.session;
    const userDetail = await firestore.collection('Users');
        const data1 = await userDetail.get();
        const users = await firestore.collection('PatientDetails');
    const data = await users.get();
    var email = ""
    var hospitalText=""
    var unitText = ""
        //console.log(token);
        // idToken comes from the client app
        admin.auth().verifySessionCookie(
            sessionCookie, true /** checkRevoked */)
            .then((decodedClaims) => {
                //console.log(decodedClaims);

                email = decodedClaims.email
                data1.forEach(doc => {
            if(email == doc.data().email){
                hospitalText = doc.data().hospital
                unitText = doc.data().unit
                
            }
            

        })
        //res.send(hospitalText)
   
    
    const userArray = [];
    if(data.empty) {
        res.status(404).send('No student record found');
    }else {
        data.forEach(doc => {
            var datee = doc.data().date
            var bookdate = datee.substring(6,8) + "-" + datee.substring(4,6) + "-" + datee.substring(0,4)
            if(doc.data().hospitalText == hospitalText && doc.data().unitText == unitText){
                const user = new PatientDetails(
                    doc.data().age,
                    doc.data().crno,
                    bookdate,
                    doc.data().email,
                    doc.data().fathername,
                    doc.data().gender,
                    doc.data().hospitalText,
                    doc.data().id,
                    doc.data().isImportant,
                    doc.data().isSevere,
                    doc.data().isStarred,
                    doc.data().isNearby,
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
                
            })
            .catch(error => {
              // Session cookie is unavailable or invalid. Force user to login.
              console.log(error);
              res.redirect('/login');
            });   


    
        
} catch (error) {
    res.status(400).send(error.message);
}
})

app.get('/users/:id/bloodReports',async (req,res) => {
  try{

    const id = req.params.id;
    const bloodReports = await firestore.collection('PatientDetails').doc(id).collection('DiagnosisForm');
    const data = await bloodReports.get()
    const ReportsArray = [];
    data.forEach(doc => {
        var datee = doc.data().date
        var bookdate = datee.substring(6,8) + "-" + datee.substring(4,6) + "-" + datee.substring(0,4)
        if(doc.data().type == "blood"){
            const form = new DiagnosisForm(
                doc.id,
                id,
                doc.data().alcohole,
                doc.data().bonescan,
                doc.data().comorbidities,
                bookdate,
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
})
app.get('/users/:id/urineReports',async (req,res) => {
  try{

    const id = req.params.id;
    const bloodReports = await firestore.collection('PatientDetails').doc(id).collection('DiagnosisForm');
    const data = await bloodReports.get()
    const ReportsArray = [];
    data.forEach(doc => {
        var datee = doc.data().date
        var bookdate = datee.substring(6,8) + "-" + datee.substring(4,6) + "-" + datee.substring(0,4)
        if(doc.data().type == "urine"){
            const form = new DiagnosisForm(
                doc.id,
                id,
                doc.data().alcohole,
                doc.data().bonescan,
                doc.data().comorbidities,
                bookdate,
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
})
app.get('/users/:id/thyroidReports',async (req,res) => {
  try{

    const id = req.params.id;
    const bloodReports = await firestore.collection('PatientDetails').doc(id).collection('DiagnosisForm');
    const data = await bloodReports.get()
    const ReportsArray = [];
    data.forEach(doc => {
        var datee = doc.data().date
        var bookdate = datee.substring(6,8) + "-" + datee.substring(4,6) + "-" + datee.substring(0,4)
        if(doc.data().type == "thyroid"){
            const form = new DiagnosisForm(
                doc.id,
                id,
                doc.data().alcohole,
                doc.data().bonescan,
                doc.data().comorbidities,
                bookdate,
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
})
app.get('/users/:id/cholestrolReports',async (req,res) => {
  try{

    const id = req.params.id;
    const bloodReports = await firestore.collection('PatientDetails').doc(id).collection('DiagnosisForm');
    const data = await bloodReports.get()
    const ReportsArray = [];
    data.forEach(doc => {
        var datee = doc.data().date
        var bookdate = datee.substring(6,8) + "-" + datee.substring(4,6) + "-" + datee.substring(0,4)
        if(doc.data().type == "cholestrol"){
            const form = new DiagnosisForm(
                doc.id,
                id,
                doc.data().alcohole,
                doc.data().bonescan,
                doc.data().comorbidities,
                bookdate,
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
})

app.get('/search', (req,res) => {
  res.render("../views/search.ejs")
})

app.get('/searchedUser',async (req,res) => {
    var url = require('url')
    var url_parts = url.parse(req.url,true)
    
    var query = url_parts.query
    var name = query.name
    const users = await firestore.collection('PatientDetails');
    const data = await users.get();
    const userArray = [];
    if(data.empty) {
        res.status(404).send('No student record found');
    }else {
        data.forEach(doc => {
            var datee = doc.data().date
            var bookdate = datee.substring(6,8) + "-" + datee.substring(4,6) + "-" + datee.substring(0,4)
            if(doc.data().name.includes(name)){
                const user = new PatientDetails(
                    doc.data().age,
                    doc.data().crno,
                    bookdate,
                    doc.data().email,
                    doc.data().fathername,
                    doc.data().gender,
                    doc.data().hospitalText,
                    doc.data().id,
                    doc.data().isImportant,
                    doc.data().isSevere,
                    doc.data().isStarred,
                    doc.data().isNearby,
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

  })

app.get('/importantUser', async (req,res) => {
  try {
    const sessionCookie= req.cookies.session;
    const userDetail = await firestore.collection('Users');
        const data1 = await userDetail.get();
        const users = await firestore.collection('PatientDetails');
    const data = await users.get();
    var email = ""
    var hospitalText=""
    var unitText = ""
        //console.log(token);
        // idToken comes from the client app
        admin.auth().verifySessionCookie(
            sessionCookie, true /** checkRevoked */)
            .then((decodedClaims) => {
                //console.log(decodedClaims);

                email = decodedClaims.email
                data1.forEach(doc => {
            if(email == doc.data().email){
                hospitalText = doc.data().hospital
                unitText = doc.data().unit
                
            }
            

        })
        //res.send(hospitalText)
   
    
    const userArray = [];
    if(data.empty) {
        res.status(404).send('No student record found');
    }else {
        data.forEach(doc => {
            var datee = doc.data().date
            var bookdate = datee.substring(6,8) + "-" + datee.substring(4,6) + "-" + datee.substring(0,4)
            if(doc.data().hospitalText == hospitalText && doc.data().unitText == unitText && doc.data().isimportant == "yes"){
                const user = new PatientDetails(
                    doc.data().age,
                    doc.data().crno,
                    bookdate,
                    doc.data().email,
                    doc.data().fathername,
                    doc.data().gender,
                    doc.data().hospitalText,
                    doc.data().id,
                    doc.data().isImportant,
                    doc.data().isSevere,
                    doc.data().isStarred,
                    doc.data().isNearby,
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
                
            })
            .catch(error => {
              // Session cookie is unavailable or invalid. Force user to login.
              console.log(error);
              res.redirect('/login');
            });   


    
        
} catch (error) {
    res.status(400).send(error.message);
}
})

app.get('/starredUser', async (req,res) => {
  try {
    const sessionCookie= req.cookies.session;
    const userDetail = await firestore.collection('Users');
        const data1 = await userDetail.get();
        const users = await firestore.collection('PatientDetails');
    const data = await users.get();
    var email = ""
    var hospitalText=""
    var unitText = ""
        //console.log(token);
        // idToken comes from the client app
        admin.auth().verifySessionCookie(
            sessionCookie, true /** checkRevoked */)
            .then((decodedClaims) => {
                //console.log(decodedClaims);

                email = decodedClaims.email
                data1.forEach(doc => {
            if(email == doc.data().email){
                hospitalText = doc.data().hospital
                unitText = doc.data().unit
                
            }
            

        })
        //res.send(hospitalText)
   
    
    const userArray = [];
    if(data.empty) {
        res.status(404).send('No student record found');
    }else {
        data.forEach(doc => {
            var datee = doc.data().date
            var bookdate = datee.substring(6,8) + "-" + datee.substring(4,6) + "-" + datee.substring(0,4)
            if(doc.data().hospitalText == hospitalText && doc.data().unitText == unitText && doc.data().isstarred == "yes"){
                const user = new PatientDetails(
                    doc.data().age,
                    doc.data().crno,
                    bookdate,
                    doc.data().email,
                    doc.data().fathername,
                    doc.data().gender,
                    doc.data().hospitalText,
                    doc.data().id,
                    doc.data().isImportant,
                    doc.data().isSevere,
                    doc.data().isStarred,
                    doc.data().isNearby,
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
                
            })
            .catch(error => {
              // Session cookie is unavailable or invalid. Force user to login.
              console.log(error);
              res.redirect('/login');
            });   


    
        
} catch (error) {
    res.status(400).send(error.message);
}
})


app.get('/severeUser', async (req,res) => {
    try {
      const sessionCookie= req.cookies.session;
      const userDetail = await firestore.collection('Users');
          const data1 = await userDetail.get();
          const users = await firestore.collection('PatientDetails');
      const data = await users.get();
      var email = ""
      var hospitalText=""
      var unitText = ""
          //console.log(token);
          // idToken comes from the client app
          admin.auth().verifySessionCookie(
              sessionCookie, true /** checkRevoked */)
              .then((decodedClaims) => {
                  //console.log(decodedClaims);
  
                  email = decodedClaims.email
                  data1.forEach(doc => {
              if(email == doc.data().email){
                  hospitalText = doc.data().hospital
                  unitText = doc.data().unit
                  
              }
              
  
          })
          //res.send(hospitalText)
     
      
      const userArray = [];
      if(data.empty) {
          res.status(404).send('No student record found');
      }else {
          data.forEach(doc => {
              var datee = doc.data().date
              var bookdate = datee.substring(6,8) + "-" + datee.substring(4,6) + "-" + datee.substring(0,4)
              if(doc.data().hospitalText == hospitalText && doc.data().unitText == unitText && doc.data().issevere == "yes"){
                  const user = new PatientDetails(
                      doc.data().age,
                      doc.data().crno,
                      bookdate,
                      doc.data().email,
                      doc.data().fathername,
                      doc.data().gender,
                      doc.data().hospitalText,
                      doc.data().id,
                      doc.data().isImportant,
                      doc.data().isSevere,
                      doc.data().isStarred,
                      doc.data().isNearby,
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
                  
              })
              .catch(error => {
                // Session cookie is unavailable or invalid. Force user to login.
                console.log(error);
                res.redirect('/login');
              });   
  
  
      
          
  } catch (error) {
      res.status(400).send(error.message);
  }
  })

  app.get('/nearbyUser', async (req,res) => {
    try {
      const sessionCookie= req.cookies.session;
      const userDetail = await firestore.collection('Users');
          const data1 = await userDetail.get();
          const users = await firestore.collection('PatientDetails');
      const data = await users.get();
      var email = ""
      var hospitalText=""
      var unitText = ""
          //console.log(token);
          // idToken comes from the client app
          admin.auth().verifySessionCookie(
              sessionCookie, true /** checkRevoked */)
              .then((decodedClaims) => {
                  //console.log(decodedClaims);
  
                  email = decodedClaims.email
                  data1.forEach(doc => {
              if(email == doc.data().email){
                  hospitalText = doc.data().hospital
                  unitText = doc.data().unit
                  
              }
              
  
          })
          //res.send(hospitalText)
     
      
      const userArray = [];
      if(data.empty) {
          res.status(404).send('No student record found');
      }else {
          data.forEach(doc => {
              var datee = doc.data().date
              var bookdate = datee.substring(6,8) + "-" + datee.substring(4,6) + "-" + datee.substring(0,4)
              if(doc.data().hospitalText == hospitalText && doc.data().unitText == unitText && doc.data().isnearby == "yes"){
                  const user = new PatientDetails(
                      doc.data().age,
                      doc.data().crno,
                      bookdate,
                      doc.data().email,
                      doc.data().fathername,
                      doc.data().gender,
                      doc.data().hospitalText,
                      doc.data().id,
                      doc.data().isImportant,
                      doc.data().isSevere,
                      doc.data().isStarred,
                      doc.data().isNearby,
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
                  
              })
              .catch(error => {
                // Session cookie is unavailable or invalid. Force user to login.
                console.log(error);
                res.redirect('/login');
              });   
  
  
      
          
  } catch (error) {
      res.status(400).send(error.message);
  }
  })


app.listen(PORT, () => {
  console.log(`Listening on http://localhost:${PORT}`);
});

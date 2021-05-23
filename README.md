**CP301 - Development Engineering Project**

**Collaboration/Beneficiary Info:** PGI (Urology) doctors/staff

**TEAM** - A4
( Akshat Goel 
, Aman Bilaiya
, Deepak Kumar
, Prateek Saini )

**Project Mentor** - Dr.Puneet Goyal

## PGI Doctor's patient management (Prostate treatment)
**Motivation** - To provide the concerned collaborator with a centralized app to manage their patient records all at a single place and reduce their effort in maintaining things offline and provide efficient and fast record lookups of their patients.

**Project Summary** - A mobile application where doctors/para medical staff can manage their patient's record belonging to the same hospital unit. They can view/edit/add diagnosis reports and upload relevant documents. Also the app facilitates patients to access their medical records.  A separate web based doctor portal is also there for better view of patients records.

*** 

## 1. Features
An app to manage patient prostate diagnosis records. Main users of the app are the PGI doctors and compounders. The app has the following features :-

- Search and sort patient's records on the basis of their name, registration-date, diagnosis type etc.
- Doctors can mark patients as important,starred,severe and nearby and can easily search and sort them with the help of these tags.
- MRI, Bone scan, PSMA Test scans can be uploaded in diagnosis reports and can be viewed when needed.
- Patient portal where patient can view all his/her previous reports along with the doctor remarks and Whatsapp chat support.  
- Separate web portal where doctor can search and view his/her patient’s records based on name and tags.


*** 

## 2. User Guide

### 2.1 PGIDoctor Mobile Application

#### 2.1.1 How to use app
- Download released apk from [here](https://github.com/mnb27/PGIDoctor/releases/download/APP/PGIDoctor.apk)
- Now install apk on an android device. Best supported with android version >= 4.1 JellyBean


#### 2.1.2 How to build app locally

System Requirements - [Install](https://developer.android.com/studio/install) Android Studio 

- Clone repository
- Open project in android studio
- Sync and build gradle
- Run app

### 2.2 PGI Doctor Web
#### 2.1.1 How to use doctor portal
- Website has been deployed [here](https://pgidoctor.df.r.appspot.com/).
- Best supported with Google Chrome browser

#### 2.1.2 How to run locally

System Requirements - Install [NodeJS](https://nodejs.org/en/)

- Clone repository
- Open `admin_dep` in code editor
- On terminal
    - $ npm install
    - $ npm start
- App running on `http://localhost:8000/`

***

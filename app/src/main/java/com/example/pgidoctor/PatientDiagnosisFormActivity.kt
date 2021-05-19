package com.example.pgidoctor

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_patient_diagnosis_form.*
import kotlinx.android.synthetic.main.forgot_password.view.*
import kotlinx.android.synthetic.main.uploadscans.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class PatientDiagnosisFormActivity : AppCompatActivity() {

    var selectedPhotoUri: Uri? = null
    var profileImageUrlText = ""
    private var photoFile: File? = null
    private lateinit var mCurrentPhotoPath: String
    private val IMAGE_DIRECTORY_NAME = "dep"

    val mriFile : Int = 7
    val bonescanFile : Int = 8
    val psmapetFile : Int = 9

    lateinit var uri : Uri
    var mriLink = ""
    var bonescanLink = ""
    var psmapetLink = ""
    lateinit var mStorage : StorageReference

    var previousDetails: PatientDetails? = null
    var ttesttype: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_diagnosis_form)

        ttesttype = intent.extras?.get("test_type").toString()
        previousDetails = intent.extras?.get("previousDetails") as PatientDetails

        val uploadAll = findViewById<View>(R.id.uploadhere) as Button
        mStorage = FirebaseStorage.getInstance().getReference("Uploads")

//        uploadMRI.setOnClickListener(View.OnClickListener {
//            view: View? -> val intent = Intent()
//            intent.setType ("*/*")
//            intent.setAction(Intent.ACTION_GET_CONTENT)
//            startActivityForResult(Intent.createChooser(intent, "Select File"), mriFile)
//        })

//        uploadMRI.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                captureImage()
//            } else {
//                captureImage2()
//            }
//        }

        uploadAll.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.uploadscans,null);
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Capture or upload scan")
            val mAlertDialog = mBuilder.show()

            mDialogView.mrifolder.setOnClickListener {
//                mAlertDialog.dismiss()
                  view: View? -> val intent = Intent()
                  intent.setType ("*/*")
                  intent.setAction(Intent.ACTION_GET_CONTENT)
                  startActivityForResult(Intent.createChooser(intent, "Select File"), mriFile)
            }
            mDialogView.mricamera.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    captureImage(mriFile+1)
                } else {
                    captureImage2(mriFile+1)
                }
            }


            mDialogView.bonescanfolder.setOnClickListener {
                    view: View? -> val intent = Intent()
                intent.setType ("*/*")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(intent, "Select File"), bonescanFile)
            }
            mDialogView.bonescancamera.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    captureImage(bonescanFile+1)
                } else {
                    captureImage2(bonescanFile+1)
                }
            }


            mDialogView.psmafolder.setOnClickListener {
                    view: View? -> val intent = Intent()
                intent.setType ("*/*")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(intent, "Select File"), psmapetFile)
            }
            mDialogView.psmacamera.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    captureImage(psmapetFile+1)
                } else {
                    captureImage2(psmapetFile+1)
                }
            }

        }

//        uploadBonescan.setOnClickListener(View.OnClickListener {
//            view: View? -> val intent = Intent()
//            intent.setType ("*/*")
//            intent.setAction(Intent.ACTION_GET_CONTENT)
//            startActivityForResult(Intent.createChooser(intent, "Select File"), bonescanFile)
//        })
//
//        uploadPSMAPET.setOnClickListener(View.OnClickListener {
//            view: View? -> val intent = Intent()
//            intent.setType ("*/*")
//            intent.setAction(Intent.ACTION_GET_CONTENT)
//            startActivityForResult(Intent.createChooser(intent, "Select File"), psmapetFile)
//        })

        val name: TextInputLayout = findViewById(R.id.one)
        val hospital: TextInputLayout = findViewById(R.id.onee)
        val unit: TextInputLayout = findViewById(R.id.oneee)

        name.editText?.setText(previousDetails?.name)
        hospital.editText?.setText(previousDetails?.hospitalText)
        unit.editText?.setText(previousDetails?.unitText)

        val id = previousDetails?.id

        val mPickTimeBtn = findViewById<Button>(R.id.pickDateBtn) // date

        val date: TextInputLayout = findViewById(R.id.date)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var dateChoose = ""

        mPickTimeBtn.setOnClickListener {
            var dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                date.editText?.setText("" + dayOfMonth + " / " + (monthOfYear.toInt()+1).toString() + " / " + year)
                var daynumber = ""
                var monthnumber = (monthOfYear+1).toString()
                var correctMonth = monthOfYear + 1
                if (correctMonth < 10) {
                    monthnumber = "0$correctMonth"
                }
                if (dayOfMonth < 10) {
                    daynumber = "0$dayOfMonth"
                    dateChoose = year.toString() + monthnumber + daynumber
                }
                else {dateChoose = year.toString() + monthnumber + dayOfMonth.toString()}
            }, year, month, day)
            dpd.show()
        }


        val firestore = FirebaseFirestore.getInstance()


                    val weight: TextInputLayout = findViewById(R.id.two)
                    val height: TextInputLayout = findViewById(R.id.three)
                    val smoking: TextInputLayout = findViewById(R.id.four)
                    val alcohole: TextInputLayout = findViewById(R.id.five)
                    val comorbidities: TextInputLayout = findViewById(R.id.six)
                    val familyho: TextInputLayout = findViewById(R.id.seven)
                    val remarks: TextInputLayout = findViewById(R.id.eight)
                    val medicines: TextInputLayout = findViewById(R.id.nine)

                    val saveButton: Button = findViewById(R.id.saveButton)

                    val auth = FirebaseAuth.getInstance()

                    saveButton.setOnClickListener {

                        Log.d("TAG", "File url 2: $profileImageUrlText")

                        var nameText = previousDetails?.name.toString()
                        var hospitalText = previousDetails?.hospitalText.toString()
                        var unitText = previousDetails?.unitText.toString()
                        val dateText = dateChoose
                        var weightText = weight.editText?.text.toString()
                        var heightText = height.editText?.text.toString()
                        var smokingText = smoking.editText?.text.toString()
                        var alcoholeText = alcohole.editText?.text.toString()
                        var comorbiditiesText = comorbidities.editText?.text.toString()
                        var familyhoText = familyho.editText?.text.toString()
                        var bonescanText = bonescanLink
                        var mriText = mriLink
                        var psmapetText = psmapetLink
                        var remarksText = remarks.editText?.text.toString()
                        var medicinesText = medicines.editText?.text.toString()

                        var id2 = SimpleDateFormat("ddMMyyyyhh:mm:ss")
                        val id1 = id2.format(Date())

                        val patientDetails = PatientDiagnosisDetails(
                            nameText,
                            hospitalText,
                            unitText,
                            dateText,
                            weightText,
                            heightText,
                            smokingText,
                            alcoholeText,
                            comorbiditiesText,
                            familyhoText,
                            bonescanText,
                            mriText,
                            psmapetText,
                            ttesttype!!,
                            remarksText,
                            medicinesText,
                                id1
                        )
                        val firestore = FirebaseFirestore.getInstance().collection("PatientDetails")


                        if (id != null) {
                            firestore.document(id).collection("DiagnosisForm").document(id1).set(patientDetails)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Successfully Saved", Toast.LENGTH_LONG).show()
                                        val intent = Intent(this, AssignedPatients::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }


                    }

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == mriFile) {
//                uri = data!!.data!!
//                uploadMRI()
//            }else if (requestCode == bonescanFile) {
//                uri = data!!.data!!
//                uploadBonescan()
//            }else if (requestCode == psmapetFile) {
//                uri = data!!.data!!
//                uploadPSMAPET()
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

                if (requestCode == mriFile && resultCode == Activity.RESULT_OK && data != null) {
                    // proceed and check what the selected image was....
                    Log.d("Collect data activity","photo was selected")
                    selectedPhotoUri = data.data
                    uploadMRItoFirestore();
                }
                if (requestCode == mriFile+1 && resultCode == Activity.RESULT_OK) {
                    uploadMRItoFirestore();
                }

                if (requestCode == bonescanFile && resultCode == Activity.RESULT_OK && data != null) {
                    // proceed and check what the selected image was....
                    Log.d("Collect data activity","photo was selected")
                    selectedPhotoUri = data.data
                    uploaBonescantoFirestore();
                }
                if (requestCode == bonescanFile+1 && resultCode == Activity.RESULT_OK) {
                    uploaBonescantoFirestore();
                }


                if (requestCode == psmapetFile && resultCode == Activity.RESULT_OK && data != null) {
                    // proceed and check what the selected image was....
                    Log.d("Collect data activity","photo was selected")
                    selectedPhotoUri = data.data
                    uploadPsmapettoFirestore();
                }
                if (requestCode == psmapetFile+1 && resultCode == Activity.RESULT_OK) {
                    uploadPsmapettoFirestore();
                }

    }

    private fun uploadMRItoFirestore() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/MRIScan/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("collect data activity", "Successfully uploaded image: ${it.metadata?.path}")
                Toast.makeText(this, "Uploaded MRI :)", Toast.LENGTH_LONG).show()
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("TAG", "File Location: $it")
                    mriLink = it.toString()
                    Log.d("TAG", "File url: $mriLink")
//                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Failed to upload image to storage: ${it.message}")
                Toast.makeText(this, "Failed To Upload :)", Toast.LENGTH_LONG).show()
            }
    }

    private fun uploaBonescantoFirestore() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/BoneScan/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("collect data activity", "Successfully uploaded image: ${it.metadata?.path}")
                Toast.makeText(this, "Uploaded Bonescan :)", Toast.LENGTH_LONG).show()
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("TAG", "File Location: $it")
                    bonescanLink = it.toString()
                    Log.d("TAG", "File url: $bonescanLink")
//                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Failed to upload image to storage: ${it.message}")
                Toast.makeText(this, "Failed To Upload :)", Toast.LENGTH_LONG).show()
            }
    }

    private fun uploadPsmapettoFirestore() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/PSMAPET/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("collect data activity", "Successfully uploaded image: ${it.metadata?.path}")
                Toast.makeText(this, "Uploaded PSMA :)", Toast.LENGTH_LONG).show()
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("TAG", "File Location: $it")
                    psmapetLink = it.toString()
                    Log.d("TAG", "File url: $psmapetLink")
//                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Failed to upload image to storage: ${it.message}")
                Toast.makeText(this, "Failed To Upload :)", Toast.LENGTH_LONG).show()
            }
    }

    /* Capture Image function for 4.4.4 and lower. Not tested for Android Version 3 and 2 */
    private fun captureImage2(requestCode: Int) {

        try {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = createImageFile4()
            if (photoFile != null) {
                displayMessage(baseContext, photoFile!!.getAbsolutePath())
                Log.i("Aman", photoFile!!.getAbsolutePath())
                selectedPhotoUri = Uri.fromFile(photoFile)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedPhotoUri)
                startActivityForResult(cameraIntent, requestCode)
            }
        } catch (e: Exception) {
            displayMessage(baseContext, "Camera is not available." + e.toString())
        }

    }

    private fun captureImage(requestCode: Int) {

        if (ContextCompat.checkSelfPermission(this,  android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                try {

                    photoFile = createImageFile()
                    displayMessage(baseContext, photoFile!!.getAbsolutePath())
                    Log.i("Aman", photoFile!!.getAbsolutePath())

                    // Continue only if the File was successfully created
                    if (photoFile != null) {


                        selectedPhotoUri = FileProvider.getUriForFile(this,
                            "com.example.pgidoctor",
                            photoFile!!
                        )

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedPhotoUri)

                        startActivityForResult(takePictureIntent, requestCode)

                    }
                } catch (ex: Exception) {
                    // Error occurred while creating the File
                    displayMessage(baseContext,"Capture Image Bug: "  + ex.message.toString())
                }

            } else {
                displayMessage(baseContext, "Nullll")
            }
        }
    }

    private fun createImageFile4(): File? {
        // External sdcard location
        val mediaStorageDir = File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            IMAGE_DIRECTORY_NAME)
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                displayMessage(baseContext, "Unable to create directory.")
                return null
            }
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
            Locale.getDefault()).format(Date())

        return File(mediaStorageDir.path + File.separator
                + "IMG_" + timeStamp + ".jpg")

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun displayMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureImage(requestCode)
            }
        }

    }


}
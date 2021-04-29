package com.example.pgidoctor

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider

import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CollectDataActivity : AppCompatActivity() {

    var profileImageUrlText = ""
    var selectedPhotoUri: Uri? = null

    private lateinit var selectphoto_imageview_register: ImageView
    private lateinit var btCapturePhoto: Button
    private var photoFile: File? = null
    private val CAPTURE_IMAGE_REQUEST = 1
    private lateinit var mCurrentPhotoPath: String
    private val IMAGE_DIRECTORY_NAME = "dep"

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d("Collect data activity","photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selectphoto_imageview_register.setImageBitmap(bitmap)

//            selectphoto_button_register.alpha = 0f
            selectphoto_imageview_register.setBackgroundColor(Color.parseColor("#ffffff"))

//      val bitmapDrawable = BitmapDrawable(bitmap)
//      selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.getAbsolutePath())
            selectphoto_imageview_register.setImageBitmap(myBitmap)
            selectphoto_imageview_register.setBackgroundColor(Color.parseColor("#ffffff"))
        } else {
//            displayMessage(baseContext, "Request cancelled or something went wrong.")
        }

        uploadImageToFirebaseStorage();
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("collect data activity", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("TAG", "File Location: $it")
                    profileImageUrlText = it.toString()
                    Log.d("TAG", "File url: $profileImageUrlText")
//                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Failed to upload image to storage: ${it.message}")
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect_data)

        val pick: Button = findViewById(R.id.selectphoto_button_register)

        // select image
        pick.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        // camera
        selectphoto_imageview_register = findViewById(R.id.selectphoto_imageview_register)
        btCapturePhoto = findViewById(R.id.btCapturePhoto)
        btCapturePhoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                captureImage()
            } else {
                captureImage2()
            }
        }

        /// date picker start
        val mPickTimeBtn = findViewById<Button>(R.id.pickDateBtn) // date

        val date: TextInputLayout = findViewById(R.id.three)

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

            val now = System.currentTimeMillis() - 1000
            //dpd.datePicker.minDate = now
            dpd.datePicker.maxDate = now + (1000*60*60*24*7)
            dpd.show()
        }

        //////end

        val name: TextInputLayout = findViewById(R.id.one)
        val email: TextInputLayout = findViewById(R.id.mail)
        val fathername: TextInputLayout = findViewById(R.id.two)
        val datecollected: TextInputLayout = findViewById(R.id.three)
        val crno: TextInputLayout = findViewById(R.id.four)
        val mobile: TextInputLayout = findViewById(R.id.five)
        val age: TextInputLayout = findViewById(R.id.six)
        var male: RadioButton = findViewById(R.id.male)
        val hospital: TextInputLayout = findViewById(R.id.eight)
        val unit: TextInputLayout = findViewById(R.id.nine)


        val saveButton: Button = findViewById(R.id.saveButton)


        val auth = FirebaseAuth.getInstance()

        saveButton.setOnClickListener {
            var nameText = name.editText?.text.toString()
            var emailText = email.editText?.text.toString()
            var fathernameText = fathername.editText?.text.toString()
            var datecollectedText = dateChoose
            var crnoText = crno.editText?.text.toString()
            var mobileText = mobile.editText?.text.toString()
            var ageText = age.editText?.text.toString()

            var hospitalText = hospital.editText?.text.toString()
            var unitText = unit.editText?.text.toString()

            var gender:String
            if(male.isChecked) gender="male"
            else gender="female"

            var genderText = gender

            if (nameText.isEmpty()) {
                name.error = "Required Field"
                return@setOnClickListener
            }

            if (emailText.isEmpty()) {
                email.error = "Required Field"
                return@setOnClickListener
            }

            if (datecollectedText.isEmpty()) {
                fathername.error = "Required Field"
                return@setOnClickListener
            }

            if (mobileText.isEmpty()) {
                mobile.error = "Required Field"
                return@setOnClickListener
            }

            if (hospitalText.isEmpty()) {
                hospital.error = "Required Field"
                return@setOnClickListener
            }

            if (unitText.isEmpty()) {
                unit.error = "Required Field"
                return@setOnClickListener
            }


            Log.d("TAG", "File url 2: $profileImageUrlText")

            var id = SimpleDateFormat("ddMMyyyyhh:mm:ss")
            val id1 = id.format(Date())

            val patientDetails = PatientDetails(nameText, fathernameText, datecollectedText, crnoText, mobileText, ageText, genderText,profileImageUrlText,hospitalText,unitText,id1,"","","","",emailText)
            val firestore = FirebaseFirestore.getInstance().collection("PatientDetails")



            firestore.document(id1).set(patientDetails)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully Saved", Toast.LENGTH_LONG).show()
                        auth.createUserWithEmailAndPassword(emailText, "Test12345")
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = User(
                                        auth.currentUser?.uid!!,
                                        nameText,
                                        mobileText,
                                        emailText,
                                        "Patient",
                                        hospitalText,
                                        unitText
                                    )
                                    val firestore = FirebaseFirestore.getInstance()
                                    firestore.collection("Users").document(auth.currentUser.uid).set(user)

                                } else {
                                    Toast.makeText(this, "Unable to add member", Toast.LENGTH_LONG).show()
                                }
                            }
                        val intent = Intent(this, OpenActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                    }
                }

            }
        }

    /* Capture Image function for 4.4.4 and lower. Not tested for Android Version 3 and 2 */
    private fun captureImage2() {

        try {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = createImageFile4()
            if (photoFile != null) {
                displayMessage(baseContext, photoFile!!.getAbsolutePath())
                Log.i("Aman", photoFile!!.getAbsolutePath())
                selectedPhotoUri = Uri.fromFile(photoFile)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedPhotoUri)
                startActivityForResult(cameraIntent, 1)
            }
        } catch (e: Exception) {
            displayMessage(baseContext, "Camera is not available." + e.toString())
        }

    }

    private fun captureImage() {

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

                        startActivityForResult(takePictureIntent, 1)

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
                captureImage()
            }
        }

    }

}
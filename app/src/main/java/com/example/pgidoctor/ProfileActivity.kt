package com.example.pgidoctor

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatDrawableManager.get
import androidx.appcompat.widget.ResourceManagerInternal.get
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.okhttp.HttpUrl.get
import com.squareup.picasso.Picasso
import com.squareup.picasso.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.change_password_layout.*
import kotlinx.android.synthetic.main.change_password_layout.view.*
import org.jetbrains.anko.db.NULL
import java.util.*


class ProfileActivity : AppCompatActivity() {
    var profileImageUrlText = ""
    var selectedPhotoUri: Uri ?= null
    var uuuid = ""
    private lateinit var authh : FirebaseAuth
    lateinit var person_image : ImageView
    var  storageref : StorageReference ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        authh = FirebaseAuth.getInstance()
        person_image = findViewById<ImageView>(R.id.person_image);
        val fireStore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        storageref = FirebaseStorage.getInstance().getReference();
        val user_email = auth.currentUser?.email
        var editprofile = findViewById<Button>(R.id.edit_profile);
        var changepassword = findViewById<Button>(R.id.change_password);

        if (user_email != null) {
            uuuid = user_email
        }
        var profileref = storageref!!.child("ProfileImage/"+user_email)
        profileref.downloadUrl.addOnSuccessListener {
                uri ->  Picasso.with(this).load(uri).into(person_image)
        }

        var name = findViewById<TextView>(R.id.person_name);
        var name1 = findViewById<TextView>(R.id.person_name1);
        var email = findViewById<TextView>(R.id.person_email);
        var mobile = findViewById<TextView>(R.id.person_mobile);
        var addr = findViewById<TextView>(R.id.person_address);

        email.setText(user_email)

        fireStore.collection("Users").whereEqualTo("email",user_email).get()
            .addOnCompleteListener(){
                if(it.isSuccessful){
                    for(document in it.result!!){
                        name.setText(""+document.data.getValue("name"));
                        name1.setText("@"+document.data.getValue("name"));
                        mobile.setText("+91 "+document.data.getValue("mobile"));
                        addr.setText("Hospital: " + document.data.getValue("hospital") + ", Unit: " + document.data.getValue("unit"));
                    }
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
            }





        editprofile.setOnClickListener {
            val opengallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(opengallery,1000);
        }


        changepassword.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.change_password_layout,null);
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
//                .setTitle("Change Password")
            val mAlertDialog = mBuilder.show()
            mDialogView.save_button.setOnClickListener {
                mAlertDialog.dismiss()
                val ps1 = mDialogView.new_pswd.editText?.text.toString()
                val ps2 = mDialogView.reenter_new_pswd.editText?.text.toString()
                if(mDialogView.new_pswd.editText?.text.toString().isNotEmpty()&&mDialogView.reenter_new_pswd.editText?.text.toString().isNotEmpty()&&mDialogView.current_pswd.editText?.text.toString().isNotEmpty()){
                    if(ps1!=ps2){
                        Toast.makeText(this,"Enter same password in both fields",Toast.LENGTH_LONG).show()
                    }
                    else{
                        val userr = authh.currentUser!!
                        if(userr!=null&&userr.email!=null){
                            val credential = EmailAuthProvider
                                .getCredential(userr.email!!, mDialogView.current_pswd.editText?.text.toString())

                            userr.reauthenticate(credential)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(this, "Re-Authetication Successful", Toast.LENGTH_LONG).show()

                                        val newPassword = ps1

                                        userr!!.updatePassword(newPassword)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_LONG).show()
                                                    authh.signOut()
                                                    val intent = Intent(this,MainActivity::class.java)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                            }

                                    }
                                    else{
                                        Toast.makeText(this, "Re-Authetication Failed", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                        else{
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                else{
                    Toast.makeText(this,"Please enter all the fields",Toast.LENGTH_LONG).show()
                }

            }
        }
    }
    override fun onActivityResult(requestCode : Int ,resultCode : Int,data:Intent?){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1000) {
            if (resultCode == Activity.RESULT_OK&&data!=NULL) {
                selectedPhotoUri = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

                person_image.setImageBitmap(bitmap)

                uploadImageToFirebase(uuuid);
//               Log.d("hello","$uuuid");
            }
        }
    }
    private fun uploadImageToFirebase(filename : String) {
        if (selectedPhotoUri == null) return

//        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/ProfileImage/"+filename)

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
}
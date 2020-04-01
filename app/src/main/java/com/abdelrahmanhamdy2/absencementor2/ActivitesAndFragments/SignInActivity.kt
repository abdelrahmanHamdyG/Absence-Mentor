package com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.checkNetwork
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.initProgress
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.showToast
import com.abdelrahmanhamdy2.absencementor2.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    var firebaseAuth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        buttonSignIn.setOnClickListener {
            var progressSignin= initProgress(this)

            var textEmail=editTextSignInEmail.text.toString()
            var textPassword=editTextSignInPassword.text!!.toString()

            if (textEmail.isEmpty()||textPassword.isEmpty()){

                showToast(this,"Empty")
            }else{

                if (!checkNetwork(this)){

                    showToast(this,"Check you Internet connection")

                }else{

                    progressSignin.show()
                    firebaseAuth.signInWithEmailAndPassword(textEmail,textPassword).addOnCompleteListener {


                        if (it.isSuccessful){

                            progressSignin.dismiss()

                            var iToGroups=Intent(this,
                                com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.GroupsActivity::class.java)
                            iToGroups.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(iToGroups)

                        }else{

                            progressSignin.dismiss()

                            showToast(this, it.exception!!.message!!)

                        }

                    }

                }

            }

        }


    }
}

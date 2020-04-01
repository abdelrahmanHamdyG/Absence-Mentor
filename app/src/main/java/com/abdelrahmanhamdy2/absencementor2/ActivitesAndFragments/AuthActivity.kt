package com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.Infromations
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.UIDS
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.checkNetwork
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.initProgress
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.showToast
import com.abdelrahmanhamdy2.absencementor2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {


    var firebaseAuth = FirebaseAuth.getInstance()
    lateinit var progressAuth:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)



    }

    override fun onStart() {
        super.onStart()


        privacy.setOnClickListener {

            val dialogPrivacy=Dialog(this)
            val pridialog=LayoutInflater.from(this).inflate(R.layout.dialog_privacy,null)
            dialogPrivacy.setContentView(pridialog)
            dialogPrivacy.show()

        }

        textToSignIn.setOnClickListener {

            startActivity(Intent(this, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.SignInActivity::class.java))
        }


            buttonRegisterAuthActivity.setOnClickListener {


                if (editTextAuthEmail.text!!.isEmpty() || editTextAuthPassword.text!!.isEmpty() || editTextAuthName.text!!.isEmpty()) {

                    showToast(this, "Empty")

                } else {

                    if (!checkNetwork(this)) {

                        showToast(this, "check Internet Connection ")
                    } else {

                        progressAuth = initProgress(this)
                        progressAuth.show()
                        CoroutineScope(IO).launch {
                            register()

                        }

                }

            }


        }
    }

    suspend fun register(){

        var firebaseDatabase = FirebaseDatabase.getInstance()
        val dbRefUIDS=firebaseDatabase.getReference(UIDS)

        firebaseAuth.createUserWithEmailAndPassword(editTextAuthEmail.text.toString(), editTextAuthPassword.text.toString())
            .addOnCompleteListener {


            if (it.isSuccessful) {

                CoroutineScope(Main).launch {
                    showToast(this@AuthActivity, "You registered Successfully")
                }
                firebaseAuth.signInWithEmailAndPassword(editTextAuthEmail.text.toString(), editTextAuthPassword.text.toString())
                    .addOnCompleteListener {

                    val uid = firebaseAuth.currentUser!!.uid

                    dbRefUIDS.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                            showToast(this@AuthActivity, p0.message)
                            dismissDialog()

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            var x = 0
                            for (i in p0.children) {

                                x += 1

                            }
                            var id="141$x"
                            var dbRefUIDSsetData=firebaseDatabase.getReference(UIDS).child(id)
                            dbRefUIDSsetData.push().setValue(uid)
                            var   dbRefInfromtaions = firebaseDatabase.getReference(uid)
                                .child(Infromations)
                            dbRefInfromtaions.setValue(
                                com.abdelrahmanhamdy2.absencementor2.Models.ModelInformations(
                                    editTextAuthName.text.toString(),
                                    id
                                )
                            )
                            dismissDialog()
                            var iToGroups=Intent(this@AuthActivity, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.GroupsActivity::class.java)
                            iToGroups.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(iToGroups)


                        }


                    })

                }

            } else {
                CoroutineScope(Main).launch {
                    showToast(this@AuthActivity,it.exception!!.message!!)
                }
                dismissDialog()
            }

        }


    }


    fun dismissDialog(){

        CoroutineScope(Main).launch {
            progressAuth.dismiss()
        }
    }


}




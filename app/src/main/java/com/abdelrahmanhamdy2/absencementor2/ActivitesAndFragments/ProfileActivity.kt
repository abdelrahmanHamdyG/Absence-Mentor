package com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.Infromations
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.NumberOfGroups
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.showToast
import com.abdelrahmanhamdy2.absencementor2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {


    private var firebaseAuth = FirebaseAuth.getInstance()
    private var uid = firebaseAuth.currentUser!!.uid
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        setSupportActionBar(toolbarProfile)
        supportActionBar!!.title = "Profile"

    }


    override fun onStart() {
        super.onStart()

        val dbRefGetNumberOfGroups=firebaseDatabase.getReference(uid).child(Infromations).child(NumberOfGroups)
        dbRefGetNumberOfGroups.addListenerForSingleValueEvent(object :ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.value==null){

                    textGroups2ProfileActivity.text="0"
                }else {
                    textGroups2ProfileActivity.text = p0.getValue(Int::class.java).toString()
                }
            }

        })

        val dbRefGetInfromations=firebaseDatabase.getReference(uid).child(Infromations)
        dbRefGetInfromations.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                showToast(this@ProfileActivity,p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val data=p0.getValue(com.abdelrahmanhamdy2.absencementor2.Models.ModelInformations::class.java)
                textName2ProfileActivity.text=data!!.name
                textID2ProfileActivity.text=data.id

            }


        })


    }
    }





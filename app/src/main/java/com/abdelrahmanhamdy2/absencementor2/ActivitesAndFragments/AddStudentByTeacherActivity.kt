package com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.GroupsNow
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.StudentAbsentsPath
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.StudentpresentsPath
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.TheFather
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.checkNetwork
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.initProgress
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.intentNameGroupExtra
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.showToast
import com.abdelrahmanhamdy2.absencementor2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_student_by_teacher.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class AddStudentByTeacherActivity : AppCompatActivity() {

    private var firebase=FirebaseDatabase.getInstance()
    private var firebaseAuth=FirebaseAuth.getInstance()
    private var uid=firebaseAuth.currentUser!!.uid
    lateinit var iDataFromMain:String
    private lateinit var progressAdd:ProgressDialog
    private lateinit var progressRemove:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student_by_teacher)




        iDataFromMain=intent.extras!!.getString(intentNameGroupExtra)!!



        buttonAddAddStudentByTeacherActivity.setOnClickListener {

            if (!checkNetwork(this)) {
                showToast(this, "check Your Internet")

            } else {
                val id = editIdAddStudentByTeacherActivity.text.toString()

                CoroutineScope(IO).launch {
                    addStudentTPresentsAndRemoveFromAbsents(id)


                }


            }
        }

        buttonRemoveAddStudentByTeacherActivity.setOnClickListener {
            if (!checkNetwork(this)) {

                showToast(this, "check Your Internet")

            } else {

                val id = editIdAddStudentByTeacherActivity.text.toString()
                CoroutineScope(IO).launch {

                    removeStudentFromPresentsAndAddToAbsents(id)
                }

            }
        }
    }

















    private fun addStudentTPresentsAndRemoveFromAbsents(id:String){

        CoroutineScope(Main).launch {
            progressAdd = initProgress(this@AddStudentByTeacherActivity)
        }
        showAddDialog()


        val dbRefRemoveStudentFromAbsents= firebase.getReference(uid).child(GroupsNow).child(iDataFromMain).child(TheFather).child(StudentAbsentsPath)
        dbRefRemoveStudentFromAbsents.orderByChild("idOfStudent").equalTo(id).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

                dismissAddDialogOnMainThread()
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.value==null){

                    CoroutineScope(Main).launch {

                        dismissAddDialogOnMainThread()

                        showToastOnMainThread("You added Your self Before")
                    }
                }else{

                    for (i in p0.children){

                        val data=i.getValue(com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase::class.java)
                        val dbRemoveValue=firebase.getReference(uid).child(GroupsNow).child(iDataFromMain).child(TheFather).child(StudentAbsentsPath).child(data!!.keyOfStudent!!)
                        dbRemoveValue.removeValue().addOnCompleteListener {
                        val dbRefAddToPresents=firebase.getReference(uid).child(GroupsNow).child(iDataFromMain).child(TheFather).child(StudentpresentsPath).push()

                            dbRefAddToPresents.setValue(
                                com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase(
                                    data.nameOfStudent!!,
                                    data.idOfStudent!!,
                                    dbRefAddToPresents.key!!
                                )
                            ).addOnCompleteListener {

                                CoroutineScope(Main).launch{

                                    dismissAddDialogOnMainThread()
                                    showToastOnMainThread("you added ${data.nameOfStudent} successfully")
                                }

                            }


                        }
                    }


                }


            }

        })


    }


    private fun removeStudentFromPresentsAndAddToAbsents(id:String){

        CoroutineScope(Main).launch {
        progressRemove= initProgress(this@AddStudentByTeacherActivity)
        }
        showRemoveDialog()


        val dbRefRemoveFromPresents=firebase.getReference(uid).child(GroupsNow).child(iDataFromMain).child(
            TheFather).child(StudentpresentsPath)

        dbRefRemoveFromPresents.orderByChild("idOfStudent").equalTo(id).addListenerForSingleValueEvent(object :ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
                dismissRemoveDialogOnMainThread()

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.value==null){

                    dismissRemoveDialogOnMainThread()

                    showToastOnMainThread("You Removed this Before")
                }else{

                for (i in p0.children){

                    val data=i.getValue(com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase::class.java)
                    val dbRefRemoveValue=firebase.getReference(uid).child(GroupsNow).child(iDataFromMain).child(
                        TheFather).child(StudentpresentsPath).child(data!!.keyOfStudent!!)

                        dbRefRemoveValue.removeValue().addOnCompleteListener {

                            val dbRefAddToAbsents=firebase.getReference(uid).child(GroupsNow).child(iDataFromMain).child(
                                TheFather).child(StudentAbsentsPath).push()
                            dbRefAddToAbsents.setValue(
                                com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase(
                                    data.nameOfStudent!!,
                                    data.idOfStudent!!,
                                    dbRefAddToAbsents.key.toString()
                                )
                            ).addOnCompleteListener {

                                dismissRemoveDialogOnMainThread()
                                showToastOnMainThread("you removed ${data.nameOfStudent}")

                                }

                            }
                        }
                    }
                }
            }
        )
    }


      fun showToastOnMainThread(string:String){

        showToast(this@AddStudentByTeacherActivity,string)

        }

    fun dismissAddDialogOnMainThread(){

        CoroutineScope(Main).launch {

            progressAdd.dismiss()

        }

    }

    fun dismissRemoveDialogOnMainThread(){

        CoroutineScope(Main).launch {

            progressRemove.dismiss()

        }

    }
    private fun showAddDialog(){

        CoroutineScope(Main).launch {
            progressAdd.show()
        }
    }

    private fun showRemoveDialog(){

        CoroutineScope(Main).launch {
            progressRemove.show()
        }
    }


}

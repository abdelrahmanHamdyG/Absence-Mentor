package com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.MyGroups
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.checkNetwork
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.initProgress
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.showRecycler
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.showToast
import com.abdelrahmanhamdy2.absencementor2.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_to_group.*
import kotlinx.android.synthetic.main.dailog_finish_add_to_group_activity.view.*
import kotlinx.android.synthetic.main.dialog_add_to_group_activity.view.*

class AddToGroupActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    
    
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.navigation_groups->{
                drawerAddToGroup.closeDrawers()
                startActivity(Intent(this,
                    com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.GroupsActivity::class.java))
                return true
            }
            R.id.navigation_history ->{
                drawerAddToGroup.closeDrawers()
                startActivity(Intent(this, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.HistoryActivity::class.java))
                    return true
            
            }
            R.id.navigation_add_groups -> {


                return true
            }
            R.id.navigation_profile ->{
                drawerAddToGroup.closeDrawers()
                startActivity(Intent(this, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.ProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK));return true}


            R.id.ShareButton-> {
                drawerAddToGroup.closeDrawers()
                showToast(this,"We are working On It")
                return true
            }

            R.id.Communicate->{

                drawerAddToGroup.closeDrawers()
                val iCommunicate=Intent(Intent.ACTION_VIEW)
                iCommunicate.data = Uri.parse("mailto:?subject=StudentsApp &to=dhamdy078@gmail.com")
                startActivity(iCommunicate)
                return true
            }

            R.id.signOut->{
                drawerAddToGroup.closeDrawers()
                firebaseAuth.signOut()
                val i=Intent(this, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AuthActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                return true
            }
            else->{
                drawerAddToGroup.closeDrawers()

                return false
            }
        }
        
    }

    private var arrayOfStudent = ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArray>()
    private var firebaseAuth= FirebaseAuth.getInstance()
    private var uid=firebaseAuth.currentUser!!.uid
    private lateinit var firebaseDatabase: FirebaseDatabase
    private var numberArray=ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_group)




        textAddStudentIfNot.visibility= View.VISIBLE
        setSupportActionBar(toolbarAddToGroup)
        supportActionBar!!.title = "Add Students"

        val actionBarToggle= ActionBarDrawerToggle(this,drawerAddToGroup,toolbarAddToGroup, R.string.bottom_sheet_behavior, R.string.bottom_sheet_behavior)
        actionBarToggle.syncState()
        drawerAddToGroup.addDrawerListener(actionBarToggle)
        navigationAddToGroup.setNavigationItemSelectedListener(this)




        

        floatingActionButtonAddToGroupActivity.setOnClickListener {

            var dialogAddToGroupActivity = Dialog(this)
            dialogAddToGroupActivity.create()
            val view =
                LayoutInflater.from(this).inflate(R.layout.dialog_add_to_group_activity, null)
            dialogAddToGroupActivity.setContentView(view)
            dialogAddToGroupActivity.setCancelable(false)
            dialogAddToGroupActivity.show()

            /////////////////////////////////////////

            var numberInEdit = 0
            numberInEdit = if (arrayOfStudent.isEmpty()) {
                1
            } else {
                arrayOfStudent.size + 1

            }

            view.DialogEditIdAddToGroupActivity.setText(numberInEdit.toString())




            view.DialogButtonCancelAddToGroupActivity.setOnClickListener {

                dialogAddToGroupActivity.dismiss()


            }

            view.DialogButtonAddAddToGroupActivity.setOnClickListener {

                textAddStudentIfNot.visibility = View.GONE
                val nameOfStudent = view.DialogEditStudentAddToGroupActivity.text.toString()
                val idOfStudent = view.DialogEditIdAddToGroupActivity.text.toString()
                if (numberArray.contains(idOfStudent.toInt())) {

                    showToast(this,"choose another number")

                } else {


                    arrayOfStudent.add(
                        com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArray(
                            nameOfStudent,
                            idOfStudent
                        )
                    )
                    numberArray.add(idOfStudent.toInt())
                    showRecycler(
                        recyclerViewAddToGroupActivity,
                        arrayOfStudent,numberArray,
                        this@AddToGroupActivity
                    )

                    val sum = arrayOfStudent.size + 1
                    view.DialogEditStudentAddToGroupActivity.setText("")
                    view.DialogEditIdAddToGroupActivity.setText(sum.toString())
                }

            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_group_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.ClearAllAddToGroupActivity -> {

                arrayOfStudent.clear()
                numberArray.clear()
                textAddStudentIfNot.visibility=View.VISIBLE
                return true
            }
            R.id.FinishAddToGroupActivity -> {

                if (arrayOfStudent.size <= 1) {


                    showToast(this@AddToGroupActivity, "You must add 2 students At Least ")

                } else {

                    val dialog = Dialog(this)
                    dialog.create()
                    val viewFinishDialog = LayoutInflater.from(this)
                        .inflate(R.layout.dailog_finish_add_to_group_activity, null)
                    dialog.setContentView(viewFinishDialog)
                    dialog.setTitle("Enter Section name")
                    dialog.show()


                    viewFinishDialog.dialogFinishButtonNo.setOnClickListener {

                        dialog.dismiss()

                    }

                    viewFinishDialog.dialogFinishButtonYes.setOnClickListener {


                        if (!checkNetwork(this)) {

                            showToast(this, "Check Network")
                        } else {

                            val groupName =
                                viewFinishDialog.dialogEditFinishAddToGroup.text.toString()
                            if (groupName.length < 5) {

                                showToast(this, "write a name more than 5 letters")
                            } else {

                                dialog.dismiss()
                                firebaseDatabase = FirebaseDatabase.getInstance()

                                val progressFinishDialog = initProgress(this@AddToGroupActivity)
                                progressFinishDialog.show()


                                val dbRefAddGroupToFirebase =
                                    firebaseDatabase.getReference(uid).child(MyGroups)
                                        .child(groupName)

                                val size = arrayOfStudent.size
                                dbRefAddGroupToFirebase.setValue(
                                    com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArrayWithSizeAndKey(
                                        arrayOfStudent,
                                        size.toString(),
                                        groupName
                                    )
                                )
                                    .addOnCompleteListener {


                                        if (it.isSuccessful) {


                                            progressFinishDialog.dismiss()
                                            showToast(this, "You Added $groupName successfully")

                                            val iToMainActivity = Intent(
                                                this,
                                                com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.GroupsActivity::class.java
                                            )
                                            iToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(iToMainActivity)

                                        } else {


                                            showToast(this, it.exception!!.message.toString())

                                        }


                                    }

                            }

                        }


                    }
                }
                return true
            }

            else->return false
        }

    }

}
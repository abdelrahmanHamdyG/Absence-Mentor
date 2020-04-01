package com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.GridViewAdapterGroupsActivity
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.Infromations
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.MyGroups
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.NumberOfGroups
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.initProgress
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.shared
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.sharedId
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.showToast
import com.abdelrahmanhamdy2.absencementor2.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_groups.*
import kotlinx.android.synthetic.main.dialog_show_id.view.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class GroupsActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {



    var firebaseAuth= FirebaseAuth.getInstance()
    var fireBaseDatabase=FirebaseDatabase.getInstance()
    var arrayHelper=ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArrayWithSizeAndKey>()
    lateinit var progressDowmloadGroups:ProgressDialog
    lateinit var edit:SharedPreferences
    lateinit var uid:String
    lateinit var dialog:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)





        if (firebaseAuth.currentUser == null) {

            startActivity(Intent(this,
                com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AuthActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))


        } else {

            uid=firebaseAuth.currentUser!!.uid
            edit=getSharedPreferences(shared, Context.MODE_PRIVATE)
            val dataedit=edit.getString(uid+sharedId,"No")

            if (dataedit=="No"||dataedit==null){

                ShowId()

            }



            progressDowmloadGroups= initProgress(this)
            setSupportActionBar(toolbarGroupsActivity)
            supportActionBar!!.title = "Your Groups"
            floatingActionButtonGroupsActivity.setOnClickListener {

                var iToAddToGroupActivity = Intent(
                    this@GroupsActivity,
                    com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AddToGroupActivity::class.java
                )
                startActivity(iToAddToGroupActivity)

            }


            val actionBarToggle = ActionBarDrawerToggle(
                this, drawerLayoutGroupsActivity, toolbarGroupsActivity,
                R.string.bottom_sheet_behavior,
                R.string.bottom_sheet_behavior
            )
            actionBarToggle.syncState()
            drawerLayoutGroupsActivity.addDrawerListener(actionBarToggle)
            navigationViewGroupsActivity.setNavigationItemSelectedListener(this)

            progressDowmloadGroups!!.show()

            CoroutineScope(IO).launch {

                   downloadGroupsFromFirebase(uid)
            }


        }
    }

        override fun onStart() {
            super.onStart()


        }


        suspend fun downloadGroupsFromFirebase(uid:String){

        fireBaseDatabase= FirebaseDatabase.getInstance()
        var dbRefGetGroups=fireBaseDatabase.getReference(uid).child(MyGroups)
        dbRefGetGroups.addListenerForSingleValueEvent(object :ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {

                arrayHelper.clear()
                if (p0.value == null) {

                    CoroutineScope(Main).launch {
                        progressDowmloadGroups.dismiss()
                        textAddGroupIfNot.visibility = View.VISIBLE
                    }
                } else {
                    for (i in p0.children){

                        arrayHelper.add(i.getValue(com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArrayWithSizeAndKey::class.java)!!)

                    }



                    CoroutineScope(Main).launch {

                        textAddGroupIfNot.visibility = View.GONE
                        setAdapterAndDismissDialog(uid,arrayHelper)

                    }
                }
            }
        })


    }




    suspend fun setAdapterAndDismissDialog(uid:String,arrayOfGrid:ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArrayWithSizeAndKey>) {

        fireBaseDatabase= FirebaseDatabase.getInstance()
        var dbRefSetNumberOfGroups=fireBaseDatabase.getReference(uid).child(Infromations).child(NumberOfGroups)
        dbRefSetNumberOfGroups.setValue(arrayOfGrid.size)
        progressDowmloadGroups.dismiss()

        var adapterGrid=
            GridViewAdapterGroupsActivity(
                this@GroupsActivity,
                arrayOfGrid, uid, GridViewGroupsActivity
            )
        GridViewGroupsActivity.layoutManager=GridLayoutManager(this@GroupsActivity,2, RecyclerView.VERTICAL,false)
        GridViewGroupsActivity.adapter=adapterGrid

    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.navigation_history ->{
                drawerLayoutGroupsActivity.closeDrawers()
                startActivity(Intent(this@GroupsActivity, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.HistoryActivity::class.java));return true}
            R.id.navigation_add_groups -> {
                drawerLayoutGroupsActivity.closeDrawers()
                startActivity(Intent(this@GroupsActivity,
                com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AddToGroupActivity::class.java));return true}
            R.id.navigation_profile ->{
            drawerLayoutGroupsActivity.closeDrawers()
            startActivity(Intent(this@GroupsActivity, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.ProfileActivity::class.java));return true}

            R.id.ShareButton-> {
                drawerLayoutGroupsActivity.closeDrawers()

                showToast(this,"We are working On It")
                return true
            }

            R.id.Communicate->{

                drawerLayoutGroupsActivity.closeDrawers()
                var iCommunicate=Intent(Intent.ACTION_VIEW)
                iCommunicate.data = Uri.parse("mailto:?subject=StudentsApp &to=dhamdy078@gmail.com")
                startActivity(iCommunicate)
                return true
            }

            R.id.signOut->{
                drawerLayoutGroupsActivity.closeDrawers()
                firebaseAuth.signOut()
                var i=Intent(this,
                    com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AuthActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)

                return true
            }

            else->return false
        }
    }

    fun ShowId(){

        var dbId=fireBaseDatabase.getReference(uid).child(Infromations)
        dbId.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                dialog.dismiss()

            }

            override fun onDataChange(p0: DataSnapshot) {
                var dialog=Dialog(this@GroupsActivity)
                val data=p0.getValue(com.abdelrahmanhamdy2.absencementor2.Models.ModelInformations::class.java)
                val viewID=LayoutInflater.from(this@GroupsActivity).inflate(R.layout.dialog_show_id,null)
                dialog.setContentView(viewID)
                dialog.setCancelable(false)
                dialog.show()
                viewID.dialogShowIdText.text="Your Id is  ${data!!.id}  give this ID to your students to be Entered in the students app"
                viewID.buttonId.setOnClickListener {
                    dialog.dismiss()
                    var putData=getSharedPreferences(shared, Context.MODE_PRIVATE).edit()
                    putData.putString(uid+sharedId,"someThing")
                    putData.apply()

                }

            }


        })

    }



}

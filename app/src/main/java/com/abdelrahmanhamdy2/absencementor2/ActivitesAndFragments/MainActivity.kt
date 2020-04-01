package com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments

import  android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.GroupsNow
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.TheFather
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.checkNetwork
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.intentNameGroupExtra
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.showToast
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.RecyclerAdapterMainActivity
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.ViewModelMainActivity
import com.abdelrahmanhamdy2.absencementor2.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_to_room_main_activity.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        /////////////////////////////////

        when(item.itemId){

            R.id.navigation_groups->{
                drawerMain.closeDrawers()
                startActivity(Intent(this,
                    com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.GroupsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                return true
            }

            /////////////////////////////////
            /////////////////////////////////

            R.id.navigation_history ->{
                drawerMain.closeDrawers()
                startActivity(Intent(this, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.HistoryActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK));return true

            }

            /////////////////////////////////
            /////////////////////////////////

            R.id.navigation_add_groups -> {
                drawerMain.closeDrawers()
                startActivity(Intent(this,
                    com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AddToGroupActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK));return true

            }

            /////////////////////////////////
            /////////////////////////////////

            R.id.navigation_profile ->{
                drawerMain.closeDrawers()
                startActivity(Intent(this, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.ProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK));return true

            }


            /////////////////////////////////
            ////////////////    /////////////////

            R.id.ShareButton-> {
                drawerMain.closeDrawers()
                showToast(this,"We are working On It")
                return true
            }

            /////////////////////////////////
            /////////////////////////////////

            R.id.Communicate->{

                drawerMain.closeDrawers()
                val iCommunicate=Intent(Intent.ACTION_VIEW)
                iCommunicate.data = Uri.parse("mailto:?subject=StudentsApp &to=dhamdy078@gmail.com")
                startActivity(iCommunicate)
                return true
            }

            /////////////////////////////////
            /////////////////////////////////

            R.id.signOut->{
                drawerMain.closeDrawers()
                firebaseAuth.signOut()
                val i=Intent(this, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AuthActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                return true
            }

            /////////////////////////////////
            /////////////////////////////////

            else->{
                drawerMain.closeDrawers()

                return false
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private var firebaseAuth= FirebaseAuth.getInstance()
    private var uid=firebaseAuth.currentUser!!.uid

    private lateinit var animBounce1: Animation
    private lateinit var animBounce2: Animation
    private lateinit var viewModelMainActivity: ViewModelMainActivity
    private lateinit var iDataFromGrid: String
    private lateinit var absentOrPresentsHelperMain: String
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var dbRefGetDataFromFatherToRecycler: DatabaseReference
    private var runIt: Boolean = false
    private var fromStartSection=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        setSupportActionBar(toolbarMain)


        val actionBarToggle= ActionBarDrawerToggle(this,drawerMain,toolbarMain, R.string.bottom_sheet_behavior, R.string.bottom_sheet_behavior)
        actionBarToggle.syncState()
        drawerMain.addDrawerListener(actionBarToggle)
        navigationMain.setNavigationItemSelectedListener(this)


        animBounce1 = AnimationUtils.loadAnimation(this@MainActivity,
            R.anim.anim_start_section
        )
        animBounce2 = AnimationUtils.loadAnimation(this@MainActivity,
            R.anim.anim_end_section
        )


        iDataFromGrid = intent.getStringExtra(  intentNameGroupExtra)!!



    }


    override fun onStart() {
        super.onStart()





        viewModelMainActivity = ViewModelProviders.of(this@MainActivity).get(ViewModelMainActivity::class.java)
        viewModelMainActivity.checkStartOrEnd(uid, iDataFromGrid)
        dbRefGetDataFromFatherToRecycler = firebaseDatabase.getReference(uid).child(GroupsNow).child(iDataFromGrid).child(TheFather)


        viewModelMainActivity.mutableStartOrEnd.observe(this, Observer {

            if (it) {

                startSectionMainActivity.visibility = View.GONE
                endSectionMainActivity.visibility = View.VISIBLE
                recyclerMainActivity.visibility = View.VISIBLE
                absentOrPresentsHelperMain = "absents"
                if (runIt && !fromStartSection){

                }else {

                    runIt = true
                    CoroutineScope(IO).launch {


                        dbRefGetDataFromFatherToRecycler.addValueEventListener(
                            ValueEventListenerMainActivity()
                        )

                    }
                }

            } else {


                supportActionBar!!.title = "Section"
                startSectionMainActivity.visibility = View.VISIBLE
                endSectionMainActivity.visibility = View.GONE
                recyclerMainActivity.visibility = View.GONE

            }


        })



        viewModelMainActivity.absentsOrPresentsMainActivity.observe(this, Observer {

            CoroutineScope(IO).launch {
                viewModelMainActivity.getSpecficData(uid, iDataFromGrid, it)
            }

            absentOrPresentsHelperMain = it


        })



        viewModelMainActivity.numberOfStudent.observe(this, Observer {


            supportActionBar!!.title = "$absentOrPresentsHelperMain=$it"

        })

        viewModelMainActivity.listOfStudent.observe(this, Observer {


            val adapterRecMain =
                RecyclerAdapterMainActivity(
                    this@MainActivity,
                    it
                )
            recyclerMainActivity.visibility = View.VISIBLE
            recyclerMainActivity.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            recyclerMainActivity.adapter = adapterRecMain



        })




        startSectionMainActivity.setOnClickListener {

            ///////////////////////////////////////////

            runIt=false
            fromStartSection=true

            CoroutineScope(IO).launch {

                viewModelMainActivity.copyDataFromFirebase(uid, iDataFromGrid)
                runIt=true
                viewModelMainActivity.setStartOrEndData(uid,iDataFromGrid,true)
            }

            endSectionMainActivity.startAnimation(animBounce1)
            it.startAnimation(animBounce2)



        }

        endSectionMainActivity.setOnClickListener {


            if (checkNetwork(this)){

                val dialogToRoom= Dialog(this)
                dialogToRoom.create()
                val viewToRoom = LayoutInflater.from(this).inflate(R.layout.dialog_to_room_main_activity, null)
                dialogToRoom.setContentView(viewToRoom)
                dialogToRoom.setTitle("Enter Section name")
                dialogToRoom.show()




                viewToRoom.dialogYesButtonToRoomMainActivity.setOnClickListener {



                    val sectionName=viewToRoom.dialogEditTextNameSectionToRoomMainActivity.text.toString()

                    dialogToRoom.dismiss()
                    runIt = false

                    viewModelMainActivity.setStartOrEndData(uid, iDataFromGrid, false)

                    CoroutineScope(IO).launch {
                        viewModelMainActivity.
                            deleteDataAndCopyItRoom(
                            uid,
                            iDataFromGrid,
                            this@MainActivity,
                            sectionName
                        )

                    }

                    it.startAnimation(animBounce2)
                    startSectionMainActivity.startAnimation(animBounce1)

                }

               viewToRoom.dialogNoButtonToRoomMainActivity.setOnClickListener {

                   dialogToRoom.dismiss()

               }


            }else{

                showToast(this,"check Your Network")
            }

        }



    }

    override fun onPause() {
        super.onPause()

        runIt=false
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.absOrPreMenuMainActivity -> {

                if (startSectionMainActivity.visibility == View.VISIBLE) {

                    showToast(this@MainActivity, "Start Section First ")
                    return true
                } else {

                    if (viewModelMainActivity.absentsOrPresentsMainActivity.value == null) {

                        viewModelMainActivity.changeAbsentsOrPresents("absents")
                    } else {


                        viewModelMainActivity.changeAbsentsOrPresents(viewModelMainActivity.absentsOrPresentsMainActivity.value!!)
                    }

                }
                return true
            }

            R.id.addStudentMenuMainActivity -> {
                if (startSectionMainActivity.visibility == View.VISIBLE) {
                    showToast(this@MainActivity, "start SectionFirst")
                }else {
                    val iToAddStudentByTeacherActivity =
                        Intent(this@MainActivity, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AddStudentByTeacherActivity::class.java)
                    iToAddStudentByTeacherActivity.putExtra(intentNameGroupExtra, iDataFromGrid)
                    startActivity(iToAddStudentByTeacherActivity)
                }
                return true

            }
            R.id.cancelMenuMain -> {
                if (startSectionMainActivity.visibility == View.VISIBLE) {
                    showToast(this@MainActivity, "start SectionFirst")
                } else {
                    if (!checkNetwork(this)){


                        showToast(this,"No Network")
                    }else{


                    val h=Handler()
                    h.postDelayed({

                        runIt=false
                        val dbRefDeleteData=firebaseDatabase.getReference(uid).child(GroupsNow).child(iDataFromGrid)
                        dbRefDeleteData.removeValue().addOnCompleteListener {

                            if (it.isSuccessful){

                                viewModelMainActivity.setStartOrEndData(uid, iDataFromGrid, false)

                            }else{

                                showToast(this, it.exception!!.message.toString())
                            }
                        }

                    },200)
                }
                }
                return true
            }

            else -> return false
        }
        }



    inner class ValueEventListenerMainActivity : ValueEventListener {

        override fun onCancelled(p0: DatabaseError) {

            showToast(this@MainActivity, p0.message)
        }

        override fun onDataChange(p0: DataSnapshot) {

            if(!runIt){

                dbRefGetDataFromFatherToRecycler.removeEventListener(this)
            }else{
            CoroutineScope(IO).launch {
                viewModelMainActivity.getSpecficData(
                    uid,
                    iDataFromGrid,
                    absentOrPresentsHelperMain
                   )


                }

            }
        }
    }
}




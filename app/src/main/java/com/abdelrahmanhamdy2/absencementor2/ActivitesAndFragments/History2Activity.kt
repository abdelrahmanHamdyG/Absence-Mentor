package com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahmanhamdy2.absencementor2.Room.DatabaseRoom
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.intentNameGroupExtra
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.intentNameGroupExtraGroup
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.RecyclerAdapterHistory2
import com.abdelrahmanhamdy2.absencementor2.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_history2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class History2Activity : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener{

    private var firebaseAuth=FirebaseAuth.getInstance()
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.navigation_groups ->{
                drawerHistory2.closeDrawers()
                startActivity(Intent(this,
                    com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.GroupsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                return true
            }
            R.id.navigation_history ->{
                drawerHistory2.closeDrawers()
                startActivity(Intent(this, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.HistoryActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK));return true}
            R.id.navigation_add_groups -> {
                drawerHistory2.closeDrawers()
                startActivity(Intent(this,
                    com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AddToGroupActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK));return true}
            R.id.navigation_profile ->{
                drawerHistory2.closeDrawers()
                startActivity(Intent(this, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.ProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK));return true}


            R.id.ShareButton -> {
                drawerHistory2.closeDrawers()
                Helper.showToast(this, "We are working On It")

                return true
            }

            R.id.Communicate ->{

                drawerHistory2.closeDrawers()

                val iCommunicate=Intent(Intent.ACTION_VIEW)
                iCommunicate.setData(Uri.parse("mailto:?subject=StudentsApp &to=dhamdy078@gmail.com"))
                startActivity(iCommunicate)
                return true
            }

            R.id.signOut ->{

                drawerHistory2.closeDrawers()
                firebaseAuth.signOut()
                val i=Intent(this,
                    com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AuthActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                return true
            }
            else->{
                drawerHistory2.closeDrawers()

                return false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history2)






        setSupportActionBar(toolbarHistory2)

        val actionBarToggle= ActionBarDrawerToggle(this,drawerHistory2,toolbarHistory2,
            R.string.bottom_sheet_behavior,
            R.string.bottom_sheet_behavior
        )
        actionBarToggle.syncState()
        drawerHistory2.addDrawerListener(actionBarToggle)
        navigationHistory2.setNavigationItemSelectedListener(this)
        


        val gson=Gson()

        val dao= DatabaseRoom.getDataBase(this).returnDao()
        val iDataGridLayout=intent.extras!!.getString(intentNameGroupExtra)
        val iDataGridLayoutGroup=intent.extras!!.getString(intentNameGroupExtraGroup)

        supportActionBar!!.title="Group: $iDataGridLayoutGroup  Section: $iDataGridLayout "
        CoroutineScope(IO).launch{

            val data=dao.readOneData(iDataGridLayout!!)
            val arrayAbesnts=gson.fromJson(data.listAbsents, Array<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArray>::class.java)
            val arrayPresents=gson.fromJson(data.listPresents,Array<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArray>::class.java)
            setAdapters(arrayAbesnts,arrayPresents)
            
        }

    }


    private fun setAdapters(arrayAbsents:Array<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArray>, arrayPresents:Array<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArray>) {

        CoroutineScope(Main).launch {

            toolbarAbsentHistory2.title="absents= ${arrayAbsents.size}"
            toolbarPresentsHistory2.title="presents= ${arrayPresents.size}"
            absentsRecyclerHistory2.layoutManager =
                LinearLayoutManager(this@History2Activity, RecyclerView.VERTICAL, false)
            absentsRecyclerHistory2.adapter =
                RecyclerAdapterHistory2(this@History2Activity, arrayAbsents)
            presentsRecyclerHistory2.layoutManager =
                LinearLayoutManager(this@History2Activity, RecyclerView.VERTICAL, false)
            presentsRecyclerHistory2.adapter =
                RecyclerAdapterHistory2(this@History2Activity, arrayPresents)



        }
    }



}

package com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.abdelrahmanhamdy2.absencementor2.R
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_of_fragments.*

class ActivityOfFragments : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,NavigationView.OnNavigationItemSelectedListener{


    lateinit var support: FragmentManager
    var firebaseAuth = FirebaseAuth.getInstance()
    var weAreIn:String="Groups"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_of_fragments)




        bottomNavigationView.setOnNavigationItemSelectedListener(this)



    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser == null) {


            startActivity(
                Intent(this, AuthActivity::class.java).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                            or Intent.FLAG_ACTIVITY_NEW_TASK
                )
            )

        } else {

            support = supportFragmentManager
            var name=intent.extras?.getString("name")
            if (name=="History"){

                support.beginTransaction().replace(R.id.layoutOfFragments, HistoryFragment()).commit()
                bottomNavigationView.selectedItemId = R.id.bottomHistory
            }else{
                if (name=="Profile"){

                    support.beginTransaction().replace(R.id.layoutOfFragments, ProfileFragment()).commit()
                    bottomNavigationView.selectedItemId = R.id.bottomProfile
                }else {

                    support.beginTransaction().replace(R.id.layoutOfFragments, GroupsFragment())
                        .commit()
                    bottomNavigationView.selectedItemId = R.id.bottomGroups
                }
            }

            setSupportActionBar(toolbarOfFragments)

            val actionBarToggle= ActionBarDrawerToggle(this,drawerFragments,toolbarOfFragments, R.string.bottom_sheet_behavior, R.string.bottom_sheet_behavior)
            actionBarToggle.syncState()
            drawerFragments.addDrawerListener(actionBarToggle)
            navigationFragments.setNavigationItemSelectedListener(this)


        }
    }

    private fun setFragment(fragment: Fragment) {

        support.beginTransaction().replace(R.id.layoutOfFragments, fragment).commit()



    }
    
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.bottomHistory -> {

                if (weAreIn=="History"){

                }else {
                    weAreIn="History"
                    setFragment(HistoryFragment())
                }
                return true
            }
            R.id.bottomGroups -> {
                if (weAreIn=="Groups"){


                }else {
                    weAreIn="Groups"
                    setFragment(GroupsFragment())
                }
                return true
            }
            R.id.bottomProfile -> {

                if (weAreIn=="Profile") {


                }else{
                    weAreIn="Profile"
                    setFragment(ProfileFragment())

                }
                return true

            }
            R.id.navigation_groups-> {
                drawerFragments.closeDrawers()

                if (weAreIn == "Groups") {


                } else{
                    weAreIn="Groups"
                    bottomNavigationView.selectedItemId=R.id.bottomGroups
                    setFragment(GroupsFragment())
                }
                return true
            }

            /////////////////////////////////
            /////////////////////////////////

            R.id.navigation_history ->{

                drawerFragments.closeDrawers()
                if (weAreIn=="History"){



                }else {
                    weAreIn="History"

                    bottomNavigationView.selectedItemId=R.id.bottomHistory

                    setFragment(HistoryFragment())
                }
                return true
            }

            /////////////////////////////////
            /////////////////////////////////

            R.id.navigation_add_groups -> {

                drawerFragments.closeDrawers()
                startActivity(Intent(this,AddToGroupActivity::class.java));return true

            }

            /////////////////////////////////
            /////////////////////////////////

            R.id.navigation_profile ->{
                drawerFragments.closeDrawers()

                if (weAreIn=="Profile"){



                }else{

                    weAreIn="Profile"

                    bottomNavigationView.selectedItemId=R.id.bottomProfile
                    setFragment(ProfileFragment())
                }




                return true

            }


            /////////////////////////////////
            ////////////////    /////////////////

            R.id.ShareButton-> {
                drawerFragments.closeDrawers()
                Helper.showToast(this, "We are working On It")
                return true
            }

            /////////////////////////////////
            /////////////////////////////////

            R.id.Communicate->{

                drawerFragments.closeDrawers()
                val iCommunicate=Intent(Intent.ACTION_VIEW)
                iCommunicate.data = Uri.parse("mailto:?subject=StudentsApp &to=dhamdy078@gmail.com")
                startActivity(iCommunicate)
                return true
            }

            /////////////////////////////////
            /////////////////////////////////
            
            R.id.signOut->{
                drawerFragments.closeDrawers()
                firebaseAuth.signOut()
                val i=Intent(this, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AuthActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                return true
            }
            
            
            else->{return false}

        }
    }


    override fun onBackPressed() {

            if (weAreIn=="Groups"){


                super.onBackPressed()
            }else{
                bottomNavigationView.selectedItemId=R.id.bottomGroups
                setFragment(GroupsFragment())
                weAreIn="Groups"
            }

    }
}
package com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahmanhamdy2.absencementor2.Room.DAO
import com.abdelrahmanhamdy2.absencementor2.Room.DatabaseRoom
import com.abdelrahmanhamdy2.absencementor2.Room.ModelDataRoom
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.RecyclerAdapterHistory
import com.abdelrahmanhamdy2.absencementor2.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HistoryActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener,SearchView.OnQueryTextListener {

    private var firebaseAuth=FirebaseAuth.getInstance()
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navigation_groups ->{
                drawerLayoutHistoryActivity.closeDrawers()
                startActivity(Intent(this@HistoryActivity, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.GroupsActivity::class.java))
                return true
            }
            R.id.navigation_add_groups ->{
                drawerLayoutHistoryActivity.closeDrawers()
                startActivity(Intent(this@HistoryActivity,
                com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AddToGroupActivity::class.java));return true}
            R.id.navigation_profile ->{

                drawerLayoutHistoryActivity.closeDrawers()
                startActivity(Intent(this@HistoryActivity,
                    com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.ProfileActivity::class.java));return true}

            R.id.ShareButton -> {
                drawerLayoutHistoryActivity.closeDrawers()
                Helper.showToast(this, "We are working On It")

                return true
            }

            R.id.Communicate ->{
                drawerLayoutHistoryActivity.closeDrawers()
                val iCommunicate=Intent(Intent.ACTION_VIEW)
                iCommunicate.data = Uri.parse("mailto:?subject=StudentsApp &to=dhamdy078@gmail.com")
                startActivity(iCommunicate)
                return true
            }

            R.id.signOut ->{
                drawerLayoutHistoryActivity.closeDrawers()
                firebaseAuth.signOut()
                val i=Intent(this,
                    com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AuthActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                return true
            }
            else-> {
                drawerLayoutHistoryActivity.closeDrawers()

                return false
            }
        }
    }


    lateinit var dao: DAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)





        setSupportActionBar(toolbarHistory)
        supportActionBar!!.title="History"

        val actionBarToggle=
            ActionBarDrawerToggle(this,drawerLayoutHistoryActivity,toolbarHistory,
                R.string.bottom_sheet_behavior,
                R.string.bottom_sheet_behavior
            )
        actionBarToggle.syncState()
        drawerLayoutHistoryActivity.addDrawerListener(actionBarToggle)
        navigationHistoryActivity.setNavigationItemSelectedListener(this)
        
        
        dao= DatabaseRoom.getDataBase(this).returnDao()
        var arrayList=ArrayList<ModelDataRoom>()
        CoroutineScope(IO).launch {
            arrayList = dao.readData() as ArrayList<ModelDataRoom>
            showAdapterHistory(arrayList)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        menuInflater.inflate(R.menu.menu_search_view_history,menu)
        val item=menu!!.findItem(R.id.search_history_menu)
        if (item!=null){

            var searchView=item.actionView as SearchView
            searchView.setOnQueryTextListener(this)

        }

        return true
    }

    private suspend fun showAdapterHistory(arrayList:ArrayList<ModelDataRoom>){
        withContext(Main){
            if (arrayList.size==0){
                NoHistory.visibility= View.VISIBLE

            }else{
        recyclerHistoryActivity.layoutManager=LinearLayoutManager(this@HistoryActivity,RecyclerView.VERTICAL,false)
        recyclerHistoryActivity.adapter= RecyclerAdapterHistory(this@HistoryActivity,arrayList)
    }
        }
}





    override fun onQueryTextSubmit(query: String?): Boolean {

        CoroutineScope(IO).launch {
            val data=dao.readOneDataToArray(query!!)
            CoroutineScope(Main).launch {
                recyclerHistoryActivity.adapter =
                    RecyclerAdapterHistory(this@HistoryActivity, data as ArrayList<ModelDataRoom>)
            }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        if (newText!!.length<=1){

            CoroutineScope(IO).launch {

                val ar = dao.readData()

                CoroutineScope(Main).launch {
                    recyclerHistoryActivity.adapter =
                        RecyclerAdapterHistory(this@HistoryActivity, ar as ArrayList <ModelDataRoom>)

                }
            }
        }

        return true
    }


}


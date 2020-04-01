package com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList


class Helper {


    companion object {


        private const val tag = "MyTag"
        const val booleanPath="booleanPath"
        const val MyGroups="MyGroups"
        const val intentNameGroupExtra="intentNameGroupExtra"
        const val intentNameGroupExtraGroup="intentNameGroupExtraGroup"
        const val GroupsNow="GroupsNow"
        const val UIDS="UIDS"
        const val shared="shared"
        const val sharedId="sharedId"
        const val NumberOfGroups="NumberOfGroups"
        const val Infromations="Infromations"
        const val TheFather="TheFather"
        const val StudentAbsentsPath="StudentAbsentsPath"
        const val StudentpresentsPath="StudentPresentsPath"


        fun checkNetwork(context: Context): Boolean {

            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(
                        ConnectivityManager
                            .TYPE_MOBILE
                    ).state == NetworkInfo.State
                .CONNECTED

        }



        fun showToast(context: Context, text: String) {

            Toast.makeText(context, text, Toast.LENGTH_LONG).show()

        }

        fun log(text: String) {

            Log.i(tag, text)
        }


        fun convertToGson(ar: ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase>): String {
            val gson = Gson()
            return gson.toJson(ar)

        }

        fun showRecycler(rec: RecyclerView, ar: ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArray>, ar2:ArrayList<Int>, context: Context) {




                val adapt =
                    AdapterAddToGroupActivity(
                        context,
                        ar,ar2,
                        rec
                    )
                if (ar.size==1) {
                    rec.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    rec.adapter = adapt
                } else {

                    rec.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    adapt.notifyDataSetChanged()
                }



        }



        fun initProgress(context: Context): ProgressDialog {

            val progressDialog=ProgressDialog(context)
            progressDialog.setTitle("Please Wait")
            progressDialog.setCancelable(false)

            return progressDialog

        }


        fun getTime():String{

            val calender = Calendar.getInstance()
            return DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calender.time)


        }

        fun getGroupsAfterDelete(grid:RecyclerView,uid:String,context: Context){

            val fireBaseDatabase=FirebaseDatabase.getInstance()

            val arrayAfterDelete=ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArrayWithSizeAndKey>()
            val dbRefGetData=fireBaseDatabase.getReference(uid).child(MyGroups)
            dbRefGetData.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {


                }

                override fun onDataChange(p0: DataSnapshot) {

                    arrayAfterDelete.clear()
                    for (i in p0.children){


                        val dataAfterDelete=i.getValue(com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArrayWithSizeAndKey::class.java)!!
                        arrayAfterDelete.add(dataAfterDelete)

                    }

                    val dbRefSetSize=fireBaseDatabase.getReference(uid).child(Infromations).child(
                        NumberOfGroups)
                    if (arrayAfterDelete.size==0){
                        dbRefSetSize.setValue(0)
                    }else {
                        val number = arrayAfterDelete.size
                        dbRefSetSize.setValue(number)
                    }
                    CoroutineScope(Main).launch {
                        grid.adapter= GridViewAdapterGroupsActivity(context, arrayAfterDelete, uid, grid)
                    }
                }


            })



        }



    }
}
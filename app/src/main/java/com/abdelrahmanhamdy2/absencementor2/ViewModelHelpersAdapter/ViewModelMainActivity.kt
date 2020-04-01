package com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.abdelrahmanhamdy2.absencementor2.Room.DatabaseRoom
import com.abdelrahmanhamdy2.absencementor2.Room.ModelDataRoom
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.GroupsNow
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.MyGroups
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.StudentAbsentsPath
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.StudentpresentsPath
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.TheFather
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.booleanPath
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.convertToGson
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.getTime
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ViewModelMainActivity(var app:Application):AndroidViewModel(app) {

    val firebaseDatabase =FirebaseDatabase.getInstance()
    var mutableStartOrEnd=MutableLiveData<Boolean>()
    var listOfStudent=MutableLiveData<ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase>>()
    var absentsOrPresentsMainActivity=MutableLiveData<String>()
    var numberOfStudent=MutableLiveData<Int>()



    fun checkStartOrEnd(uid:String,groupName:String){

        val dbRefCheckStartOrEnd=firebaseDatabase.getReference(uid).child(groupName+ booleanPath)
        dbRefCheckStartOrEnd.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.value==null){

                    mutableStartOrEnd.value=false

                }else{

                    mutableStartOrEnd.value=p0.getValue(Boolean::class.java)
                }


            }


        })


    }




        fun copyDataFromFirebase(uid:String,groupName: String){


        val dbRefCopyData=firebaseDatabase.getReference(uid).child(MyGroups).child(groupName).child("arrayOfStudent")

        dbRefCopyData.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                for (i in p0.children){

                    val data=i.getValue(com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArray::class.java)!!
                    val dbRefPastData=firebaseDatabase.getReference(uid).child(GroupsNow).child(groupName).child(TheFather).child(StudentAbsentsPath).push()
                    dbRefPastData.setValue(
                        com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase(
                            data.nameOfStudent!!,
                            data.idOfStudent!!,
                            dbRefPastData.key!!
                        )
                    )

                }

            }


        })

    }


    fun setStartOrEndData(uid:String,groupName: String,boolean: Boolean){
            val dbRefSetStartOrEnd = firebaseDatabase.getReference(uid).child(groupName + booleanPath)
            dbRefSetStartOrEnd.setValue(boolean)




        }


    fun getSpecficData(uid:String,groupName: String,absentOrPresents: String){

        var x=""
        x = if (absentOrPresents=="absents"){

            StudentAbsentsPath

        }else{

            StudentpresentsPath
        }

        val dbRefGetDataSpecific=firebaseDatabase.getReference(uid).child(GroupsNow).child(groupName).child(TheFather).child(x)

        dbRefGetDataSpecific.addListenerForSingleValueEvent(object :ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                var listOfStudentsHelper=ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase>()
                listOfStudentsHelper.clear()

                for (i in p0.children){

                    listOfStudentsHelper.add(i.getValue(com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase::class.java)!!)


                }
                listOfStudent.postValue(listOfStudentsHelper)
                numberOfStudent.postValue(listOfStudentsHelper.size)

            }


        })

    }


    fun deleteDataAndCopyItRoom(uid:String,groupName:String,context: Context,sectionName:String){

        val dao= DatabaseRoom.getDataBase(context).returnDao()
        val arrayAbsents=ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase>()
        val arrayPresents=ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase>()
        val dbRefDeleteData=firebaseDatabase.getReference(uid).child(GroupsNow).child(groupName)
        val dbRefCopyDataAbsents=firebaseDatabase.getReference(uid).child(GroupsNow).child(groupName).child(TheFather).child(StudentAbsentsPath)
        val dbRefCopyDataPresents=firebaseDatabase.getReference(uid).child(GroupsNow).child(groupName).child(TheFather).child(StudentpresentsPath)

        dbRefCopyDataAbsents.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                arrayAbsents.clear()
                arrayPresents.clear()
                for(i in p0.children){
                    
                    arrayAbsents.add(i.getValue(com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase::class.java)!!)

                }

                dbRefCopyDataPresents.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }


                    override fun onDataChange(p0: DataSnapshot) {


                        for (i2 in p0.children){
                            arrayPresents.add(i2.getValue(com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase::class.java)!!)

                        }

                CoroutineScope(IO).launch {
                    dao.insertData(
                        ModelDataRoom(
                            null,
                            getTime(),
                            sectionName,
                            groupName,
                            convertToGson(arrayAbsents),
                            convertToGson(arrayPresents)
                        )
                    )
                    dbRefDeleteData.removeValue()
                }
                    }


                })



            }


        })



    }





    fun changeAbsentsOrPresents(type:String){

        if (type=="absents"){

            absentsOrPresentsMainActivity.value="presents"

        }else{

            absentsOrPresentsMainActivity.value="absents"
        }



    }




}
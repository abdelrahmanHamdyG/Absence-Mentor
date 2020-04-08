package com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments



import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArrayWithSizeAndKey
import com.abdelrahmanhamdy2.absencementor2.R
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.GridViewAdapterGroupsActivity
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.initProgress
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_groups.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.zip.Inflater

/**
 * A simple [Fragment] subclass.
 */


class GroupsFragment : Fragment() {



    var fireBaseDatabase = FirebaseDatabase.getInstance()
    var arrayHelper = ArrayList<ModelDataOfArrayWithSizeAndKey>()
    lateinit var progressDownloadGroups: ProgressDialog
    var firebaseAuth = FirebaseAuth.getInstance()
    lateinit var floating:FloatingActionButton
    var uid = firebaseAuth.currentUser!!.uid
    lateinit var textGroup: TextView
    lateinit var recyclerView: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.activity_groups, container, false)

        recyclerView = view.findViewById(R.id.GridViewGroupsActivity) as RecyclerView
        textGroup = view.findViewById(R.id.textAddGroupIfNot) as TextView
        floating = view.findViewById(R.id.floatingActionButtonGroupsActivity)



        var toolbar = activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarOfFragments)
        toolbar.title = "Your Groups"

        progressDownloadGroups = initProgress(requireContext())
        progressDownloadGroups.show()

        CoroutineScope(Dispatchers.IO).launch {

            downloadGroupsFromFirebase(uid,requireContext())
        }



        if (floating==null){

            floatingActionButtonGroupsActivity.setOnClickListener {


                var iToAddToGroupActivity = Intent(
                    requireContext(),
                    AddToGroupActivity::class.java
                )
                startActivity(iToAddToGroupActivity)


            }

        }else{
            floating.setOnClickListener {


                var iToAddToGroupActivity = Intent(
                    requireContext(),
                    com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.AddToGroupActivity::class.java
                )
                startActivity(iToAddToGroupActivity)

            }
        }



        return view
    }


    suspend fun downloadGroupsFromFirebase(uid: String,context: Context) {

        fireBaseDatabase=FirebaseDatabase.getInstance()
        val dbRefGetGroups = fireBaseDatabase.getReference(uid).child(Helper.MyGroups)
        dbRefGetGroups.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {

                arrayHelper.clear()
                if (p0.value == null) {

                    CoroutineScope(Dispatchers.Main).launch {
                        progressDownloadGroups.dismiss()
                        textGroup.visibility = View.VISIBLE
                    }
                } else {
                    for (i in p0.children) {

                        arrayHelper.add(i.getValue(com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArrayWithSizeAndKey::class.java)!!)

                    }



                    CoroutineScope(Dispatchers.Main).launch {

                        textGroup.visibility = View.GONE
                        setAdapterAndDismissDialog(uid, arrayHelper,context)

                    }
                }
            }
        })


    }

    suspend fun setAdapterAndDismissDialog(
        uid: String,
        arrayOfGrid: ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArrayWithSizeAndKey>,context: Context
    ) {

        fireBaseDatabase = FirebaseDatabase.getInstance()
        var dbRefSetNumberOfGroups =
            fireBaseDatabase.getReference(uid).child(Helper.Infromations).child(
                Helper.NumberOfGroups)
        dbRefSetNumberOfGroups.setValue(arrayOfGrid.size)
        progressDownloadGroups.dismiss()

        var adapterGrid =
            GridViewAdapterGroupsActivity(
                context,
                arrayOfGrid, uid, recyclerView
            )
        recyclerView.layoutManager =
            GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapterGrid

    }
}

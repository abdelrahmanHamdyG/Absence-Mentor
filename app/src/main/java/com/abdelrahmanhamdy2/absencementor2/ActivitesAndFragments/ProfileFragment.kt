package com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.abdelrahmanhamdy2.absencementor2.R
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {


    private var firebaseAuth = FirebaseAuth.getInstance()
    private var uid = firebaseAuth.currentUser!!.uid
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var textNumber:TextView
    private lateinit var textName:TextView
    private lateinit var textId:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        var toolbar=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarOfFragments)
        toolbar.title="Your Profile "


        var view=inflater.inflate(R.layout.activity_profile, container, false)

        textId=view.findViewById(R.id.textID2ProfileActivity) as TextView
        textName=view.findViewById(R.id.textName2ProfileActivity) as TextView
        textNumber=view.findViewById(R.id.textGroups2ProfileActivity) as TextView

        val dbRefGetNumberOfGroups=firebaseDatabase.getReference(uid).child(Helper.Infromations).child(
            Helper.NumberOfGroups
        )
        dbRefGetNumberOfGroups.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.value==null){

                    textNumber.text="0"
                }else {
                    textNumber.text = p0.getValue(Int::class.java).toString()
                }
            }

        })


        val dbRefGetInfromations=firebaseDatabase.getReference(uid).child(Helper.Infromations)
        dbRefGetInfromations.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Helper.showToast(requireContext(), p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val data=p0.getValue(com.abdelrahmanhamdy2.absencementor2.Models.ModelInformations::class.java)
                textName.text=data!!.name
                textId.text=data.id

            }


        })


        return view
    }


    }


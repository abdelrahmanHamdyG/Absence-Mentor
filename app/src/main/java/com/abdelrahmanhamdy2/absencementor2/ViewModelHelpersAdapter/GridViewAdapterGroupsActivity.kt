package com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.abdelrahmanhamdy2.absencementor2.R
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.MyGroups
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.getGroupsAfterDelete
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.intentNameGroupExtra
import kotlinx.android.synthetic.main.dialog_grid_delete.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class GridViewAdapterGroupsActivity(
    var context: Context,
    var array: ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArrayWithSizeAndKey>,
    var uid: String,
    var grid2: RecyclerView
) :RecyclerView.Adapter<GridViewAdapterGroupsActivity.viewHolderGridGroupsActivity>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderGridGroupsActivity {


        val view= LayoutInflater.from(context).inflate(R.layout.grid_layout_groups_activity, parent,false)
        return viewHolderGridGroupsActivity(view)

    }

    override fun getItemCount(): Int {

        return array.size
    }

    override fun onBindViewHolder(holder: viewHolderGridGroupsActivity, position: Int) {

        val postionGrid=array[position]
        holder.text_name.text=postionGrid.key
        holder.text_size.text=postionGrid.sizeOfArray

        holder.itemView.setOnClickListener {

            val i= Intent(context,
                com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.MainActivity::class.java)
            i.putExtra(intentNameGroupExtra,holder.text_name.text.toString())
            context.startActivity(i)

        }

        holder.button.setOnClickListener {

            val firebase=FirebaseDatabase.getInstance()

            val dialog = Dialog(context)

            val view=LayoutInflater.from(context).inflate(R.layout.dialog_grid_delete,null)
            dialog.setContentView(view)
            dialog.show()

            view.dialogYesButtonGrid.setOnClickListener {

                dialog.dismiss()
                val dbRefRemoveData=firebase.getReference(uid).child(MyGroups).child(holder.text_name.text.toString())
                dbRefRemoveData.removeValue()
                CoroutineScope(IO).launch {
                    getGroupsAfterDelete(grid2,uid,context)

                }

            }

            view.dialogNoButtonToGrid.setOnClickListener {

                dialog.dismiss()

            }




        }



    }



    inner class viewHolderGridGroupsActivity(view: View):RecyclerView.ViewHolder(view){

        var text_name= view.findViewById<TextView>(R.id.name_grid_groups_activity)
        var text_size= view.findViewById<TextView>(R.id.size_grid_groups_activity)
        var button=view.findViewById<ImageButton>(R.id.deleteGridView)

    }

}

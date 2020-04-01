package com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahmanhamdy2.absencementor2.Room.DatabaseRoom
import com.abdelrahmanhamdy2.absencementor2.Room.ModelDataRoom
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.intentNameGroupExtra
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.intentNameGroupExtraGroup
import com.abdelrahmanhamdy2.absencementor2.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


class RecyclerAdapterHistory(var context: Context,var array: ArrayList<ModelDataRoom>):RecyclerView.Adapter<RecyclerAdapterHistory.viewHolderHistory>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderHistory {

        return viewHolderHistory(LayoutInflater.from(context).inflate(R.layout.recycler_layout_history,parent,false))
    }

    override fun getItemCount(): Int {
        return array.size
    }


    override fun onBindViewHolder(holder: viewHolderHistory, positio: Int) {

        val position=array[positio]
        holder.textGroup.text=position.nameOfSGroup
        holder.textSection.text=position.nameOfSection
        holder.text_date.text=position.time
        holder.imageButton.setOnClickListener {

            val databaseRoom= DatabaseRoom.getDataBase(context).returnDao()

                CoroutineScope(IO).launch {

                    databaseRoom.deleteOneData(holder.textSection.text.toString(),holder.textGroup.text.toString(),holder.text_date.text.toString())

                    array= databaseRoom.readData() as ArrayList<ModelDataRoom>
                    CoroutineScope(Main).launch {

                        notifyDataSetChanged()
                    }
                }

        }

        holder.itemView.setOnClickListener {

            context.startActivity(Intent(context, com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments.History2Activity::class.java).putExtra(intentNameGroupExtra,holder.textSection.text.toString()).putExtra(
                intentNameGroupExtraGroup,holder.textGroup.text.toString()))

        }
    }


    inner class viewHolderHistory(view: View):RecyclerView.ViewHolder(view){

        var text_date=view.findViewById<TextView>(R.id.recycler_history_date)
        var textSection=view.findViewById<TextView>(R.id.recycler_history_section_name)
        var textGroup=view.findViewById<TextView>(R.id.recycler_history_group_name)
        var imageButton= view.findViewById<ImageButton>(R.id.recycler_history_image_button)!!

    }
}

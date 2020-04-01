package com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahmanhamdy2.absencementor2.R

class RecyclerAdapterMainActivity(var context: Context,var  array: ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataPastedFirebase>):RecyclerView.Adapter<RecyclerAdapterMainActivity.ViewHolderRecMain>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRecMain {

        var view= LayoutInflater.from(context).inflate(R.layout.recycler_layout_main_activity,parent,false)
        return ViewHolderRecMain(view)

    }

    override fun getItemCount(): Int {

        return array.size

    }

    override fun onBindViewHolder(holder: ViewHolderRecMain, position: Int) {

        var position1 = array[position]
        holder.text_view.text = position1.nameOfStudent
        holder.id.text = position1.idOfStudent


    }









    inner class ViewHolderRecMain(view:View):RecyclerView.ViewHolder(view){

        var text_view=view.findViewById<TextView>(R.id.recyclerNameMainActivity)
        var id=view.findViewById<TextView>(R.id.recyclerIdMainActivity)


    }

}

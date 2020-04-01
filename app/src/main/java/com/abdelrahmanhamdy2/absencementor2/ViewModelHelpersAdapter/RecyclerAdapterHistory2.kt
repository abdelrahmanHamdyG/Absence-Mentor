package com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahmanhamdy2.absencementor2.R

class RecyclerAdapterHistory2(var context: Context,var array: Array<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArray>) :RecyclerView.Adapter<RecyclerAdapterHistory2.ViewHolderHistory2>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHistory2 {


        return ViewHolderHistory2(LayoutInflater.from(context).inflate(R.layout.recycler_layout_main_activity_but_small,parent,false))

    }

    override fun getItemCount(): Int {

        return array.size

    }

    override fun onBindViewHolder(holder: ViewHolderHistory2, position: Int) {
        val position1 = array[position]
        holder.text_view.text = position1.nameOfStudent
        holder.id.text = position1.idOfStudent



    }


    inner class ViewHolderHistory2(view:View):RecyclerView.ViewHolder(view){


        var text_view=view.findViewById<TextView>(R.id.recyclerNameMainActivity)
        var id=view.findViewById<TextView>(R.id.recyclerIdMainActivity)


    }

}
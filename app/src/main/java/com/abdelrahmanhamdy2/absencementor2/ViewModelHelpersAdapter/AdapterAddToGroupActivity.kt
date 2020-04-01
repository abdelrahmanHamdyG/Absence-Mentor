package com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.Helper.Companion.showRecycler
import com.abdelrahmanhamdy2.absencementor2.R

class AdapterAddToGroupActivity(var context: Context, var array: ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArray>, var arrayNumber:ArrayList<Int>, var rec:RecyclerView) :RecyclerView.Adapter<AdapterAddToGroupActivity.AddToGroupViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddToGroupViewHolder {

        return AddToGroupViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_layout_add_to_group,parent,false))
    }


    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: AddToGroupViewHolder, position: Int) {
        holder.textName.text=array[position].nameOfStudent
        holder.textId.text=array[position].idOfStudent

        holder.button.setOnClickListener{

            array.removeAt(position)
            arrayNumber.removeAt(position)
            showRecycler(rec,array,arrayNumber,context)
        }

    }


    inner class AddToGroupViewHolder(view: View): RecyclerView.ViewHolder(view){

        var textName= view.findViewById<TextView>(R.id.studentNameRecyclerAddToGroup)!!
        var textId= view.findViewById<TextView>(R.id.studentIdRecyclerAddToGroup)!!
        var button= view.findViewById<ImageButton>(R.id.imageButtonRecyclerMinusAddToGroup)!!

    }
}
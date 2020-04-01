package com.abdelrahmanhamdy2.absencementor2.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DAO {


    @Insert
    fun insertData(modelDataRoom: ModelDataRoom)

    //////////////////////////////////////////////////////////////////

    @Query("SELECT * from MyTable")
    fun readData():List<ModelDataRoom>

    //////////////////////////////////////////////////////////////////

    @Query("DELETE FROM MyTable")
    fun deleteData()

    //////////////////////////////////////////////////////////////////

    @Query("SELECT * FROM MyTable WHERE nameOfSection = :name1")
    fun readOneData(name1:String): ModelDataRoom


    @Query("SELECT * FROM MyTable WHERE nameOfSection = :name1")
    fun readOneDataToArray(name1:String):List<ModelDataRoom>


    @Query("DELETE FROM MyTable WHERE nameOfSection = :sectionName  AND nameOfSGroup= :groupName AND time = :date")
    fun deleteOneData(sectionName:String,groupName:String,date:String)

}

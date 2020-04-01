package com.abdelrahmanhamdy2.absencementor2.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity(tableName = "MyTable")
    data class ModelDataRoom(
        @PrimaryKey(autoGenerate = true)
        var id:Int?,
        var time:String,
        var nameOfSection:String,
        var nameOfSGroup:String
        , var listAbsents:String
        , var listPresents:String
    )



package com.abdelrahmanhamdy2.absencementor2.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ModelDataRoom::class],version = 1 ,exportSchema = false)
abstract class DatabaseRoom:RoomDatabase() {



    abstract fun returnDao(): DAO

    companion object{

        @Volatile
        private var INSTANCE: DatabaseRoom?=null

        fun getDataBase(context: Context): DatabaseRoom {

            if (INSTANCE ==null){
                synchronized(this) {

                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        DatabaseRoom::class.java,"Data").build()
                }
            }
            return INSTANCE!!
        }

    }

}
package com.abdelrahmanhamdy2.absencementor2.Models

class ModelDataOfArrayWithSizeAndKey{

    var arrayOfStudent:ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArray>?=null
    var sizeOfArray:String?=null
    var key:String?=null

    constructor(){



    }

    constructor(arrayOfStudent:ArrayList<com.abdelrahmanhamdy2.absencementor2.Models.ModelDataOfArray>, sizeOfArray: String, key:String){

        this.arrayOfStudent=arrayOfStudent
        this.sizeOfArray=sizeOfArray
        this.key=key

    }

}

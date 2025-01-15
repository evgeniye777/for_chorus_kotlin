package com.example.for_chour_kotlin.PersonsInfo

class infoOnePerson {
    private  var id: Int=0
    private lateinit var name: String
    private var state: Int = 0
    private  var gender: Int=0
    private  var allowed: Int=0
    constructor(id0:Int,name0:String, state0:Int) {id=id0; name = name0; state = state0}
    fun getId():Int {return id}
    fun getName():String {return name}
    fun getState(): Int {return state}
    fun setState(state0:Int) {state = state0}
    fun getGender():Int {return gender}
    fun setGender(gender0:Int) {gender = gender0}
    fun getAllowed():Int {return allowed}
    fun setAllowed(allowed0:Int) {allowed = allowed0}
}
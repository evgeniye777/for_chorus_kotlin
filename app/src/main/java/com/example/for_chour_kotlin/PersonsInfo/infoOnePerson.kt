package com.example.for_chour_kotlin.PersonsInfo

class infoOnePerson {
    private lateinit var name: String
    private var state: Int = 0
    private  var gender: Int=0
    constructor(name0:String, state0:Int) {name = name0; state = state0}
    fun getName():String {return name}
    fun getState(): Int {return state}
    fun setState(state0:Int) {state = state0}
    fun getGender():Int {return gender}
    fun setGender(gender0:Int) {gender = gender0}
}
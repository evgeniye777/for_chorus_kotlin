package com.example.for_chour_kotlin.domain.entities

class infoOnePerson {
    var id: Int=0
    var name: String
    var state: Int = 0
    var gender: Int=0
    var allowed: Int=0
    var visible: Int=1
    constructor(id0:Int,name0:String, state0:Int) {id=id0; name = name0; state = state0}
}
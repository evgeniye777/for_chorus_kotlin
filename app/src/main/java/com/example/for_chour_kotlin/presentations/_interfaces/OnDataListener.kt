package com.example.for_chour_kotlin.presentations._interfaces

import com.example.for_chour_kotlin.data.typeData.appAllGroups.AppAllGroups

interface OnDataListener {
    fun onDataUpdate(counts: Triple<Int, Int, Int>)
}

interface OnMainMenuListener {
    fun onMainMenuUpdate(group: AppAllGroups)
}

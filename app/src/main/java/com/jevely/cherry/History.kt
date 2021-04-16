package com.jevely.cherry

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

const val S_PREF_NAME = "name"

const val COUNT_URLS = "count"

class History(context : Context) {
    var sPrefs : SharedPreferences = context.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
    private var count = sPrefs.getInt(COUNT_URLS, 0)
    var history : MutableList<String>

    init {
        history = if(count != 0)
            getOldHistoryFromSprefs().toMutableList()
        else
            mutableListOf()
    }


    private fun getOldHistoryFromSprefs() : List<String>{
        val urlsHistory = mutableListOf<String>()
        for(index in 0 until count){
            val item = sPrefs.getString(index.toString(), null)
            if (item != null) {
                urlsHistory.add(item)
            }
        }
        return urlsHistory
    }

    fun goBack(): String? {
        if(count > 0) {
            count--
            Log.e("Count", count.toString())
            Log.e("History", history.toString())
            return history.removeAt(count)
        }
        else return null
    }

    fun goForward(url : String){
        history.add(url)
        count++
    }

    fun saveHistory(){
        sPrefs.edit().putInt(COUNT_URLS, count).apply()
        for(index in 0 until history.size){
            sPrefs.edit().putString(index.toString(), history[index]).apply()
        }
    }
}
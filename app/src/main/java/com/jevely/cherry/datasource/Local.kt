package com.jevely.cherry.datasource

import android.util.Log
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class Local(private val db : ScoreDB) {

    fun getScoreAsync() : Deferred<List<ScoreEntity>> {
        return GlobalScope.async {
            db.scoreDao().getScores()
        }
    }

    fun getScoreIdAsync(id: Int) : Deferred<ScoreEntity> {
        return GlobalScope.async {
            db.scoreDao().getScoreId(id = id)
        }
    }

    fun removeScore(){
        GlobalScope.async {
            db.scoreDao().removeScores()
        }
    }

    fun deleteAllLessons(id : Int, player : String){
        GlobalScope.async {
            db.scoreDao().deleteScore(ScoreEntity(id = id,player = player))
        }
    }
}
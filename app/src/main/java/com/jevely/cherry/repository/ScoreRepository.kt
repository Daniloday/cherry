package com.jevely.cherry.repository

import com.jevely.cherry.datasource.Local

class ScoreRepository(var local : Local) : IScoreRepository {


    override suspend fun geScoreId(id : Int) : String{
        val entity = local.getScoreIdAsync(id).await()
        return if(entity != null)
            entity.toString()
        else "sync"
    }
    override suspend fun getScores() : String {
        val ent =  local.getScoreAsync().await()
        return ent.toString()
    }
    override suspend fun deleteAll(id : Int,player : String) {
        local.deleteAllLessons(id,player)
    }

    override suspend fun deleteScore() {
        local.removeScore()
    }

}
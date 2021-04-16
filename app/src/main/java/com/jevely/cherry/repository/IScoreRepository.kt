package com.jevely.cherry.repository

interface IScoreRepository {

    suspend fun getScores() : String
    suspend fun deleteScore()
    suspend fun deleteAll(id : Int,player : String)
    suspend fun geScoreId(id : Int) : String
}
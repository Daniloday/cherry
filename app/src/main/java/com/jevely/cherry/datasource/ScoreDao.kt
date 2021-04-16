package com.jevely.cherry.datasource

import androidx.room.*

@Dao
interface ScoreDao {

    @Query("SELECT * FROM score")
    fun getScores() : List<ScoreEntity>

    @Query("SELECT * FROM score WHERE id = :id")
    fun getScoreId(id : Int) : ScoreEntity

    @Query("DELETE FROM score")
    fun removeScores()

    @Delete
    fun deleteScore(scoreEntity: ScoreEntity)

}
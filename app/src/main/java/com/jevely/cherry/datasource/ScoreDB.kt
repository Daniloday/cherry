package com.jevely.cherry.datasource

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScoreEntity::class], version = 1)
abstract class ScoreDB : RoomDatabase(){
    abstract fun scoreDao() : ScoreDao

}
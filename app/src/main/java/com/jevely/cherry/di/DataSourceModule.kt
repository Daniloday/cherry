package com.jevely.cherry.di

import android.content.Context
import androidx.room.Room
import com.jevely.cherry.datasource.Local
import com.jevely.cherry.datasource.ScoreDB
import com.jevely.cherry.repository.IScoreRepository
import com.jevely.cherry.repository.ScoreRepository
import org.koin.dsl.module

val dataSourceModule = module{
    single {
        provideDatabase(get())
    }
    single {
        provideLocal(get())
    }
    single {
        provideRepository(get())
    }

}

fun provideLocal(ScoreDB: ScoreDB) : Local{
    return Local(ScoreDB)
}

fun provideRepository(local: Local): IScoreRepository {
    return ScoreRepository(local)
}

fun provideDatabase(context: Context) : ScoreDB {
    return Room.databaseBuilder(context, ScoreDB::class.java, "db")
        .fallbackToDestructiveMigration()
        .build()
}
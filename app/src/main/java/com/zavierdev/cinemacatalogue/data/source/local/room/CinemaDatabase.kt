package com.zavierdev.cinemacatalogue.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zavierdev.cinemacatalogue.data.source.local.entity.MovieEntity
import com.zavierdev.cinemacatalogue.data.source.local.entity.TvShowEntity
import com.zavierdev.cinemacatalogue.utils.DateConveter

@Database(
    entities = [MovieEntity::class, TvShowEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DateConveter::class)
abstract class CinemaDatabase : RoomDatabase() {
    abstract fun cinemaDao(): CinemaDao

    companion object {

        @Volatile
        private var INSTANCE: CinemaDatabase? = null

        fun getInstance(context: Context): CinemaDatabase {
            if (INSTANCE == null) {
                synchronized(CinemaDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            CinemaDatabase::class.java, "Cinema-Catalogue-DB"
                        ).build()
                    }
                }
            }

            return INSTANCE as CinemaDatabase
        }
    }
}
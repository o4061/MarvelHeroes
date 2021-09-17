package com.userfaltakas.marvelheroes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.userfaltakas.marvelheroes.data.api.Result

@Database(
    entities = [Result::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class HeroDatabase : RoomDatabase() {
    abstract fun getHeroDao(): HeroDao

    companion object {
        @Volatile
        private var instance: HeroDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                HeroDatabase::class.java,
                "squad_db.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}
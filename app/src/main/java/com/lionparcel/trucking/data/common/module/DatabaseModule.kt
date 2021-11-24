package com.lionparcel.trucking.data.common.module

import androidx.room.Room
import androidx.room.RoomDatabase.JournalMode
import com.lionparcel.trucking.BuildConfig
import com.lionparcel.trucking.data.RoomDatabase
import com.lionparcel.trucking.view.app.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    //TODO add Migration DB
    @Singleton
    @Provides
    fun provideRoomDatabase(): RoomDatabase {
        return Room.databaseBuilder(
            App.instance,
            RoomDatabase::class.java,
            BuildConfig.DATABASE_NAME
        ).setJournalMode(JournalMode.TRUNCATE)
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }
}

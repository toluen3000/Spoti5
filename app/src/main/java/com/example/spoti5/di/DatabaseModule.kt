package com.example.spoti5.di

//import android.content.Context
//import androidx.room.Room
//import com.example.spoti5.data.database.AppDatabase
//import com.example.spoti5.data.database.dao.AppDao
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DatabaseModule {
//    @Provides
//    @Singleton
//    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
//        return Room.databaseBuilder(context, AppDatabase::class.java, "AppDatabase").build()
//    }
//
//    @Provides
//    fun provideFilesDao(appDatabase: AppDatabase): AppDao {
//        return appDatabase.filesDao()
//    }
//}
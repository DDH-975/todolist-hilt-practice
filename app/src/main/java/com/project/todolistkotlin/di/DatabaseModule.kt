package com.project.todolistkotlin.di

import android.content.Context
import androidx.room.Room
import com.project.todolistkotlin.roomDB.TodoDao
import com.project.todolistkotlin.roomDB.TodoDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoDataBase{
        return Room.databaseBuilder(
            context,
            TodoDataBase::class.java,
            "todo_database"
        ).build()
    }

    @Provides
    fun provideTodoDao(dataBase: TodoDataBase): TodoDao{
        return dataBase.dao()
    }
}
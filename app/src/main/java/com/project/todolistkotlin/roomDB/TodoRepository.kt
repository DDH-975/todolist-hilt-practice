package com.project.todolistkotlin.roomDB

import androidx.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val dao: TodoDao
) {
    val allData: LiveData<List<TodoEntity>> = dao.getAllData()

    suspend fun insertData(todoEntity: TodoEntity) {
        dao.setInsertTodo(todoEntity)
    }

    suspend fun deleteDataById(id: Int) {
        dao.deleteDataWhereId(id)
    }
}



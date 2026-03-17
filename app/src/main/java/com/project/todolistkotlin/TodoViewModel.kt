package com.project.todolistkotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.todolistkotlin.roomDB.TodoEntity
import com.project.todolistkotlin.roomDB.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val repo: TodoRepository) : ViewModel() {
    val allData: LiveData<List<TodoEntity>> = repo.allData

    fun insertData(tododata: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertData(tododata)
        }
    }

    fun deleteById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteDataById(id)
        }
    }
}
package com.raywenderlich.android.librarian.ui.addBook

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.state.AddBookState
import com.raywenderlich.android.librarian.repository.LibrarianRepository

class AddBookViewModel @ViewModelInject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  private val _addBookState = MutableLiveData(AddBookState())
  val addBookState: LiveData<AddBookState> = _addBookState

  private val _genresState = MutableLiveData<List<Genre>>()
  val genresState: LiveData<List<Genre>> = _genresState

  private lateinit var view: AddBookView

  fun setView(view: AddBookView) {
    this.view = view
  }
}
package com.raywenderlich.android.librarian.ui.addBook

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.state.AddBookState
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import kotlinx.coroutines.launch

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

  fun loadGenres() {
    viewModelScope.launch {
      _genresState.value = repository.getGenres()
    }
  }

  fun onAddBookTapped() {
    val bookState = _addBookState.value ?: return

    if (bookState.name.isNotEmpty() &&
      bookState.description.isNotEmpty() &&
      bookState.genreId.isNotEmpty()
    ) {
      viewModelScope.launch {
        repository.addBook(
          Book(
            name = bookState.name,
            description = bookState.description,
            genreId = bookState.genreId
          )
        )

        view.onBookAdded()
      }
    }
  }

  fun onNameChanged(name: String) {
    _addBookState.value = _addBookState.value?.copy(name = name)
  }

  fun onDescriptionChanged(description: String) {
    _addBookState.value = _addBookState.value?.copy(description = description)
  }

  fun genrePicked(genre: Genre) {
    _addBookState.value = _addBookState.value?.copy(genreId = genre.id)
  }
}
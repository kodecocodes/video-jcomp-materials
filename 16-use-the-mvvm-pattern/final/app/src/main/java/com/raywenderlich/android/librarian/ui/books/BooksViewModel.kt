package com.raywenderlich.android.librarian.ui.books

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.books.filter.Filter

class BooksViewModel @ViewModelInject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  var filter: Filter? = null

  private val _booksState = MutableLiveData(emptyList<BookAndGenre>())
  val booksState: LiveData<List<BookAndGenre>> = _booksState

  private val _deleteBookState = MutableLiveData<BookAndGenre>()
  val deleteBookState: LiveData<BookAndGenre> = _deleteBookState

  private val _genresState = MutableLiveData<List<Genre>>()
  val genresState: LiveData<List<Genre>> = _genresState
}
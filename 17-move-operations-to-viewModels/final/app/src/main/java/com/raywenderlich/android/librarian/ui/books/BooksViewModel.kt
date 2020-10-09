package com.raywenderlich.android.librarian.ui.books

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.books.filter.ByGenre
import com.raywenderlich.android.librarian.ui.books.filter.ByRating
import com.raywenderlich.android.librarian.ui.books.filter.Filter
import kotlinx.coroutines.launch

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

  fun loadGenres() {
    viewModelScope.launch {
      val genres = repository.getGenres()

      _genresState.value = genres
    }
  }

  fun loadBooks() {
    viewModelScope.launch {

      val books = when (val currentFilter = filter) {
        is ByGenre -> repository.getBooksByGenre(currentFilter.genreId)
        is ByRating -> repository.getBooksByRating(currentFilter.rating)
        else -> repository.getBooks()
      }

      _booksState.value = books
    }
  }

  fun removeBook(book: Book) {
    viewModelScope.launch {
      repository.removeBook(book)
      loadBooks()
      cancelDeleteBook()
    }
  }

  fun showDeleteBook(bookAndGenre: BookAndGenre) {
    _deleteBookState.value = bookAndGenre
  }

  fun cancelDeleteBook() {
    _deleteBookState.value = null
  }
}
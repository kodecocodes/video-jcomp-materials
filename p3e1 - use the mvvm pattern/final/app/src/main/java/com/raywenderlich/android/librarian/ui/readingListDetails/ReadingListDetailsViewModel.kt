package com.raywenderlich.android.librarian.ui.readingListDetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.raywenderlich.android.librarian.model.BookItem
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.repository.LibrarianRepository

class ReadingListDetailsViewModel @ViewModelInject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  private val _addBookState = MutableLiveData<List<BookItem>>()
  val addBookState: LiveData<List<BookItem>> = _addBookState

  var readingListState: LiveData<ReadingListsWithBooks> = MutableLiveData()
    private set

  private val _deleteBookState = MutableLiveData<BookAndGenre>()
  val deleteBookState: LiveData<BookAndGenre> = _deleteBookState

  fun setReadingList(readingListsWithBooks: ReadingListsWithBooks) {
    readingListState = repository.getReadingListByIdFlow(readingListsWithBooks.id)
      .asLiveData(viewModelScope.coroutineContext)

    // refresh books
  }
}
package com.raywenderlich.android.librarian.ui.readingList

import androidx.lifecycle.*
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingListViewModel @Inject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  val readingListsState: LiveData<List<ReadingListsWithBooks>> =
    repository.getReadingListsFlow().asLiveData(viewModelScope.coroutineContext)

  private val _isShowingAddReadingListState = MutableLiveData(false)
  val isShowingAddReadingListState: LiveData<Boolean> = _isShowingAddReadingListState

  private val _deleteReadingListState = MutableLiveData<ReadingListsWithBooks?>()
  val deleteReadingListState: LiveData<ReadingListsWithBooks?> = _deleteReadingListState

  fun onAddReadingListTapped() {
    this._isShowingAddReadingListState.value = true
  }

  fun deleteReadingList(readingListsWithBooks: ReadingListsWithBooks) {
    viewModelScope.launch {
      repository.removeReadingList(
        ReadingList(
          readingListsWithBooks.id,
          readingListsWithBooks.name,
          readingListsWithBooks.books.map { it.book.id }
        )
      )

      onDialogDismiss()
    }
  }

  fun onDeleteReadingList(readingListsWithBooks: ReadingListsWithBooks) {
    _deleteReadingListState.value = readingListsWithBooks
  }

  fun addReadingList(readingListName: String) {
    viewModelScope.launch {
      repository.addReadingList(ReadingList(name = readingListName, bookIds = emptyList()))

      onDialogDismiss()
    }
  }

  fun onDialogDismiss() {
    _isShowingAddReadingListState.value = false
    _deleteReadingListState.value = null
  }
}
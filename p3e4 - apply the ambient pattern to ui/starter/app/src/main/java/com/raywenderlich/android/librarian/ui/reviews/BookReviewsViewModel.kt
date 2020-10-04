package com.raywenderlich.android.librarian.ui.reviews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import kotlinx.coroutines.launch

class BookReviewsViewModel @ViewModelInject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  val bookReviewsState: LiveData<List<BookReview>> = repository.getReviewsFlow().asLiveData(
    viewModelScope.coroutineContext
  )

  private val _deleteReviewState = MutableLiveData<BookReview>()
  val deleteReviewState: LiveData<BookReview> = _deleteReviewState

  fun onItemLongTapped(bookReview: BookReview) {
    _deleteReviewState.value = bookReview
  }

  fun deleteReview(bookReview: BookReview) {
    viewModelScope.launch {
      repository.removeReview(bookReview.review)
      onDialogDismissed()
    }
  }

  fun onDialogDismissed() {
    _deleteReviewState.value = null
  }
}
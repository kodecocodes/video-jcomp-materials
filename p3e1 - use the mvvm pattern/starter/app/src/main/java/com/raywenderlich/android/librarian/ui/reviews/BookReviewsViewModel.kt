package com.raywenderlich.android.librarian.ui.reviews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.repository.LibrarianRepository

class BookReviewsViewModel @ViewModelInject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  val bookReviewsState: LiveData<List<BookReview>> = repository.getReviewsFlow().asLiveData(
    viewModelScope.coroutineContext
  )

  private val _deleteReviewState = MutableLiveData<BookReview>()
  val deleteReviewState: LiveData<BookReview> = _deleteReviewState
}
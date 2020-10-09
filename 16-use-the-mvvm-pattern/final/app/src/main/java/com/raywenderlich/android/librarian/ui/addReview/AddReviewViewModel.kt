package com.raywenderlich.android.librarian.ui.addReview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.state.AddBookReviewState
import com.raywenderlich.android.librarian.repository.LibrarianRepository

class AddReviewViewModel @ViewModelInject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  private val _bookReviewState = MutableLiveData(AddBookReviewState())
  val bookReviewState: LiveData<AddBookReviewState> = _bookReviewState

  val booksState: LiveData<List<BookAndGenre>> = repository.getBooksFlow().asLiveData()

  private lateinit var addReviewView: AddReviewView

  fun setView(addReviewView: AddReviewView) {
    this.addReviewView = addReviewView
  }
}
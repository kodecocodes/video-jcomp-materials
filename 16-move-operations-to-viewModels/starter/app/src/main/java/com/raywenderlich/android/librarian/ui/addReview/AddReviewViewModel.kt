package com.raywenderlich.android.librarian.ui.addReview

import androidx.lifecycle.*
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.state.AddBookReviewState
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddReviewViewModel @Inject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  private val _bookReviewState = MutableLiveData(AddBookReviewState())
  val bookReviewState: LiveData<AddBookReviewState> = _bookReviewState

  val booksState: LiveData<List<BookAndGenre>> =
    repository.getBooksFlow().asLiveData(viewModelScope.coroutineContext)

  private lateinit var addReviewView: AddReviewView

  fun setView(addReviewView: AddReviewView) {
    this.addReviewView = addReviewView
  }

  fun addBookReview() {
    val state = _bookReviewState.value ?: return

    viewModelScope.launch {
      val bookId = state.bookAndGenre.book.id
      val imageUrl = state.bookImageUrl
      val notes = state.notes
      val rating = state.rating

      if (bookId.isNotEmpty() && imageUrl.isNotBlank() && notes.isNotBlank()) {
        val bookReview = Review(
          bookId = bookId,
          rating = rating,
          notes = notes,
          imageUrl = imageUrl,
          lastUpdatedDate = Date(),
          entries = emptyList()
        )
        repository.addReview(bookReview)

        addReviewView.onReviewAdded()
      }
    }
  }

  fun onBookPicked(bookAndGenre: BookAndGenre) {
    _bookReviewState.value = _bookReviewState.value?.copy(bookAndGenre = bookAndGenre)
  }

  fun onRatingSelected(rating: Int) {
    _bookReviewState.value = _bookReviewState.value?.copy(rating = rating)
  }

  fun onImageUrlChanged(imageUrl: String) {
    _bookReviewState.value = _bookReviewState.value?.copy(bookImageUrl = imageUrl)
  }

  fun onNotesChanged(notes: String) {
    _bookReviewState.value = _bookReviewState.value?.copy(notes = notes)
  }
}
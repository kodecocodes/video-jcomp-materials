/*
 * Copyright (c) 2022 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
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

package com.raywenderlich.android.librarian.ui.bookReviewDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.ReadingEntry
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.bookReviewDetails.animation.BookReviewDetailsScreenState
import com.raywenderlich.android.librarian.ui.bookReviewDetails.animation.Initial
import com.raywenderlich.android.librarian.ui.bookReviewDetails.animation.Loaded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookReviewDetailsViewModel @Inject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  private val _bookReviewDetailsState = MutableLiveData<BookReview>()
  val bookReviewDetailsState: LiveData<BookReview> = _bookReviewDetailsState

  private val _genreState = MutableLiveData<Genre>()
  val genreState: LiveData<Genre> = _genreState

  private val _deleteEntryState = MutableLiveData<ReadingEntry?>()
  val deleteEntryState: LiveData<ReadingEntry?> = _deleteEntryState

  private val _isShowingAddEntryState = MutableLiveData(false)
  val isShowingAddEntryState: LiveData<Boolean> = _isShowingAddEntryState

  private val _screenAnimationState = MutableLiveData<BookReviewDetailsScreenState>(Initial)
  val screenAnimationState: LiveData<BookReviewDetailsScreenState> = _screenAnimationState

  fun setReview(bookReview: BookReview) {
    _bookReviewDetailsState.value = bookReview

    viewModelScope.launch {
      _genreState.value = repository.getGenreById(bookReview.book.genreId)
    }
  }

  fun onFirstLoad(){
    _screenAnimationState.value = Loaded
  }

  fun addNewEntry(entry: String) {
    val data = _bookReviewDetailsState.value?.review ?: return

    val updatedReview = data.copy(
      entries = data.entries + ReadingEntry(comment = entry),
      lastUpdatedDate = Date()
    )

    updateReview(updatedReview)
    onDialogDismiss()
  }

  private fun updateReview(review: Review) {
    viewModelScope.launch {
      repository.updateReview(review)

      setReview(repository.getReviewById(review.id))
    }
  }

  fun onDialogDismiss() {
    _deleteEntryState.value = null
    _isShowingAddEntryState.value = false
  }

  fun removeReadingEntry(readingEntry: ReadingEntry) {
    val data = _bookReviewDetailsState.value?.review ?: return

    val updatedReview = data.copy(
      entries = data.entries - readingEntry,
      lastUpdatedDate = Date()
    )

    updateReview(updatedReview)
    onDialogDismiss()
  }

  fun onItemLongTapped(readingEntry: ReadingEntry) {
    _deleteEntryState.value = readingEntry
  }

  fun onAddEntryTapped() {
    _isShowingAddEntryState.value = true
  }
}
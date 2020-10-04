/*
 * Copyright (c) 2020 Razeware LLC
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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.ReadingEntry
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BookReviewDetailsActivity : AppCompatActivity() {

  @Inject
  lateinit var repository: LibrarianRepository
  private val _bookReviewDetailsState = MutableLiveData<BookReview>()
  private val _genreState = MutableLiveData<Genre>()

  companion object {
    private const val KEY_BOOK_REVIEW = "book_review"

    fun getIntent(context: Context, review: BookReview): Intent {
      val intent = Intent(context, BookReviewDetailsActivity::class.java)

      intent.putExtra(KEY_BOOK_REVIEW, review)
      return intent
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val data = intent?.getParcelableExtra<BookReview>(KEY_BOOK_REVIEW)

    if (data == null) {
      finish()
      return
    }

    setReview(data)
    setContent { BookReviewDetailsContent() }
  }

  @Composable
  fun BookReviewDetailsContent() {
    Scaffold(topBar = { BookReviewDetailsTopBar() },
      floatingActionButton = { AddReadingEntry() }) {
      BookReviewDetailsInformation()
    }
  }

  @Composable
  fun BookReviewDetailsTopBar() {
    val reviewState = _bookReviewDetailsState.value
    val bookName =
      reviewState?.book?.name ?: stringResource(id = R.string.book_review_details_title)

    TopBar(title = bookName, onBackPressed = { onBackPressed() })
  }

  @Composable
  fun AddReadingEntry() {
    FloatingActionButton(onClick = { }) {
      Icon(asset = Icons.Default.Add)
    }
  }

  @Composable
  fun BookReviewDetailsInformation() {

  }

  fun setReview(bookReview: BookReview) {
    _bookReviewDetailsState.value = bookReview

    lifecycleScope.launch {
      _genreState.value = repository.getGenreById(bookReview.book.genreId)
    }
  }

  fun addNewEntry(entry: String) {
    val data = _bookReviewDetailsState.value?.review ?: return

    val updatedReview = data.copy(
      entries = data.entries + ReadingEntry(comment = entry),
      lastUpdatedDate = Date()
    )

    updateReview(updatedReview)
  }

  fun removeReadingEntry(readingEntry: ReadingEntry) {
    val data = _bookReviewDetailsState.value?.review ?: return

    val updatedReview = data.copy(
      entries = data.entries - readingEntry,
      lastUpdatedDate = Date()
    )

    updateReview(updatedReview)
  }

  private fun updateReview(updatedReview: Review) {
    lifecycleScope.launch {
      repository.updateReview(updatedReview)

      setReview(repository.getReviewById(updatedReview.id))
    }
  }
}
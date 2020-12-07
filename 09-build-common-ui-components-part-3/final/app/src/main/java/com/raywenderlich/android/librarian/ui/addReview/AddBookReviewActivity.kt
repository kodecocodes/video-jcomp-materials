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

package com.raywenderlich.android.librarian.ui.addReview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.material.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.state.AddBookReviewState
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.addReview.ui.BookPicker
import com.raywenderlich.android.librarian.ui.composeUi.ActionButton
import com.raywenderlich.android.librarian.ui.composeUi.InputField
import com.raywenderlich.android.librarian.ui.composeUi.RatingBar
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.utils.EMPTY_BOOK_AND_GENRE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddBookReviewActivity : AppCompatActivity(), AddReviewView {

  @Inject
  lateinit var repository: LibrarianRepository

  private val _bookReviewState = mutableStateOf(AddBookReviewState())
  private val _books = mutableStateOf(emptyList<BookAndGenre>())

  companion object {
    fun getIntent(context: Context) = Intent(context, AddBookReviewActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { AddBookReviewContent() }
    loadBooks()
  }

  private fun loadBooks() {
    lifecycleScope.launch {
      _books.value = repository.getBooks()
    }
  }

  @Composable
  fun AddBookReviewContent() {
    Scaffold(topBar = { AddBookReviewTopBar() }) {
      AddBookReviewForm()
    }
  }

  @Composable
  fun AddBookReviewTopBar() {
    TopBar(
      onBackPressed = { onBackPressed() },
      title = stringResource(id = R.string.add_review_title)
    )
  }

  @Composable
  fun AddBookReviewForm() {
    val bookUrl = remember { mutableStateOf("") }
    val bookNotes = remember { mutableStateOf("") }
    val currentRatingFilter = remember { mutableStateOf(0) }
    val currentlySelectedBook = remember { mutableStateOf(EMPTY_BOOK_AND_GENRE) }

    ScrollableColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = stringResource(id = R.string.book_picker_hint),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(8.dp))

      BookPicker(
        books = _books.value,
        selectedBookId = currentlySelectedBook.value.book.id,
        onItemPicked = {
          _bookReviewState.value = _bookReviewState.value.copy(bookAndGenre = it)
        })

      Spacer(modifier = Modifier.height(8.dp))

      InputField(
        label = stringResource(id = R.string.book_image_url_input_hint),
        value = bookUrl.value,
        onStateChanged = { url ->
          _bookReviewState.value = _bookReviewState.value.copy(bookImageUrl = url)
          bookUrl.value = url
        }
      )

      Spacer(modifier = Modifier.height(16.dp))

      RatingBar(range = 1..5,
        currentRating = currentRatingFilter.value,
        isLargeRating = true,
        onRatingChanged = { newRating ->
          currentRatingFilter.value = newRating
          _bookReviewState.value = _bookReviewState.value.copy(rating = newRating)
        })

      Spacer(modifier = Modifier.height(16.dp))

      InputField(
        label = stringResource(id = R.string.review_notes_hint),
        value = bookNotes.value,
        onStateChanged = { notes ->
          _bookReviewState.value = _bookReviewState.value.copy(notes = notes)
          bookNotes.value = notes
        }
      )

      Spacer(modifier = Modifier.height(16.dp))

      ActionButton(
        modifier = Modifier.fillMaxWidth(0.7f),
        text = stringResource(id = R.string.add_book_review_text),
        onClick = ::addBookReview
      )

      Spacer(modifier = Modifier.height(16.dp))
    }
  }

  fun addBookReview() {
    val state = _bookReviewState.value ?: return

    lifecycleScope.launch {
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

        onReviewAdded()
      }
    }
  }

  override fun onReviewAdded() {
    setResult(RESULT_OK)
    finish()
  }
}
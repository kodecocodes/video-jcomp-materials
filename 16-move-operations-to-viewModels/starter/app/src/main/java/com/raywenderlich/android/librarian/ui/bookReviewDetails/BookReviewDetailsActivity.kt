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

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.ReadingEntry
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.bookReviewDetails.animation.*
import com.raywenderlich.android.librarian.ui.bookReviewDetails.readingEntries.AddReadingEntryDialog
import com.raywenderlich.android.librarian.ui.bookReviewDetails.readingEntries.ReadingEntries
import com.raywenderlich.android.librarian.ui.composeUi.DeleteDialog
import com.raywenderlich.android.librarian.ui.composeUi.LibrarianTheme
import com.raywenderlich.android.librarian.ui.composeUi.RatingBar
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.utils.EMPTY_BOOK_REVIEW
import com.raywenderlich.android.librarian.utils.EMPTY_GENRE
import com.raywenderlich.android.librarian.utils.formatDateToText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BookReviewDetailsActivity : AppCompatActivity() {

  private val bookReviewDetailsViewModel by viewModels<BookReviewDetailsViewModel>()

  @Inject
  lateinit var repository: LibrarianRepository

  private val _bookReviewDetailsState = mutableStateOf(EMPTY_BOOK_REVIEW)
  private val _genreState = mutableStateOf(EMPTY_GENRE)
  private val _deleteEntryState = mutableStateOf<ReadingEntry?>(null)
  private val _isShowingAddEntryState = mutableStateOf(false)
  private val _screenState = mutableStateOf<BookReviewDetailsScreenState>(Initial)

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

    val data = if (Build.VERSION.SDK_INT >= 33) {
      intent?.getParcelableExtra(KEY_BOOK_REVIEW, BookReview::class.java)
    } else {
      intent?.getParcelableExtra(KEY_BOOK_REVIEW)
    }

    if (data == null) {
      finish()
      return
    }

    setReview(data)
    setContent {
      LibrarianTheme {
        BookReviewDetailsContent()
      }
    }
  }

  @Composable
  fun BookReviewDetailsContent() {
    val animationState by _screenState
    val state = animateBookReviewDetails(screenState = animationState)

    LaunchedEffect(Unit, block = {
      _screenState.value = Loaded
    })

    Scaffold(topBar = { BookReviewDetailsTopBar() },
      floatingActionButton = { AddReadingEntry(state) }) {
      BookReviewDetailsInformation(state)
    }
  }

  @Composable
  fun BookReviewDetailsTopBar() {
    val reviewState = _bookReviewDetailsState.value
    val bookName = reviewState.book.name

    TopBar(title = bookName, onBackPressed = { onBackPressedDispatcher.onBackPressed() })
  }

  @Composable
  fun AddReadingEntry(state: BookReviewDetailsTransitionState) {
    FloatingActionButton(
      modifier = Modifier.size(state.floatingButtonSize),
      onClick = { _isShowingAddEntryState.value = true }) {
      Icon(imageVector = Icons.Default.Add, contentDescription = "Add Reading Entry")
    }
  }

  @Composable
  fun BookReviewDetailsInformation(state: BookReviewDetailsTransitionState) {
    val bookReview = _bookReviewDetailsState.value
    val genre = _genreState.value

    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center) {

      ReadingEntries(
        readingEntries = bookReview.review.entries,
        onItemLongClick = { _deleteEntryState.value = it },
        content = {
          Column(
            modifier = Modifier
              .fillMaxSize(),
            horizontalAlignment = CenterHorizontally,
          ) {

            Spacer(modifier = Modifier.height(state.imageMarginTop))

            Card(
              modifier = Modifier
                .size(width = 200.dp, height = 300.dp),
              shape = RoundedCornerShape(16.dp),
              elevation = 16.dp
            ) {
              AsyncImage(
                model = bookReview.review.imageUrl,
                contentScale = ContentScale.FillWidth,
                contentDescription = null)
            }

            Spacer(modifier = Modifier.height(state.titleMarginTop))

            Text(
              text = bookReview.book.name,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = MaterialTheme.colors.onPrimary
            )

            Spacer(modifier = Modifier.height(state.contentMarginTop))

            Text(
              text = genre.name,
              fontSize = 12.sp,
              color = MaterialTheme.colors.onPrimary
            )

            Spacer(modifier = Modifier.height(state.contentMarginTop))

            RatingBar(
              modifier = Modifier.align(CenterHorizontally),
              range = 1..5,
              isSelectable = false,
              isLargeRating = false,
              currentRating = bookReview.review.rating,
              onRatingChanged = {}
            )

            Spacer(modifier = Modifier.height(state.contentMarginTop))

            Text(
              modifier = Modifier.alpha(state.contentAlpha),
              text = stringResource(
                id = R.string.last_updated_date,
                formatDateToText(bookReview.review.lastUpdatedDate)
              ),
              fontSize = 12.sp,
              color = MaterialTheme.colors.onPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(
              modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(1.dp)
                .background(
                  brush = SolidColor(value = Color.LightGray),
                  shape = RectangleShape
                )
            )

            Text(
              modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 8.dp)
                .alpha(state.contentAlpha),
              text = bookReview.review.notes,
              fontSize = 12.sp,
              fontStyle = FontStyle.Italic,
              color = MaterialTheme.colors.onPrimary
            )

            Spacer(
              modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(1.dp)
                .background(
                  brush = SolidColor(value = Color.LightGray),
                  shape = RectangleShape
                )
            )
          }

          Spacer(Modifier.size(16.dp))
        })

      if (_isShowingAddEntryState.value) {
        AddReadingEntryDialog(
          onDismiss = { _isShowingAddEntryState.value = false },
          onReadingEntryFinished = {
            addNewEntry(it)
            _isShowingAddEntryState.value = false
          }
        )
      }

      val entryToDelete = _deleteEntryState.value

      if (entryToDelete != null) {
        DeleteDialog(
          item = entryToDelete,
          message = stringResource(id = R.string.delete_entry_message),
          onDeleteItem = {
            removeReadingEntry(it)
            _deleteEntryState.value = null
          },
          onDismiss = { _deleteEntryState.value = null }
        )
      }
    }
  }

  fun setReview(bookReview: BookReview) {
    _bookReviewDetailsState.value = bookReview

    lifecycleScope.launch {
      _genreState.value = repository.getGenreById(bookReview.book.genreId)
    }
  }

  fun addNewEntry(entry: String) {
    val data = _bookReviewDetailsState.value.review

    val updatedReview = data.copy(
      entries = data.entries + ReadingEntry(comment = entry),
      lastUpdatedDate = Date()
    )

    updateReview(updatedReview)
  }

  fun removeReadingEntry(readingEntry: ReadingEntry) {
    val data = _bookReviewDetailsState.value.review

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
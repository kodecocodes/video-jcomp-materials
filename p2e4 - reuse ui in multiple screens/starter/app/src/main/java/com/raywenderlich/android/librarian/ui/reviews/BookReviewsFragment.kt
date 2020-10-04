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

package com.raywenderlich.android.librarian.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.ColumnScope.align
import androidx.compose.foundation.layout.RowScope.align
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.bookReviewDetails.BookReviewDetailsActivity
import com.raywenderlich.android.librarian.ui.composeUi.DeleteReviewDialog
import com.raywenderlich.android.librarian.ui.composeUi.LibrarianTheme
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.ui.reviews.ui.BookReviewsList
import com.raywenderlich.android.librarian.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Fetches and displays notes from the API.
 */

private const val REQUEST_CODE_ADD_REVIEW = 202

@AndroidEntryPoint
class BookReviewsFragment : Fragment() {

  @Inject
  lateinit var repository: LibrarianRepository

  val bookReviewsState = mutableStateOf(emptyList<BookReview>())
  private val _deleteReviewState = mutableStateOf<BookReview?>(null)

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return ComposeView(requireContext()).apply {
      setContent {
        LibrarianTheme {
          BookReviewsContent()
        }
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    lifecycleScope.launch {
      bookReviewsState.value = repository.getReviews()
    }
  }

  @Composable
  fun BookReviewsContent() {
    Scaffold(
      topBar = { BookReviewsTopBar() },
      floatingActionButton = { AddBookReview() }
    ) {
      BookReviewsContentWrapper()
    }
  }

  @Composable
  fun BookReviewsTopBar() {
    TopBar(title = stringResource(id = R.string.book_reviews_title))
  }

  @Composable
  fun AddBookReview() {
    FloatingActionButton(onClick = { startAddBookReview() }) {
      Icon(asset = Icons.Default.Add)
    }
  }

  @Composable
  fun BookReviewsContentWrapper() {
    val bookReviews = bookReviewsState.value

    Stack(
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .align(Alignment.CenterVertically)
        .fillMaxSize()
    ) {
      BookReviewsList(bookReviews,
        onItemClick = ::onItemSelected,
        onItemLongTap = { bookReview ->
          _deleteReviewState.value = bookReview
        })

      val reviewToDelete = _deleteReviewState.value

      if (reviewToDelete != null) {
        DeleteReviewDialog(
          item = reviewToDelete,
          message = stringResource(id = R.string.delete_review_message, reviewToDelete.book.name),
          onDeleteItem = { bookReview ->
            deleteReview(bookReview)
            _deleteReviewState.value = null
          },
          onDismiss = {
            _deleteReviewState.value = null
          }
        )
      }
    }
  }

  fun deleteReview(bookReview: BookReview) {
    lifecycleScope.launch {
      repository.removeReview(bookReview.review)

      bookReviewsState.value = repository.getReviews()
    }
  }

  private fun startAddBookReview() {
    val addReview =
      registerForActivityResult(AddBookReviewContract()) { isReviewAdded ->
        if (isReviewAdded) {
          activity?.toast("Review added!")
        }
      }

    addReview.launch(REQUEST_CODE_ADD_REVIEW)
  }

  private fun onItemSelected(item: BookReview) {
    startActivity(BookReviewDetailsActivity.getIntent(requireContext(), item))
  }
}
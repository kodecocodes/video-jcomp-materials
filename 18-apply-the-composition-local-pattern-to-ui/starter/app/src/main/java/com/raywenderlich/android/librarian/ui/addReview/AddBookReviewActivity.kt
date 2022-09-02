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

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.state.AddBookReviewState
import com.raywenderlich.android.librarian.ui.composeUi.*
import com.raywenderlich.android.librarian.utils.EMPTY_BOOK_AND_GENRE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBookReviewActivity : AppCompatActivity(), AddReviewView {

  private val addBookReviewViewModel by viewModels<AddReviewViewModel>()

  companion object {
    fun getIntent(context: Context) = Intent(context, AddBookReviewActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addBookReviewViewModel.setView(this)
    setContent {
      LibrarianTheme {
        AddBookReviewContent()
      }
    }
  }

  @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
    val books by addBookReviewViewModel.booksState.observeAsState(emptyList())
    val reviewState by addBookReviewViewModel.bookReviewState.observeAsState(AddBookReviewState())

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(state = rememberScrollState()),
      horizontalAlignment = CenterHorizontally
    ) {
      Text(
        text = stringResource(id = R.string.book_picker_hint),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onPrimary
      )

      Spacer(modifier = Modifier.height(8.dp))

      SpinnerPicker(
        pickerText = stringResource(id = R.string.book_select),
        items = books,
        itemToName = { it.book.name },
        preselectedItem = reviewState.bookAndGenre,
        onItemPicked = { bookAndGenre -> addBookReviewViewModel.onBookPicked(bookAndGenre) })

      Spacer(modifier = Modifier.height(8.dp))

      InputField(
        label = stringResource(id = R.string.book_image_url_input_hint),
        value = reviewState.bookImageUrl,
        onStateChanged = { url -> addBookReviewViewModel.onImageUrlChanged(url) },
        isInputValid = reviewState.bookImageUrl.isNotEmpty()
      )

      Spacer(modifier = Modifier.height(16.dp))

      RatingBar(
        modifier = Modifier.align(CenterHorizontally),
        range = 1..5,
        currentRating = reviewState.rating,
        isSelectable = true,
        isLargeRating = true,
        onRatingChanged = { newRating -> addBookReviewViewModel.onRatingSelected(newRating) })

      Spacer(modifier = Modifier.height(16.dp))

      InputField(
        label = stringResource(id = R.string.review_notes_hint),
        value = reviewState.notes,
        onStateChanged = { notes -> addBookReviewViewModel.onNotesChanged(notes) },
        isInputValid = reviewState.notes.isNotEmpty()
      )

      Spacer(modifier = Modifier.height(16.dp))

      ActionButton(
        modifier = Modifier.fillMaxWidth(0.7f),
        text = stringResource(id = R.string.add_book_review_text),
        onClick = addBookReviewViewModel::addBookReview,
        isEnabled = reviewState.bookImageUrl.isNotEmpty() &&
            reviewState.notes.isNotEmpty() &&
            reviewState.bookAndGenre != EMPTY_BOOK_AND_GENRE
      )

      Spacer(modifier = Modifier.height(16.dp))
    }
  }

  override fun onReviewAdded() {
    setResult(RESULT_OK)
    finish()
  }
}
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

package com.raywenderlich.android.librarian.ui.reviews.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.ui.composeUi.RatingBar

@ExperimentalFoundationApi
@Composable
fun BookReviewsList(
  bookReviews: List<BookReview>,
  onItemClick: (BookReview) -> Unit,
  onItemLongClick: (BookReview) -> Unit
) {
  LazyColumn(modifier = Modifier.fillMaxSize()) {
    items(bookReviews) { bookReview ->
      BookReviewItem(bookReview = bookReview, onItemClick, onItemLongClick)
    }
  }
}

@ExperimentalFoundationApi
@Composable
fun BookReviewItem(
  bookReview: BookReview,
  onItemClick: (BookReview) -> Unit,
  onItemLongClick: (BookReview) -> Unit) {
  Card(elevation = 8.dp,
    border = BorderStroke(1.dp, MaterialTheme.colors.primary),
    shape = RoundedCornerShape(16.dp),
    modifier = Modifier
      .wrapContentHeight()
      .padding(16.dp)
      .combinedClickable(
        interactionSource = MutableInteractionSource(),
        indication = null,
        onClick = { onItemClick(bookReview) },
      onLongClick = { onItemLongClick(bookReview) })) {
    Row(modifier = Modifier.fillMaxSize()) {

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier
        .weight(0.6f)
        .fillMaxHeight()) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = bookReview.book.name,
          color = MaterialTheme.colors.primary,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        Row {
          Text(text = stringResource(id = R.string.rating_text))

          RatingBar(
            modifier = Modifier.align(CenterVertically),
            range = 1..5,
            currentRating = bookReview.review.rating,
            isSelectable = false,
            isLargeRating = false,
            onRatingChanged = {})
        }

        Text(text = stringResource(id = R.string.number_of_reading_entries, bookReview.review.entries.size))

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = bookReview.review.notes,
          fontSize = 12.sp,
          modifier = Modifier.fillMaxSize(),
          overflow = TextOverflow.Ellipsis,
          fontStyle = FontStyle.Italic,
          maxLines = 4)

        Spacer(modifier = Modifier.height(16.dp))
      }

      Spacer(modifier = Modifier.width(16.dp))

      Card(modifier = Modifier.weight(0.4f),
        shape = RoundedCornerShape(
          topEnd = 16.dp,
          topStart = 16.dp,
          bottomEnd = 16.dp,
          bottomStart = 0.dp),
        elevation = 16.dp) {

        /**
         * Update Note: CoilImage composable is old not compatible with current Jetpack Compose version.
         * Use AsyncImage instead of CoilImage composable with updated parameters.
         * */
        AsyncImage(
          model = bookReview.review.imageUrl,
          contentScale = ContentScale.FillWidth,
          contentDescription = null)
      }
    }
  }
}
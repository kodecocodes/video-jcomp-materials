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

package com.raywenderlich.android.librarian.ui.readingListDetails.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.BookItem
import com.raywenderlich.android.librarian.ui.composeUi.ActionButton

@Composable
fun BookPicker(
  modifier: Modifier,
  books: List<BookItem>,
  onBookSelected: (BookItem) -> Unit,
  onBookPicked: () -> Unit,
  onDismiss: () -> Unit
) {

  val hasSelectedBook = books.any { it.isSelected }

  if (books.isNotEmpty()) {

    Row(
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxWidth()
    ) {
      ActionButton(
        modifier = Modifier.weight(0.5f),
        isEnabled = hasSelectedBook,
        text = stringResource(id = R.string.add_book_to_reading_list_button_text),
        onClick = { onBookPicked() })

      ActionButton(
        modifier = Modifier.weight(0.5f),
        text = stringResource(id = R.string.cancel),
        onClick = { onDismiss() },
        isEnabled = true)
    }

    LazyColumn {
      items(books) { bookItem ->
        BookPickerItem(bookItem = bookItem, onBookSelected = onBookSelected)
      }
    }
  } else {
    Text(
      text = stringResource(id = R.string.no_books_to_add_to_reading_list),
      modifier =
      modifier
        .fillMaxWidth()
        .padding(16.dp),
      fontSize = 16.sp,
      fontWeight = FontWeight.SemiBold,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colors.onPrimary
    )

    ActionButton(
      enabledColor = colorResource(id = R.color.orange_200),
      modifier =
      modifier.fillMaxWidth(0.5f),
      text = stringResource(id = R.string.cancel),
      onClick = { onDismiss() },
      isEnabled = true)

    Image(
      modifier = modifier,
      painter = painterResource(id = R.drawable.box_of_books),
      contentDescription = null)
  }
}


@Composable
fun BookPickerItem(
  bookItem: BookItem,
  onBookSelected: (BookItem) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    RadioButton(
      selected = bookItem.isSelected,
      onClick = { onBookSelected(bookItem) }
    )

    Text(
      text = bookItem.name,
      modifier = Modifier.padding(16.dp)
    )
  }
}
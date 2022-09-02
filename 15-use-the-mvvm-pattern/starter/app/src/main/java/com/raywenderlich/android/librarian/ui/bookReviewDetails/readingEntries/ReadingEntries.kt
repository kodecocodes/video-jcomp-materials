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

package com.raywenderlich.android.librarian.ui.bookReviewDetails.readingEntries

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raywenderlich.android.librarian.model.ReadingEntry
import com.raywenderlich.android.librarian.utils.formatDateToText

@Composable
fun ReadingEntries(
  readingEntries: List<ReadingEntry>,
  onItemLongClick: (ReadingEntry) -> Unit,
  content: @Composable () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(top = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    item { content() }

    items(readingEntries) { entry ->
      ReadingEntryItem(entry, onItemLongClick)
    }

    item { Spacer(modifier = Modifier.height(96.dp)) }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReadingEntryItem(
  entry: ReadingEntry,
  onItemLongClick: (ReadingEntry) -> Unit
) {
  Card(
    elevation = 8.dp,
    border = BorderStroke(1.dp, MaterialTheme.colors.primary),
    shape = RoundedCornerShape(16.dp),
    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
  ) {
    Row(
      Modifier
        .fillMaxWidth()
        .combinedClickable(
          onClick = {},
          onLongClick = { onItemLongClick(entry) },
          indication = null,
          interactionSource = MutableInteractionSource()
        )
    ) {
      Spacer(modifier = Modifier.width(16.dp))

      Column(Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = formatDateToText(entry.dateOfEntry),
          fontStyle = FontStyle.Italic,
          color = MaterialTheme.colors.primaryVariant,
          fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = entry.comment,
          modifier = Modifier.padding(end = 16.dp),
          color = MaterialTheme.colors.onPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
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

package com.raywenderlich.android.librarian.ui.books.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.ui.books.filter.ByGenre
import com.raywenderlich.android.librarian.ui.books.filter.ByRating
import com.raywenderlich.android.librarian.ui.books.filter.Filter
import com.raywenderlich.android.librarian.ui.composeUi.ActionButton
import com.raywenderlich.android.librarian.ui.composeUi.RatingBar
import com.raywenderlich.android.librarian.ui.composeUi.SpinnerPicker

@Composable
fun BookFilter(
  modifier: Modifier = Modifier,
  filter: Filter?,
  genres: List<Genre>,
  onFilterSelected: (Filter?) -> Unit
) {
  val currentFilter = remember {
    mutableStateOf(when (filter) {
      null -> 0
      is ByGenre -> 1
      is ByRating -> 2
    })
  }

  val currentGenreFilter = remember { mutableStateOf<Genre?>(null) }
  val currentRatingFilter = remember { mutableStateOf(0) }

  Column(modifier = modifier.fillMaxSize(),
    horizontalAlignment = CenterHorizontally) {

    Column {
      Row {
        RadioButton(selected = currentFilter.value == 0,
          onClick = { currentFilter.value = 0 },
          modifier = Modifier.padding(8.dp)
        )

        Text(text = stringResource(id = R.string.no_filter),
          modifier = Modifier.align(CenterVertically),
          color = MaterialTheme.colors.onPrimary)
      }

      Row {
        RadioButton(selected = currentFilter.value == 1,
          onClick = { currentFilter.value = 1 },
          modifier = Modifier.padding(8.dp)
        )

        Text(text = stringResource(id = R.string.filter_by_genre),
          modifier = Modifier.align(CenterVertically), color = MaterialTheme.colors.onPrimary)
      }

      Row {
        RadioButton(selected = currentFilter.value == 2,
          onClick = { currentFilter.value = 2 },
          modifier = Modifier.padding(8.dp)
        )

        Text(text = stringResource(id = R.string.filter_by_rating),
          modifier = Modifier.align(CenterVertically), color = MaterialTheme.colors.onPrimary)
      }
    }

    val currentlySelectedGenre = currentGenreFilter.value

    if (currentFilter.value == 1) {
      SpinnerPicker(
        pickerText = stringResource(id = R.string.genre_select),
        items = genres,
        preselectedItem = currentlySelectedGenre,
        itemToName = { it.name },
        onItemPicked = { currentGenreFilter.value = it })
    }

    if (currentFilter.value == 2) {
      RatingBar(
        modifier = Modifier.align(CenterHorizontally),
        range = 1..5,
        currentRating = currentRatingFilter.value,
        isLargeRating = true,
        onRatingChanged = { newRating -> currentRatingFilter.value = newRating }
      )
    }

    ActionButton(
      modifier = Modifier.fillMaxWidth(),
      text = stringResource(id = R.string.confirm_filter), isEnabled = true,
      onClick = {
        val newFilter = when (currentFilter.value) {
          0 -> null
          1 -> ByGenre(currentGenreFilter.value?.id ?: "")
          2 -> ByRating(currentRatingFilter.value)
          else -> throw IllegalArgumentException("Unknown filter!")
        }

        onFilterSelected(newFilter)
      }
    )
  }
}
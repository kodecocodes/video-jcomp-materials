package com.raywenderlich.android.librarian.ui.books.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.ui.books.filter.ByGenre
import com.raywenderlich.android.librarian.ui.books.filter.ByRating
import com.raywenderlich.android.librarian.ui.books.filter.Filter
import com.raywenderlich.android.librarian.ui.composeUi.ActionButton
import com.raywenderlich.android.librarian.ui.composeUi.GenrePicker
import com.raywenderlich.android.librarian.ui.composeUi.RatingBar

@Composable
fun BookFilter(
  filter: Filter?,
  genres: List<Genre>,
  onFilterSelected: (Filter?) -> Unit
) {
  val currentFilter = remember {
    mutableStateOf(
      when (filter) {
        null -> 0
        is ByGenre -> 1
        is ByRating -> 2
      }
    )
  }

  val currentGenreFilter = remember { mutableStateOf<Genre?>(null) }
  val currentRatingFilter = remember { mutableStateOf(0) }

  Column(
    modifier = with(ColumnScope) { Modifier.align(Alignment.CenterHorizontally) },
    horizontalAlignment = Alignment.CenterHorizontally
  ) {

    Column {
      Row {
        RadioButton(
          selected = currentFilter.value == 0,
          onClick = { currentFilter.value = 0 },
          modifier = Modifier.padding(8.dp)
        )

        Text(
          text = stringResource(id = R.string.no_filter),
          modifier = Modifier.align(Alignment.CenterVertically),
          color = MaterialTheme.colors.onPrimary
        )
      }

      Row {
        RadioButton(
          selected = currentFilter.value == 1,
          onClick = { currentFilter.value = 1 },
          modifier = Modifier.padding(8.dp)
        )

        Text(
          text = stringResource(id = R.string.filter_by_genre),
          modifier = Modifier.align(Alignment.CenterVertically),
          color = MaterialTheme.colors.onPrimary
        )
      }

      Row {
        RadioButton(
          selected = currentFilter.value == 2,
          onClick = { currentFilter.value = 2 },
          modifier = Modifier.padding(8.dp)
        )

        Text(
          text = stringResource(id = R.string.filter_by_rating),
          modifier = Modifier.align(Alignment.CenterVertically),
          color = MaterialTheme.colors.onPrimary
        )
      }
    }

    val currentlySelectedGenre = currentGenreFilter.value

    if (currentFilter.value == 1) {
      GenrePicker(
        genres = genres,
        selectedGenreId = currentlySelectedGenre?.id ?: "",
        onItemPicked = { currentGenreFilter.value = it })
    }

    if (currentFilter.value == 2) {
      RatingBar(range = 1..5,
        currentRating = currentRatingFilter.value,
        isLargeRating = true,
        onRatingChanged = { newRating -> currentRatingFilter.value = newRating })
    }

    ActionButton(
      modifier = Modifier.fillMaxWidth(),
      text = stringResource(id = R.string.confirm_filter),
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
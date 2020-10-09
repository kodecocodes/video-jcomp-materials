package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.foundation.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope.align
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R

@Composable
fun RatingBar(
  range: IntRange,
  isLargeRating: Boolean = true,
  isSelectable: Boolean = true,
  currentRating: Int = 0,
  onRatingChanged: (Int) -> Unit = {}
) {

  val selectedRating = remember { mutableStateOf(currentRating) }

  LazyRowFor(items = range.toList(),
    modifier = Modifier.align(Alignment.CenterHorizontally)) { index ->
    RatingItem(
      isSelected = index <= selectedRating.value,
      isSelectable = isSelectable,
      index,
      isLargeRating
    ) { newRating ->
      selectedRating.value = newRating
      onRatingChanged(selectedRating.value)
    }
  }
}

@Composable
fun RatingItem(
  isSelected: Boolean,
  isSelectable: Boolean,
  rating: Int,
  isLargeRating: Boolean,
  onRatingChanged: (Int) -> Unit
) {

  val padding = if (isLargeRating) 2.dp else 0.dp
  val size = if (isLargeRating) 48.dp else 16.dp

  val baseModifier = if (isSelectable) {
    Modifier.clickable(onClick = { onRatingChanged(rating) }, indication = null)
  } else {
    Modifier
  }

  Icon(
    asset = if (isSelected)
      Icons.Default.Star
    else
      vectorResource(id = R.drawable.ic_baseline_star_outline_24),
    modifier = baseModifier
      .padding(padding)
      .size(size),
    tint = colorResource(id = R.color.orange_200)
  )
}
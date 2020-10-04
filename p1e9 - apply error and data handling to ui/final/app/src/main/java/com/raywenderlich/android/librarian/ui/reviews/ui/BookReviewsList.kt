package com.raywenderlich.android.librarian.ui.reviews.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.ui.composeUi.RatingBar
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun BookReviewsList(
  bookReviews: List<BookReview>,
  onItemClick: (BookReview) -> Unit
) {

  LazyColumnFor(items = bookReviews) { bookReview ->
    BookReviewItem(bookReview, onItemClick)
  }
}

@Composable
fun BookReviewItem(
  bookReview: BookReview,
  onItemClick: (BookReview) -> Unit) {

  Card(elevation = 8.dp,
    border = BorderStroke(1.dp, MaterialTheme.colors.primary),
    shape = RoundedCornerShape(16.dp),
    modifier = Modifier
      .wrapContentHeight()
      .padding(16.dp)
      .clickable(onClick = { onItemClick(bookReview) }, indication = null)) {
    Row(modifier = Modifier.fillMaxSize()) {
      Spacer(modifier = Modifier.size(16.dp))

      Column(
        modifier = Modifier
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
            range = 1..5,
            currentRating = bookReview.review.rating,
            isSelectable = false,
            isLargeRating = false)
        }

        Text(text =
        stringResource(id = R.string.number_of_reading_entries,
          bookReview.review.entries.size)
        )

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

      Card(
        modifier = Modifier.weight(0.4f),
        shape = RoundedCornerShape(topRight = 16.dp,
          topLeft = 16.dp,
          bottomLeft = 0.dp,
          bottomRight = 16.dp),
        elevation = 16.dp
      ) {
        CoilImage(
          data = bookReview.review.imageUrl,
          contentScale = ContentScale.FillWidth)
      }
    }
  }
}
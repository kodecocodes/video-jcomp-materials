package com.raywenderlich.android.librarian.ui.books.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raywenderlich.android.librarian.model.relations.BookAndGenre

@Composable
@Preview
fun BooksList(
  books: List<BookAndGenre> = emptyList()
) {
  LazyColumnFor(items = books, modifier = Modifier.padding(top = 16.dp)) { bookAndGenre ->
    BookListItem(bookAndGenre)
    Spacer(modifier = Modifier.size(2.dp))
  }
}

@Composable
fun BookListItem(
  bookAndGenre: BookAndGenre
) {
  Card(
    modifier = Modifier
      .wrapContentHeight()
      .fillMaxWidth()
      .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
    elevation = 8.dp,
    border = BorderStroke(1.dp, MaterialTheme.colors.primary),
    shape = RoundedCornerShape(16.dp)
  ) {

    Row(modifier = Modifier.fillMaxSize()) {
      Spacer(modifier = Modifier.width(16.dp))

      Column {
        Text(
          modifier = Modifier.padding(top = 16.dp),
          text = bookAndGenre.book.name,
          color = MaterialTheme.colors.primary,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          fontSize = 16.sp,
          text = bookAndGenre.genre.name,
          fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          fontSize = 12.sp,
          overflow = TextOverflow.Ellipsis,
          text = bookAndGenre.book.description,
          fontStyle = FontStyle.Italic,
          modifier = Modifier.fillMaxHeight().padding(end = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
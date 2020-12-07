package com.raywenderlich.android.librarian.ui.books.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raywenderlich.android.librarian.model.relations.BookAndGenre

@Composable
fun BooksList(
  books: List<BookAndGenre> = emptyList(),
  onLongItemTap: (BookAndGenre) -> Unit = {}
) {

  LazyColumnFor(
    items = books,
    modifier = Modifier.padding(top = 16.dp)
  ) { bookAndGenre ->
    BookListItem(bookAndGenre, onLongItemTap)
    Spacer(modifier = Modifier.size(2.dp))
  }
}

@Composable
fun BookListItem(
  bookAndGenre: BookAndGenre,
  onLongItemTap: (BookAndGenre) -> Unit
) {
  Card(
    modifier = Modifier
      .wrapContentHeight()
      .fillMaxWidth()
      .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
      .clickable(onClick = {},
        indication = null,
        onLongClick = { onLongItemTap(bookAndGenre) }),
    elevation = 8.dp,
    border = BorderStroke(1.dp, MaterialTheme.colors.primary),
    shape = RoundedCornerShape(16.dp)
  ) {

    Row(modifier = Modifier.fillMaxSize()) {
      Spacer(modifier = Modifier.width(16.dp))

      Column {
        Text(
          text = bookAndGenre.book.name,
          modifier = Modifier.padding(top = 16.dp),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = bookAndGenre.genre.name,
          fontStyle = FontStyle.Italic,
          fontSize = 16.sp,
          color = MaterialTheme.colors.onPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = bookAndGenre.book.description,
          fontStyle = FontStyle.Italic,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.fillMaxHeight().padding(end = 16.dp),
          fontSize = 12.sp,
          color = MaterialTheme.colors.onPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
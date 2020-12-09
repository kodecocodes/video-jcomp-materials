package com.raywenderlich.android.librarian.ui.readingListDetails.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
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
        onClick = { onDismiss() })
    }

    LazyColumnFor(items = books) { bookItem ->
      BookPickerItem(bookItem = bookItem, onBookSelected = onBookSelected)
    }
  } else {
    Text(
      text = stringResource(id = R.string.no_books_to_add_to_reading_list),
      modifier = with(ColumnScope) {
        Modifier
          .align(Alignment.CenterHorizontally)
          .padding(16.dp)
      },
      fontSize = 16.sp,
      fontWeight = FontWeight.SemiBold,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colors.onPrimary
    )

    ActionButton(
      enabledColor = colorResource(id = R.color.orange_200),
      modifier = with(ColumnScope) {
        Modifier
          .align(Alignment.CenterHorizontally)
          .fillMaxWidth(0.5f)
      },
      text = stringResource(id = R.string.cancel),
      onClick = { onDismiss() })

    Image(bitmap = imageResource(id = R.drawable.box_of_books))
  }
}


@Composable
fun BookPickerItem(
  bookItem: BookItem,
  onBookSelected: (BookItem) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth()
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
package com.raywenderlich.android.librarian.ui.addReview.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.relations.BookAndGenre

@Composable
fun BookPicker(
  books: List<BookAndGenre>,
  selectedBookId: String,
  onItemPicked: (BookAndGenre) -> Unit
) {
  val isBookPickerOpen = remember { mutableStateOf(false) }
  val selectedBookName = books
    .firstOrNull { it.book.id == selectedBookId }?.book?.name ?: "None"

  DropdownMenu(
    toggle = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        TextButton(
          onClick = { isBookPickerOpen.value = true },
          content = { Text(text = stringResource(id = R.string.book_picker_button_text)) })

        Text(text = selectedBookName)
      }
    },
    expanded = isBookPickerOpen.value,
    onDismissRequest = {
      isBookPickerOpen.value = false
    },
    dropdownContent = {
      for (bookAndGenre in books) {
        DropdownMenuItem(onClick = {
          onItemPicked(bookAndGenre)
          isBookPickerOpen.value = false
        }) {
          Text(text = bookAndGenre.book.name)
        }
      }
    })
}
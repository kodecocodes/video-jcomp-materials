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
  val isPickerOpen = remember { mutableStateOf(false) }
  val selectedBookName =
    books.firstOrNull { it.book.id == selectedBookId }?.book?.name ?: "None"

  DropdownMenu(
    toggle = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        TextButton(
          onClick = { isPickerOpen.value = true },
          content = { Text(text = stringResource(id = R.string.book_picker_button_text)) })

        Text(text = selectedBookName)
      }
    },
    expanded = isPickerOpen.value,
    onDismissRequest = {
      isPickerOpen.value = false
    },
    dropdownContent = {
      for (book in books) {
        DropdownMenuItem(onClick = {
          onItemPicked(book)
          isPickerOpen.value = false
        }) {
          Text(text = book.book.name)
        }
      }
    })
}
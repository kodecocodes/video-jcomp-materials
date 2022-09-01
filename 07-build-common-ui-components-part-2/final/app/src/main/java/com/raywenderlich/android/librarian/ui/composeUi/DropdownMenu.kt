package com.raywenderlich.android.librarian.ui.composeUi

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
import com.raywenderlich.android.librarian.model.Genre

@Composable
fun GenrePicker(
  genres: List<Genre>,
  selectedGenreId: String,
  onItemPicked: (Genre) -> Unit
) {
  val isGenresPickerOpen = remember { mutableStateOf(false) }
  val selectedGenreName = genres.firstOrNull { it.id == selectedGenreId }?.name
    ?: "None"

  Row(verticalAlignment = Alignment.CenterVertically) {

    TextButton(onClick = { isGenresPickerOpen.value = true },
      content = { Text(text = stringResource(id = R.string.genre_select)) })

    DropdownMenu(expanded = isGenresPickerOpen.value,
      onDismissRequest = { isGenresPickerOpen.value = false }) {
      for (genre in genres) {
        DropdownMenuItem(onClick = {
          onItemPicked(genre)
          isGenresPickerOpen.value = false
        }) {
          Text(text = genre.name)
        }
      }
    }

    Text(text = selectedGenreName)
  }
}
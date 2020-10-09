package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Genre

@Composable
fun GenrePicker(
  genres: List<Genre>,
  selectedGenreId: String,
  onItemPicked: (Genre) -> Unit
) {
  val isGenresPickerOpen = remember { mutableStateOf(false) }
  val selectedGenreName = genres.firstOrNull { it.id == selectedGenreId }?.name ?: "None"

  DropdownMenu(
    toggle = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        DropdownMenuButton(
          onClick = { isGenresPickerOpen.value = true },
          text = stringResource(id = R.string.genre_select))

        Text(text = selectedGenreName)
      }
    },
    expanded = isGenresPickerOpen.value,
    onDismissRequest = {
      isGenresPickerOpen.value = false
    },
    dropdownContent = {
      for (genre in genres) {
        DropdownMenuItem(onClick = {
          onItemPicked(genre)
          isGenresPickerOpen.value = false
        }) {
          Text(text = genre.name)
        }
      }
    })
}

@Composable
fun DropdownMenuButton(text: String, onClick: () -> Unit) {
  Button(onClick = onClick, modifier = Modifier.padding(end = 16.dp)) {
    Text(text = text, color = MaterialTheme.colors.onSecondary)
  }
}
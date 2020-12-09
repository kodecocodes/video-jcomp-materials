package com.raywenderlich.android.librarian.ui.bookReviewDetails.readingEntries

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raywenderlich.android.librarian.model.ReadingEntry
import com.raywenderlich.android.librarian.utils.formatDateToText

@Composable
fun ReadingEntries(
  readingEntries: List<ReadingEntry>,
  onItemLongClick: (ReadingEntry) -> Unit,
  content: @Composable () -> Unit
) {
  LazyColumn(
    modifier = with(ColumnScope) {
      Modifier.padding(top = 16.dp)
        .align(Alignment.CenterHorizontally)
    },
  ) {
    item { content() }

    items(items = readingEntries) { entry ->
      ReadingEntryItem(entry, onItemLongClick)
    }

    item { Spacer(modifier = Modifier.height(96.dp)) }
  }
}

@Composable
fun ReadingEntryItem(
  entry: ReadingEntry,
  onItemLongClick: (ReadingEntry) -> Unit
) {
  Card(
    elevation = 8.dp,
    border = BorderStroke(1.dp, MaterialTheme.colors.primary),
    shape = RoundedCornerShape(16.dp),
    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
  ) {
    Row(
      Modifier.fillMaxWidth()
        .clickable(
          onClick = {},
          onLongClick = { onItemLongClick(entry) },
          indication = null
        )
    ) {
      Spacer(modifier = Modifier.width(16.dp))

      Column(Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = formatDateToText(entry.dateOfEntry),
          fontStyle = FontStyle.Italic,
          color = MaterialTheme.colors.primaryVariant,
          fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = entry.comment,
          modifier = Modifier.padding(end = 16.dp),
          color = MaterialTheme.colors.onPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
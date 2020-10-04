package com.raywenderlich.android.librarian.ui.readingList.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks

@Composable
fun ReadingLists(
  readingLists: List<ReadingListsWithBooks>,
  onItemClick: (ReadingListsWithBooks) -> Unit
) {
  LazyColumnFor(items = readingLists, modifier = Modifier.fillMaxWidth()) { readingList ->
    ReadingListItem(readingList, onItemClick)
  }
}

@Composable
fun ReadingListItem(
  readingList: ReadingListsWithBooks,
  onItemClick: (ReadingListsWithBooks) -> Unit
) {
  Card(
    modifier = Modifier
      .height(75.dp)
      .fillMaxWidth()
      .padding(8.dp)
      .clickable(
        onClick = { onItemClick(readingList) },
        indication = null
      ),
    elevation = 8.dp,
    border = BorderStroke(1.dp, colorResource(id = R.color.colorPrimary)),
    shape = RoundedCornerShape(16.dp),
  ) {
    Column {
      Spacer(modifier = Modifier.height(16.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(16.dp))

        Text(
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = colorResource(id = R.color.colorPrimary),
          text = readingList.name
        )

        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = stringResource(
            id = R.string.reading_list_number_of_books,
            readingList.books.size
          ),
          color = colorResource(id = R.color.colorPrimary)
        )

        Spacer(modifier = Modifier.width(16.dp))
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

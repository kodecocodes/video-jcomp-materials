package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.relations.BookReview

@Composable
fun DeleteBookDialog(
  item: BookAndGenre,
  message: String,
  onDeleteItem: (BookAndGenre) -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    title = { Text(text = stringResource(id = R.string.delete_title)) },
    text = { Text(text = message) },
    onDismissRequest = onDismiss,
    buttons = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        DialogButton(
          text = R.string.yes,
          onClickAction = { onDeleteItem(item) }
        )

        DialogButton(text = R.string.cancel, onClickAction = onDismiss)
      }
    })
}


@Composable
fun DeleteReviewDialog(
  item: BookReview,
  message: String,
  onDeleteItem: (BookReview) -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    title = { Text(text = stringResource(id = R.string.delete_title)) },
    text = { Text(text = message) },
    onDismissRequest = onDismiss,
    buttons = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        DialogButton(
          text = R.string.yes,
          onClickAction = { onDeleteItem(item) }
        )

        DialogButton(text = R.string.cancel, onClickAction = onDismiss)
      }
    })
}
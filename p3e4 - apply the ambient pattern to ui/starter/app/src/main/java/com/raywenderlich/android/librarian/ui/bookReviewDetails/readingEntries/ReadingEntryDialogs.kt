package com.raywenderlich.android.librarian.ui.bookReviewDetails.readingEntries

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.ui.composeUi.ActionButton
import com.raywenderlich.android.librarian.ui.composeUi.InputField

@Composable
fun AddReadingEntryDialog(
  onDismiss: () -> Unit,
  onReadingEntryFinished: (String) -> Unit
) {
  val entryState = remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    val shape = RoundedCornerShape(16.dp)

    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.background(MaterialTheme.colors.surface, shape = shape)
        .border(width = 1.dp, color = MaterialTheme.colors.primary, shape = shape),
    ) {

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = stringResource(id = R.string.add_reading_entry_title),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onPrimary
      )

      Spacer(modifier = Modifier.height(8.dp))

      InputField(
        value = entryState.value,
        onStateChanged = { newValue -> entryState.value = newValue },
        isInputValid = entryState.value.isNotEmpty(),
        label = stringResource(id = R.string.reading_entry_hint)
      )

      Spacer(modifier = Modifier.height(8.dp))

      ActionButton(
        modifier = Modifier.fillMaxWidth(0.7f),
        text = stringResource(id = R.string.add_reading_entry_button_text),
        isEnabled = entryState.value.isNotEmpty(),
        onClick = { onReadingEntryFinished(entryState.value) }
      )
    }
  }
}
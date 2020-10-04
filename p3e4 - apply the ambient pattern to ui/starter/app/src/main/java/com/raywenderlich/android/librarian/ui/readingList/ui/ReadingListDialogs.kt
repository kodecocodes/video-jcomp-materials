package com.raywenderlich.android.librarian.ui.readingList.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
fun AddReadingList(
  onAddList: (String) -> Unit = {},
  onDismiss: () -> Unit = {}
) {
  val inputState = remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    val shape = RoundedCornerShape(16.dp)

    Column(
      modifier = Modifier.background(
        MaterialTheme.colors.surface,
        shape = shape
      ).border(width = 1.dp, color = MaterialTheme.colors.primary, shape = shape),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = stringResource(id = R.string.add_reading_list_title),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onPrimary
      )

      InputField(
        label = stringResource(id = R.string.reading_list_name_hint),
        value = inputState.value,
        isInputValid = inputState.value.isNotEmpty(),
        onStateChanged = { newValue -> inputState.value = newValue }
      )

      ActionButton(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.add_reading_list_button_text),
        isEnabled = inputState.value.isNotEmpty(),
        onClick = { onAddList(inputState.value) },
      )
    }
  }
}
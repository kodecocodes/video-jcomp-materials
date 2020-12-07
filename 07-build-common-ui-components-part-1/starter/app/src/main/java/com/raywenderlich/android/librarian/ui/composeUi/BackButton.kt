package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun BackButton(
  modifier: Modifier = Modifier,
  onBackAction: () -> Unit = {}
) {
  IconButton(
    modifier = modifier,
    content = {
      Icon(
        Icons.Default.ArrowBack,
        tint = Color.White
      )
    },
    onClick = { onBackAction() }
  )
}
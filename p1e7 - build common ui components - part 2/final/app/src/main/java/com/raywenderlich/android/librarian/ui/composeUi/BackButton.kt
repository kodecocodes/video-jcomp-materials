package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.foundation.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.ui.tooling.preview.Preview

@Composable
@Preview
fun BackButton(
  modifier: Modifier = Modifier,
  onBackAction: () -> Unit = {}
) {
  IconButton(
    modifier = modifier,
    icon = {
      Icon(
        Icons.Default.ArrowBack,
        tint = Color.White
      )
    },
    onClick = { onBackAction() }
  )
}
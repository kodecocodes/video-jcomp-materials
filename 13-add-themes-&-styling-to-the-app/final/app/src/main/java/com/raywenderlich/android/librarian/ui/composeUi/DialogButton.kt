package com.raywenderlich.android.librarian.ui.composeUi

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonConstants.defaultButtonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun DialogButton(
  modifier: Modifier = Modifier,
  @StringRes text: Int,
  onClickAction: () -> Unit
) {
  TextButton(
    onClick = onClickAction,
    modifier = modifier.padding(8.dp),
    colors = defaultButtonColors(
      backgroundColor = MaterialTheme.colors.primary,
      contentColor = MaterialTheme.colors.onSecondary
    )
  ) {
    Text(text = stringResource(id = text))
  }
}
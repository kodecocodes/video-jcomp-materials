package com.raywenderlich.android.librarian.ui.composeUi

import androidx.annotation.StringRes
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R

@Composable
fun DialogButton(
  modifier: Modifier = Modifier,
  @StringRes text: Int,
  onClickAction: () -> Unit
) {
  TextButton(
    modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
    backgroundColor = colorResource(id = R.color.colorPrimary),
    contentColor = Color.White,
    onClick = onClickAction
  ) {
    Text(text = stringResource(id = text))
  }
}
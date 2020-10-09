package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.ui.tooling.preview.Preview

@Composable
@Preview
fun TopBar(
  modifier: Modifier = Modifier,
  title: String = "Add a new review",
  actions: @Composable RowScope.() -> Unit = {},
  onBackPressed: (() -> Unit)? = null
) {
  val backButtonAction: (@Composable () -> Unit)? = if (onBackPressed != null) {
    @Composable { BackButton(onBackAction = { onBackPressed() }) }
  } else {
    null
  }

  TopAppBar(
    modifier = modifier,
    title = { Text(title) },
    navigationIcon = backButtonAction,
    actions = actions,
    backgroundColor = MaterialTheme.colors.primary,
    contentColor = MaterialTheme.colors.onSecondary
  )
}
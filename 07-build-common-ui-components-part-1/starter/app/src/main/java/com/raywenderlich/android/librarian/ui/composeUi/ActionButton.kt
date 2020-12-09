package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonConstants.defaultButtonColors
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R

@Composable
@Preview
fun ActionButton(
  modifier: Modifier = Modifier,
  text: String = "Librarian",
  isEnabled: Boolean = true,
  enabledColor: Color = colorResource(id = R.color.colorPrimary),
  disabledTextColor: Color = Color.Gray,
  onClick: () -> Unit = {}
) {
  val backgroundColor = if (isEnabled) enabledColor else Color.LightGray
  val contentColor = if (isEnabled) Color.White else disabledTextColor

  TextButton(
    shape = RoundedCornerShape(16.dp),
    enabled = isEnabled,
    colors = defaultButtonColors(
      backgroundColor = backgroundColor,
      contentColor = contentColor
    ),
    modifier = modifier
      .padding(16.dp),
    content = { Text(text = text) },
    onClick = onClick
  )
}
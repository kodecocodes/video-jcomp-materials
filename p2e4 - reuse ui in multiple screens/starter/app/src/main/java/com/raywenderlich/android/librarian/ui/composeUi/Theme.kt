package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColors(
  primary = Color(0xFF9557E6),
  primaryVariant = Color(0xFF4A188B),
  secondary = Color(0xFFE667AF),
  surface = Color.White,
  background = Color(0xFFF2F3F7),
  onPrimary = Color(0xFF777682),
  onSecondary = Color.White
)

private val DarkColors = darkColors(
  primary = Color(0xFF9557E6),
  primaryVariant = Color(0xFFD5BCF5),
  secondary = Color(0xFFE667AF),
  surface = Color(0xFF292A2E),
  background = Color(0xFF3E3F42),
  onPrimary = Color.White,
  onSecondary = Color.White
)

@Composable
fun LibrarianTheme(
  isDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colors = if (isDarkTheme) DarkColors else LightColors
  ) {
    content()
  }
}
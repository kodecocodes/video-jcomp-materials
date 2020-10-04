package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.raywenderlich.android.librarian.R

@Composable
@Preview
fun InputField(
  modifier: Modifier = Modifier,
  value: String = "",
  label: String = stringResource(id = R.string.app_name),
  keyboardType: KeyboardType = KeyboardType.Text,
  isInputValid: Boolean = true,
  imeAction: ImeAction = ImeAction.Next,
  onStateChanged: (String) -> Unit = {}
) {
  OutlinedTextField(
    value = value,
    onValueChange = { newValue -> onStateChanged(newValue) },
    label = { Text(label) },
    imeAction = imeAction,
    modifier = modifier
      .fillMaxWidth()
      .padding(
        start = 16.dp,
        end = 16.dp,
        top = 4.dp,
        bottom = 4.dp
      ),
    activeColor = MaterialTheme.colors.primary,
    inactiveColor = MaterialTheme.colors.primaryVariant,
    keyboardType = keyboardType,
    visualTransformation = getVisualTransformation(keyboardType),
    isErrorValue = !isInputValid
  )
}

private fun getVisualTransformation(keyboardType: KeyboardType) =
  if (keyboardType == KeyboardType.Password || keyboardType == KeyboardType.NumberPassword)
    PasswordVisualTransformation()
  else VisualTransformation.None
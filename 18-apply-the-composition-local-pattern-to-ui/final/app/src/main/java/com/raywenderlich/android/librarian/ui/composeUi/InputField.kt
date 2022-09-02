/*
 * Copyright (c) 2022 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R

@Composable
fun InputField(
  modifier: Modifier = Modifier,
  value: String = "",
  label: String,
  keyboardType: KeyboardType = KeyboardType.Text,
  isInputValid: Boolean = true,
  imeAction: ImeAction = ImeAction.Next,
  onStateChanged: (String) -> Unit
) {
  val focusedColor = MaterialTheme.colors.primary
  val unfocusedColor = MaterialTheme.colors.primaryVariant

  OutlinedTextField(
    value = value,
    onValueChange = onStateChanged,
    label = { Text(text = label) },
    modifier = modifier
      .fillMaxWidth()
      .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
    keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
    visualTransformation = getVisualTransformation(keyboardType),
    isError = !isInputValid,
    colors = TextFieldDefaults.textFieldColors(
      focusedIndicatorColor = focusedColor,
      focusedLabelColor = focusedColor,
      unfocusedIndicatorColor = unfocusedColor,
      unfocusedLabelColor = unfocusedColor,
      cursorColor = focusedColor,
      backgroundColor = MaterialTheme.colors.background
    )
  )
}

private fun getVisualTransformation(keyboardType: KeyboardType) =
  if (keyboardType == KeyboardType.Password || keyboardType == KeyboardType.NumberPassword)
    PasswordVisualTransformation()
  else VisualTransformation.None
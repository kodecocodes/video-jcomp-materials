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

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R

@Composable
fun <T> SpinnerPicker(
  pickerText: String,
  preselectedItem: T? = null,
  items: List<T>,
  itemToName: (T) -> String,
  onItemPicked: (T) -> Unit
) {
  val isPickerExpanded = remember { mutableStateOf(false) }
  val pickedItem = remember { mutableStateOf(preselectedItem?.let(itemToName) ?: "") }

  Row(modifier = Modifier
    .wrapContentWidth()
    .padding(start = 16.dp, end = 16.dp),
    verticalAlignment = Alignment.CenterVertically) {
    DropdownMenuButton(text = pickerText) {
      isPickerExpanded.value = true
    }

    DropdownMenu(
      modifier = Modifier
        .height(300.dp)
        .width(200.dp),
      expanded = isPickerExpanded.value,
      onDismissRequest = { isPickerExpanded.value = false }) {
      for (item in items) {
        DropdownMenuItem(onClick = {
          onItemPicked(item)
          isPickerExpanded.value = false
          pickedItem.value = itemToName(item)
        }) {
          Text(text = itemToName(item))
        }
      }
    }

    val currentlySelected = if (pickedItem.value.isEmpty()) "None" else pickedItem.value

    Text(
      text = stringResource(id = R.string.current_selection, currentlySelected),
      color = MaterialTheme.colors.onPrimary
    )
  }
}


@Composable
fun DropdownMenuButton(text: String, onClick: () -> Unit) {
  Button(onClick = onClick, modifier = Modifier.padding(end = 16.dp)) {
    Text(text = text, color = MaterialTheme.colors.onSecondary)
  }
}
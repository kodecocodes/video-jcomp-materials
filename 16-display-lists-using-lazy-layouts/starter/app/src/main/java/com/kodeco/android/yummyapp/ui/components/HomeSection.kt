/*
 * Copyright (c) 2023 Razeware LLC
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

package com.kodeco.android.yummyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kodeco.android.yummyapp.R
import com.kodeco.android.yummyapp.data.getCategories
import com.kodeco.android.yummyapp.data.getFood

@Preview
@Composable
fun HomeSection() {
  Surface(modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .padding(horizontal = 8.dp),
    ) {
      // Categories header
      Row(
          modifier = Modifier
              .fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(text = "Food Category")
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
          Text(
              text = "See more",
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold,
          )

          Spacer(modifier = Modifier.width(10.dp))

          Image(
              painter = painterResource(id = R.drawable.ic_right),
              contentDescription = "Right",
              colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
          )
        }
      }

      // TODO: Add Categories list


      Spacer(modifier = Modifier
          .fillMaxWidth()
          .height(32.dp))

      Row(
          modifier = Modifier
              .fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween) {

        Text(text = "Food For You")

        Row(modifier = Modifier
            .width(40.dp)
            .align(Alignment.CenterVertically),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

          Surface(modifier = Modifier
              .size(10.dp),
              shape = CircleShape,
              color = MaterialTheme.colorScheme.primary) {

          }

          Surface(modifier = Modifier
              .size(10.dp),
              shape = CircleShape,
              color = MaterialTheme.colorScheme.onBackground) {

          }

          Surface(modifier = Modifier
              .size(10.dp),
              shape = CircleShape,
              color = MaterialTheme.colorScheme.onBackground) {

          }
        }

      }

      Spacer(modifier = Modifier
          .fillMaxWidth()
          .height(8.dp))

      // TODO: Food Items


    }
  }
}
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

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kodeco.android.yummyapp.R
import com.kodeco.android.yummyapp.data.Food
import com.kodeco.android.yummyapp.data.getFood

@Preview
@ExperimentalMaterial3Api
@Composable
fun FoodItem(
    food: Food = getFood()[1]
) {

  Card(
      onClick ={},
      modifier = Modifier
          .width(200.dp)
          .padding(end = 8.dp),
      shape = RoundedCornerShape(corner = CornerSize(10.dp))) {

    Image(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        painter = painterResource(id = food.banner),
        contentDescription = food.name,
        contentScale = ContentScale.Crop
    )

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp))

    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(bottom = 5.dp)
            .fillMaxWidth()) {

      Text(
          modifier = Modifier,
          text = food.name,
          style = MaterialTheme.typography.bodyLarge,
          fontStyle = FontStyle.Normal,
          fontWeight = FontWeight.ExtraBold)

      Row(
          modifier = Modifier
              .padding(top = 5.dp),
          verticalAlignment = Alignment.CenterVertically) {

        Image(
            painter = painterResource(id = R.drawable.ic_clock),
            contentDescription = "Clock",
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Text(
            modifier = Modifier.padding(start = 3.dp),
            text = "${food.waitTime} mins",
            style = MaterialTheme.typography.bodySmall)

      }

      Spacer(modifier = Modifier
          .height(5.dp))


      Row(
          modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically) {


        Text(
            text = "$${food.originalPrice}",
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.bodyLarge
        )

        FloatingActionButton(
            onClick = { /*TODO*/ },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.tertiary
        ) {

          Icon(
              painter = painterResource(id = R.drawable.ic_add),
              contentDescription = "Add")

        }

      }



    }

  }

}
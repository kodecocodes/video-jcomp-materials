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

package com.kodeco.android.yummyapp.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kodeco.android.yummyapp.R
import com.kodeco.android.yummyapp.data.getFood
import com.kodeco.android.yummyapp.ui.components.FoodDetailsMeasure
import com.kodeco.android.yummyapp.ui.components.PriceBar

@Preview
@Composable
@ExperimentalMaterial3Api
fun FoodDetails(
    foodId: Int? = 2
) {

  val food = getFood()[foodId?:0]

  Box(
      modifier = Modifier
          .fillMaxSize()
          .background(color = Color.White)){

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .paint(
            painter = painterResource(id = food.banner),
            contentScale = ContentScale.FillBounds))

    TopNavItem(modifier = Modifier
        .align(Alignment.TopStart)
        .padding(top = 32.dp, start = 16.dp), icon = R.drawable.ic_back)


    TopNavItem(modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(top = 32.dp, end = 16.dp), icon = R.drawable.ic_favorite)


    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 300.dp),
        shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
    ) {


      Column(
          modifier = Modifier
              .padding(top = 110.dp, start = 16.dp, end = 16.dp),) {

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = food.name,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.ExtraBold)

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {

          FoodDetailsMeasure(
              icon = R.drawable.ic_clock,
              text = "${food.waitTime} mins",
              iconColor = MaterialTheme.colorScheme.tertiary)

          Divider()

          FoodDetailsMeasure(
              icon = R.drawable.ic_cal,
              text = "${food.calories} Kal",
              iconColor = MaterialTheme.colorScheme.primary)

          Divider()

          FoodDetailsMeasure(
              icon = R.drawable.ic_star,
              text = "${food.rating} / 5",
              iconColor = MaterialTheme.colorScheme.error)


        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.onBackground)
        )


        // price and qty
        PriceBar(price = food.price, originalPrice = food.originalPrice)

        Text(
            text = "About Food",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 50.dp))

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = food.description,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium)

      }

    }

    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(horizontal = 64.dp)
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(50.dp)
    ) {

      Text(text = "Add to order $${(food.price?:food.originalPrice)} each", modifier = Modifier)

    }

    Surface(
        modifier = Modifier
            .padding(top = 150.dp)
            .size(250.dp)
            .background(color = MaterialTheme.colorScheme.tertiary, shape = CircleShape)
            .shadow(elevation = 10.dp, shape = CircleShape)
            .border(
                width = 4.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .clip(CircleShape)
            .align(Alignment.TopCenter)
    ) {

      Image(
          painter = painterResource(id = food.banner),
          contentDescription = "",
          contentScale = ContentScale.Crop)

    }

  }


}

@Composable
fun TopNavItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int =  R.drawable.ic_cart
) {

  Box(
      modifier = modifier
          .size(50.dp)
          .clip(CircleShape)
          .background(color = MaterialTheme.colorScheme.background)) {

    Icon(
        modifier = Modifier.align(Alignment.Center),
        painter = painterResource(id = icon),
        contentDescription = "nav",
        tint = MaterialTheme.colorScheme.onBackground
    )

  }

}

@Composable
fun Divider() {
  Box(modifier = Modifier
      .size(8.dp)
      .clip(CircleShape)
      .border(
          width = 2.dp,
          color = MaterialTheme.colorScheme.onBackground,
          shape = CircleShape
      ))
}
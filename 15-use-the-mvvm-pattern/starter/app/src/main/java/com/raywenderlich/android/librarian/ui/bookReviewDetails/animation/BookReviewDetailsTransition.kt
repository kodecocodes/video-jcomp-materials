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

package com.raywenderlich.android.librarian.ui.bookReviewDetails.animation

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun animateBookReviewDetails(
  screenState: BookReviewDetailsScreenState
): BookReviewDetailsTransitionState {
  val transition = updateTransition(targetState = screenState, label = "bookReviewDetailsEntry")

  val imageMarginTop by transition.animateDp(
    transitionSpec = { tween(durationMillis = 1000) }, label = "imageMargin"
  ) { target -> if (target == Loaded) 16.dp else 125.dp }

  val floatingButtonSize by transition.animateDp(
    transitionSpec = { tween(durationMillis = 1000) }, label = "FABSize"
  ) { target -> if (target == Loaded) 56.dp else 0.dp }

  val titleMarginTop by transition.animateDp(
    transitionSpec = { tween(durationMillis = 1000) }, label = "titleMargin"
  ) { target -> if (target == Loaded) 16.dp else 75.dp }

  val contentMarginTop by transition.animateDp(
    transitionSpec = { tween(durationMillis = 1000) }, label = "contentMargin"
  ) { target -> if (target == Loaded) 6.dp else 50.dp }

  val contentAlpha by transition.animateFloat(
    transitionSpec = { tween(durationMillis = 1000) }, label = "contentAlpha"
  ) { target -> if (target == Loaded) 1f else 0.3f }

  val state = remember { BookReviewDetailsTransitionState() }

  state.apply {
    this.imageMarginTop = imageMarginTop
    this.floatingButtonSize = floatingButtonSize
    this.titleMarginTop = titleMarginTop
    this.contentMarginTop = contentMarginTop
    this.contentAlpha = contentAlpha
  }

  return state
}

class BookReviewDetailsTransitionState {

  var imageMarginTop: Dp by mutableStateOf(0.dp)
  var floatingButtonSize: Dp by mutableStateOf(0.dp)
  var titleMarginTop: Dp by mutableStateOf(0.dp)
  var contentMarginTop: Dp by mutableStateOf(0.dp)

  var contentAlpha: Float by mutableStateOf(0f)
}
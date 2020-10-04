package com.raywenderlich.android.librarian.ui.bookReviewDetails.animation

import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.dp

val transitionDefinition = transitionDefinition<Boolean> {

  state(false) {
    this[imageMarginTop] = 125.dp
    this[floatingButtonSize] = 0.dp
    this[titleMarginTop] = 75.dp
    this[contentMarginTop] = 50.dp
    this[contentAlpha] = 0.3f
  }

  state(true) {
    this[imageMarginTop] = 16.dp
    this[floatingButtonSize] = 56.dp
    this[titleMarginTop] = 16.dp
    this[contentMarginTop] = 6.dp
    this[contentAlpha] = 1f
  }

  transition(fromState = false, toState = true) {
    imageMarginTop using tween(durationMillis = 1000)

    floatingButtonSize using tween(durationMillis = 1500)
    titleMarginTop using tween(durationMillis = 1500)
    contentMarginTop using tween(durationMillis = 1500)
    contentAlpha using tween(durationMillis = 1500)
  }
}
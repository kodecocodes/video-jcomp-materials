package com.raywenderlich.android.librarian.model.state

data class AddBookState(
  val name: String = "",
  val description: String = "",
  val genreId: String = ""
)
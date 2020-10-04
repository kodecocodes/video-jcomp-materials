package com.raywenderlich.android.librarian.model.state

import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.utils.EMPTY_BOOK_AND_GENRE

data class AddBookReviewState(
  val bookAndGenre: BookAndGenre = EMPTY_BOOK_AND_GENRE,
  val bookImageUrl: String = "",
  val rating: Int = 0,
  val notes: String = ""
)
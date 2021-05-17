package com.raywenderlich.android.librarian.utils

import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.relations.BookReview
import java.util.*

val EMPTY_BOOK = Book("", "", "", "")
val EMPTY_GENRE = Genre("", "")
val EMPTY_BOOK_AND_GENRE = BookAndGenre(EMPTY_BOOK, EMPTY_GENRE)

val EMPTY_REVIEW = Review("", "", 0, "", "", Date(), emptyList())
val EMPTY_BOOK_REVIEW = BookReview(EMPTY_REVIEW, EMPTY_BOOK)
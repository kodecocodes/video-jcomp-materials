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

package com.raywenderlich.android.librarian.repository

import com.raywenderlich.android.librarian.database.dao.BookDao
import com.raywenderlich.android.librarian.database.dao.GenreDao
import com.raywenderlich.android.librarian.database.dao.ReadingListDao
import com.raywenderlich.android.librarian.database.dao.ReviewDao
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LibrarianRepositoryImpl @Inject constructor(
  private val bookDao: BookDao,
  private val genreDao: GenreDao,
  private val reviewDao: ReviewDao,
  private val readingListDao: ReadingListDao
) : LibrarianRepository {

  override suspend fun addBook(book: Book) = bookDao.addBook(book)

  override suspend fun removeBook(book: Book) = bookDao.removeBook(book)

  override suspend fun getBooks(): List<BookAndGenre> = bookDao.getBooks()

  override fun getBooksFlow(): Flow<List<BookAndGenre>> = bookDao.getBooksFlow()

  override suspend fun getBookById(bookId: String): BookAndGenre = bookDao.getBookById(bookId)

  override suspend fun getBooksByGenre(genreId: String): List<BookAndGenre> =
    genreDao.getBooksByGenre(genreId).let { booksByGenre ->
      val books = booksByGenre.books ?: return emptyList()

      return books.map { BookAndGenre(it, booksByGenre.genre) }
    }

  override suspend fun getGenres(): List<Genre> = genreDao.getGenres()

  override suspend fun getGenreById(genreId: String): Genre = genreDao.getGenreById(genreId)

  override suspend fun addGenres(genres: List<Genre>) = genreDao.addGenres(genres)

  override suspend fun getReviews(): List<BookReview> = reviewDao.getReviews()

  override fun getReviewsFlow(): Flow<List<BookReview>> = reviewDao.getReviewsFlow()

  override suspend fun getBooksByRating(rating: Int): List<BookAndGenre> {
    val reviewsByRating = reviewDao.getReviewsByRating(rating)

    return reviewsByRating.map { BookAndGenre(it.book, genreDao.getGenreById(it.book.genreId)) }
  }

  override suspend fun getReviewById(reviewId: String): BookReview =
    reviewDao.getReviewsById(reviewId)

  override suspend fun addReview(review: Review) = reviewDao.addReview(review)

  override suspend fun removeReview(review: Review) = reviewDao.removeReview(review)

  override suspend fun removeReviews(reviews: List<Review>) = reviewDao.removeReviews(reviews)

  override suspend fun updateReview(review: Review) = reviewDao.updateReview(review)

  override suspend fun getReadingLists(): List<ReadingListsWithBooks> =
    readingListDao.getReadingLists().map {
      ReadingListsWithBooks(it.id, it.name, it.bookIds.map { bookId -> getBookById(bookId) })
    }

  override fun getReadingListsFlow(): Flow<List<ReadingListsWithBooks>> =
    readingListDao.getReadingListsFlow()
      .map { items ->
        items.map { readingList ->
          ReadingListsWithBooks(readingList.id, readingList.name,
            readingList.bookIds.map { bookId ->
              getBookById(bookId)
            })
        }
      }

  override suspend fun removeReadingList(readingList: ReadingList) =
    readingListDao.removeReadingList(readingList)

  override suspend fun updateReadingList(readingList: ReadingList) =
    readingListDao.updateReadingList(readingList)

  override suspend fun removeReadingLists(readingLists: List<ReadingList>) =
    readingListDao.removeReadingLists(readingLists)

  override suspend fun addReadingList(readingList: ReadingList) =
    readingListDao.addReadingList(readingList)

  override fun getReadingListById(id: String): Flow<ReadingListsWithBooks> {
    val readingList = readingListDao.getReadingListById(id)

    return readingList.map {
      ReadingListsWithBooks(it.id, it.name, it.bookIds.map { bookId -> getBookById(bookId) })
    }
  }

  override suspend fun getReviewsForBook(
    bookId: String
  ): List<Review> = reviewDao.getReviewsForBook(bookId)
}
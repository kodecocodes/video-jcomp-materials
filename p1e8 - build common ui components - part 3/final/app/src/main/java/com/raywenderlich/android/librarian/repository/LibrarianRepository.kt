/*
 * Copyright (c) 2020 Razeware LLC
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

import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import kotlinx.coroutines.flow.Flow

interface LibrarianRepository {

  suspend fun addBook(book: Book)

  suspend fun removeBook(book: Book)

  suspend fun getBooks(): List<BookAndGenre>

  fun getBooksFlow(): Flow<List<BookAndGenre>>

  suspend fun getBookById(bookId: String): BookAndGenre

  suspend fun getBooksByGenre(genreId: String): List<BookAndGenre>

  suspend fun getGenres(): List<Genre>

  suspend fun getGenreById(genreId: String): Genre

  suspend fun addGenres(genres: List<Genre>)

  suspend fun getReviews(): List<BookReview>

  fun getReviewsFlow(): Flow<List<BookReview>>

  suspend fun getBooksByRating(rating: Int): List<BookAndGenre>

  suspend fun getReviewById(reviewId: String): BookReview

  suspend fun addReview(review: Review)

  suspend fun updateReview(review: Review)

  suspend fun removeReview(review: Review)

  suspend fun removeReviews(reviews: List<Review>)

  suspend fun getReviewsForBook(bookId: String): List<Review>

  suspend fun getReadingLists(): List<ReadingListsWithBooks>

  fun getReadingListsFlow(): Flow<List<ReadingListsWithBooks>>

  suspend fun removeReadingList(readingList: ReadingList)

  suspend fun updateReadingList(readingList: ReadingList)

  suspend fun removeReadingLists(readingLists: List<ReadingList>)

  suspend fun addReadingList(readingList: ReadingList)

  fun getReadingListById(id: String): Flow<ReadingListsWithBooks>
}
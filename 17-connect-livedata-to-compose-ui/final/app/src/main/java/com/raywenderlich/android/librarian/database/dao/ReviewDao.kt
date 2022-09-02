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

package com.raywenderlich.android.librarian.database.dao

import androidx.room.*
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookReview
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

  @Query("SELECT * FROM reviews")
  suspend fun getReviews(): List<BookReview>

  @Transaction
  @Query("SELECT * FROM reviews")
  fun getReviewsFlow(): Flow<List<BookReview>>

  @Query("SELECT * FROM reviews WHERE rating >= :rating")
  suspend fun getReviewsByRating(rating: Int): List<BookReview>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addReview(review: Review)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  suspend fun updateReview(review: Review)

  @Delete
  suspend fun removeReview(review: Review)

  @Delete
  suspend fun removeReviews(review: List<Review>)

  @Query("SELECT * FROM reviews WHERE bookId = :bookId")
  suspend fun getReviewsForBook(bookId: String): List<Review>

  @Query("SELECT * FROM reviews WHERE id = :reviewId")
  suspend fun getReviewsById(reviewId: String): BookReview
}
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

package com.raywenderlich.android.librarian.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.raywenderlich.android.librarian.database.converters.BookIdsConverter
import com.raywenderlich.android.librarian.database.converters.DateConverter
import com.raywenderlich.android.librarian.database.converters.ReadingEntryConverter
import com.raywenderlich.android.librarian.database.dao.BookDao
import com.raywenderlich.android.librarian.database.dao.GenreDao
import com.raywenderlich.android.librarian.database.dao.ReadingListDao
import com.raywenderlich.android.librarian.database.dao.ReviewDao
import com.raywenderlich.android.librarian.database.migration.migration_1_2
import com.raywenderlich.android.librarian.database.migration.migration_2_3
import com.raywenderlich.android.librarian.database.migration.migration_3_4
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.Review

const val DATABASE_VERSION = 4

@Database(
  entities = [Book::class, Genre::class, ReadingList::class, Review::class],
  version = DATABASE_VERSION
)
@TypeConverters(DateConverter::class, ReadingEntryConverter::class, BookIdsConverter::class)
abstract class LibrarianDatabase : RoomDatabase() {

  companion object {
    private const val DATABASE_NAME = "Librarian"

    fun buildDatabase(context: Context): LibrarianDatabase {
      return Room.databaseBuilder(
        context,
        LibrarianDatabase::class.java,
        DATABASE_NAME
      ).addMigrations(migration_1_2, migration_2_3, migration_3_4)
        .build()
    }
  }

  abstract fun bookDao(): BookDao

  abstract fun genreDao(): GenreDao

  abstract fun readingListDao(): ReadingListDao

  abstract fun reviewDao(): ReviewDao
}
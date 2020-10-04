package com.raywenderlich.android.librarian.di

import android.content.Context
import com.raywenderlich.android.librarian.database.LibrarianDatabase
import com.raywenderlich.android.librarian.database.dao.BookDao
import com.raywenderlich.android.librarian.database.dao.GenreDao
import com.raywenderlich.android.librarian.database.dao.ReadingListDao
import com.raywenderlich.android.librarian.database.dao.ReviewDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

  @Provides
  @Singleton
  fun librarianDatabase(@ApplicationContext context: Context): LibrarianDatabase {
    return LibrarianDatabase.buildDatabase(context)
  }

  @Provides
  fun bookDao(database: LibrarianDatabase): BookDao = database.bookDao()

  @Provides
  fun reviewDao(database: LibrarianDatabase): ReviewDao = database.reviewDao()

  @Provides
  fun genreDao(database: LibrarianDatabase): GenreDao = database.genreDao()

  @Provides
  fun readingListDao(database: LibrarianDatabase): ReadingListDao = database.readingListDao()
}
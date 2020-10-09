package com.raywenderlich.android.librarian.di

import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.repository.LibrarianRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {

  @Binds
  @Singleton
  abstract fun bindRepository(repositoryImpl: LibrarianRepositoryImpl): LibrarianRepository
}
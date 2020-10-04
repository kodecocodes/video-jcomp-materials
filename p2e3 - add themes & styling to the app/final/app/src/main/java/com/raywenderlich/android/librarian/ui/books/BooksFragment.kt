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

package com.raywenderlich.android.librarian.ui.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.ColumnScope.align
import androidx.compose.foundation.layout.RowScope.align
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.books.filter.ByGenre
import com.raywenderlich.android.librarian.ui.books.filter.ByRating
import com.raywenderlich.android.librarian.ui.books.filter.Filter
import com.raywenderlich.android.librarian.ui.books.ui.BookFilter
import com.raywenderlich.android.librarian.ui.books.ui.BooksList
import com.raywenderlich.android.librarian.ui.composeUi.DeleteBookDialog
import com.raywenderlich.android.librarian.ui.composeUi.LibrarianTheme
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val REQUEST_CODE_ADD_BOOK = 201

@AndroidEntryPoint
class BooksFragment : Fragment() {

  @Inject
  lateinit var repository: LibrarianRepository

  private val _booksState = mutableStateOf(emptyList<BookAndGenre>())
  private val _genresState = mutableStateOf<List<Genre>>(emptyList())
  private val _deleteBookState = mutableStateOf<BookAndGenre?>(null)
  var filter: Filter? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return ComposeView(requireContext()).apply {
      setContent {
        LibrarianTheme {
          BooksContent()
        }
      }
    }
  }

  @Composable
  fun BooksContent() {
    val bookFilterDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)

    Scaffold(topBar = { BooksTopBar(bookFilterDrawerState) },
      floatingActionButton = { AddNewBook(bookFilterDrawerState) }) {
      BookFilterModalDrawer(bookFilterDrawerState)
    }
  }

  @Composable
  fun BooksTopBar(bookFilterDrawerState: BottomDrawerState) {
    TopBar(
      title = stringResource(id = R.string.my_books_title),
      actions = { FilterButton(bookFilterDrawerState) })
  }

  @Composable
  fun FilterButton(bookFilterDrawerState: BottomDrawerState) {
    IconButton(onClick = {
      if (!bookFilterDrawerState.isClosed) {
        bookFilterDrawerState.close()
      } else {
        bookFilterDrawerState.expand()
      }
    }) {
      Icon(Icons.Default.Edit, tint = Color.White)
    }
  }

  @Composable
  fun BookFilterModalDrawer(bookFilterDrawerState: BottomDrawerState) {
    val books = _booksState.value

    BottomDrawerLayout(
      drawerContent = { BookFilterModalDrawerContent(bookFilterDrawerState) },
      drawerState = bookFilterDrawerState,
      bodyContent = {
        Stack(
          modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .align(Alignment.CenterVertically)
            .fillMaxSize()
        ) {
          BooksList(books, onItemLongTap = { bookAndGenre ->
            _deleteBookState.value = bookAndGenre
          })

          val bookToDelete = _deleteBookState.value

          if (bookToDelete != null) {
            DeleteBookDialog(
              item = bookToDelete,
              message = stringResource(id = R.string.delete_message, bookToDelete.book.name),
              onDeleteItem = {
                removeBook(it.book)
                _deleteBookState.value = null
              },
              onDismiss = {
                _deleteBookState.value = null
              }
            )
          }
        }
      })
  }

  @Composable
  fun BookFilterModalDrawerContent(bookFilterDrawerState: BottomDrawerState) {
    val genres = _genresState.value

    BookFilter(filter, genres, onFilterSelected = {
      bookFilterDrawerState.close()
      this.filter = it
      loadBooks()
    })
  }

  @Composable
  fun AddNewBook(bookFilterDrawerState: BottomDrawerState) {
    FloatingActionButton(
      icon = { Icon(Icons.Filled.Add) },
      onClick = {
        bookFilterDrawerState.close { showAddBook() }
      },
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    loadGenres()
    loadBooks()
  }

  fun loadGenres() {
    lifecycleScope.launch {
      val genres = repository.getGenres()

      _genresState.value = genres
    }
  }

  fun loadBooks() {
    lifecycleScope.launch {

      val books = when (val currentFilter = filter) {
        is ByGenre -> repository.getBooksByGenre(currentFilter.genreId)
        is ByRating -> repository.getBooksByRating(currentFilter.rating)
        else -> repository.getBooks()
      }

      _booksState.value = books
    }
  }

  fun removeBook(book: Book) {
    lifecycleScope.launch {
      repository.removeBook(book)
      loadBooks()
    }
  }

  private fun showAddBook() {
    val addBook = registerForActivityResult(AddBookContract()) { isBookCreated ->
      if (isBookCreated) {
        loadBooks()
        activity?.toast("Book added!")
      }
    }

    addBook.launch(REQUEST_CODE_ADD_BOOK)
  }
}
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.ui.books.ui.BookFilter
import com.raywenderlich.android.librarian.ui.books.ui.BooksList
import com.raywenderlich.android.librarian.ui.composeUi.DeleteDialog
import com.raywenderlich.android.librarian.ui.composeUi.LibrarianTheme
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.utils.toast
import dagger.hilt.android.AndroidEntryPoint


private const val REQUEST_CODE_ADD_BOOK = 201

@AndroidEntryPoint
class BooksFragment : Fragment() {

  private val booksViewModel by viewModels<BooksViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setContent {
        LibrarianTheme {
          BooksContent()
        }
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    booksViewModel.loadGenres()
    booksViewModel.loadBooks()
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
      Icon(Icons.Default.Edit, tint = MaterialTheme.colors.onSecondary)
    }
  }

  @Composable
  fun BookFilterModalDrawer(bookFilterDrawerState: BottomDrawerState) {
    val books by booksViewModel.booksState.observeAsState(emptyList())
    val deleteDialogBook by booksViewModel.deleteBookState.observeAsState()

    val bookToDelete = deleteDialogBook

    BottomDrawerLayout(
      drawerContent = { BookFilterModalDrawerContent(bookFilterDrawerState) },
      drawerState = bookFilterDrawerState,
      bodyContent = {

        Box(modifier = with(BoxScope) {
          Modifier
            .align(Alignment.Center)
            .fillMaxSize()
        }) {
          BooksList(books, onLongItemTap = { booksViewModel.showDeleteBook(it) })


          if (bookToDelete != null) {
            DeleteDialog(
              item = bookToDelete,
              message = stringResource(id = R.string.delete_message, bookToDelete.book.name),
              onDeleteItem = {
                booksViewModel.removeBook(it.book)
                booksViewModel.cancelDeleteBook()
              },
              onDismiss = { booksViewModel.cancelDeleteBook() }
            )
          }
        }
      })
  }

  @Composable
  fun BookFilterModalDrawerContent(
    bookFilterDrawerState: BottomDrawerState
  ) {
    val genres by booksViewModel.genresState.observeAsState(emptyList())
    val filter = booksViewModel.filter

    BookFilter(filter, genres, onFilterSelected = {
      bookFilterDrawerState.close()
      booksViewModel.filter = it
      booksViewModel.loadBooks()
    })
  }

  @Composable
  fun AddNewBook(bookFilterDrawerState: BottomDrawerState) {
    FloatingActionButton(
      content = { Icon(Icons.Filled.Add) },
      onClick = {
        bookFilterDrawerState.close { showAddBook() }
      },
    )
  }

  private fun showAddBook() {
    val addBook = registerForActivityResult(AddBookContract()) { isBookCreated ->
      if (isBookCreated) {
        booksViewModel.loadBooks()
        activity?.toast("Book added!")
      }
    }

    addBook.launch(REQUEST_CODE_ADD_BOOK)
  }
}
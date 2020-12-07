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

package com.raywenderlich.android.librarian.ui.readingListDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.BookItem
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.books.ui.BooksList
import com.raywenderlich.android.librarian.ui.composeUi.DeleteDialog
import com.raywenderlich.android.librarian.ui.composeUi.LibrarianTheme
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.ui.readingListDetails.ui.BookPicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReadingListDetailsActivity : AppCompatActivity() {

  @Inject
  lateinit var repository: LibrarianRepository

  private val _addBookState = mutableStateOf<List<BookItem>>(emptyList())
  private val readingListState = mutableStateOf<ReadingListsWithBooks?>(null)
  private val _deleteBookState = mutableStateOf<BookAndGenre?>(null)

  companion object {
    private const val KEY_READING_LIST = "reading_list"

    fun getIntent(context: Context, readingList: ReadingListsWithBooks): Intent {
      val intent = Intent(context, ReadingListDetailsActivity::class.java)

      intent.putExtra(KEY_READING_LIST, readingList)
      return intent
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val readingList = intent.getParcelableExtra<ReadingListsWithBooks>(KEY_READING_LIST)

    if (readingList != null) {
      setReadingList(readingList)
    } else {
      finish()
      return
    }

    setContent {
      LibrarianTheme {
        ReadingListDetailsContent()
      }
    }
  }

  @Composable
  fun ReadingListDetailsContent() {
    val bottomDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)

    Scaffold(
      topBar = { ReadingListDetailsTopBar(readingListState.value) },
      floatingActionButton = { AddBookToReadingList(bottomDrawerState) }
    ) {
      ReadingListDetailsModalDrawer(bottomDrawerState, readingListState.value)
    }
  }

  @Composable
  fun AddBookToReadingList(bottomDrawerState: BottomDrawerState) {
    FloatingActionButton(onClick = {

      if (bottomDrawerState.isClosed) {
        refreshBooks()
        bottomDrawerState.expand()
      }
    }) {
      Icon(imageVector = Icons.Default.Add, tint = MaterialTheme.colors.onSecondary)
    }
  }

  @Composable
  fun ReadingListDetailsTopBar(readingList: ReadingListsWithBooks?) {
    val title = readingList?.name ?: stringResource(id = R.string.reading_list)

    TopBar(title = title, onBackPressed = { onBackPressed() })
  }

  @Composable
  fun ReadingListDetailsModalDrawer(
    drawerState: BottomDrawerState,
    readingList: ReadingListsWithBooks?
  ) {
    val bookToDelete = _deleteBookState.value

    BottomDrawerLayout(
      drawerState = drawerState,
      gesturesEnabled = false,
      drawerContent = {
        ReadingListDetailsModalDrawerContent(
          drawerState,
          _addBookState.value
        )
      }) {
      Box(
        modifier = with(BoxScope) {
          Modifier
            .align(Alignment.Center)
            .fillMaxSize()
        }
      ) {
        BooksList(
          readingList?.books ?: emptyList(),
          onLongItemTap = { book -> _deleteBookState.value = book }
        )

        if (bookToDelete != null) {
          DeleteDialog(
            item = bookToDelete,
            message = stringResource(id = R.string.delete_message, bookToDelete.book.name),
            onDeleteItem = {
              removeBookFromReadingList(it.book.id)
              _deleteBookState.value = null
            },
            onDismiss = { _deleteBookState.value = null }
          )
        }
      }
    }
  }

  @Composable
  fun ReadingListDetailsModalDrawerContent(
    drawerState: BottomDrawerState,
    addBookState: List<BookItem>
  ) {
    BookPicker(
      books = addBookState,
      onBookSelected = { bookPickerItemSelected(it) },
      onBookPicked = {
        addBookToReadingList(addBookState.firstOrNull { it.isSelected }?.bookId)

        drawerState.close()
      }, onDismiss = { drawerState.close() })
  }

  fun setReadingList(readingListsWithBooks: ReadingListsWithBooks) {
    lifecycleScope.launch {
      readingListState.value = repository.getReadingListById(readingListsWithBooks.id)
    }

    refreshBooks()
  }

  fun refreshBooks() {
    lifecycleScope.launch {
      val books = repository.getBooks()
      val readingListBooks = readingListState.value?.books?.map { it.book.id } ?: emptyList()

      val freshBooks = books.filter { it.book.id !in readingListBooks }

      _addBookState.value = freshBooks.map { BookItem(it.book.id, it.book.name, false) }
    }
  }

  fun addBookToReadingList(bookId: String?) {
    val data = readingListState.value

    if (data != null && bookId != null) {
      val bookIds = (data.books.map { it.book.id } + bookId).distinct()

      val newReadingList = ReadingList(
        data.id,
        data.name,
        bookIds
      )

      updateReadingList(newReadingList)
    }
  }

  fun removeBookFromReadingList(bookId: String) {
    val data = readingListState.value

    if (data != null) {
      val bookIds = data.books.map { it.book.id } - bookId

      val newReadingList = ReadingList(
        data.id,
        data.name,
        bookIds
      )

      updateReadingList(newReadingList)
    }
  }

  private fun updateReadingList(newReadingList: ReadingList) {
    lifecycleScope.launch {
      repository.updateReadingList(newReadingList)

      readingListState.value = repository.getReadingListById(newReadingList.id)
      refreshBooks()
    }
  }

  fun bookPickerItemSelected(bookItem: BookItem) {
    val books = _addBookState.value
    val newBooks = books.map { BookItem(it.bookId, it.name, it.bookId == bookItem.bookId) }

    _addBookState.value = newBooks
  }
}
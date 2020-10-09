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

package com.raywenderlich.android.librarian.ui.readingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.ColumnScope.align
import androidx.compose.foundation.layout.RowScope.align
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.composeUi.DeleteDialog
import com.raywenderlich.android.librarian.ui.composeUi.LibrarianTheme
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.ui.readingList.ui.AddReadingList
import com.raywenderlich.android.librarian.ui.readingList.ui.ReadingLists
import com.raywenderlich.android.librarian.ui.readingListDetails.ReadingListDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReadingListFragment : Fragment() {

  @Inject
  lateinit var repository: LibrarianRepository

  val readingListsState = mutableStateOf(emptyList<ReadingListsWithBooks>())

  private val _isShowingAddReadingListState = mutableStateOf(false)
  private val _deleteListState = mutableStateOf<ReadingListsWithBooks?>(null)

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return ComposeView(requireContext()).apply {
      setContent {
        LibrarianTheme {
          ReadingListContent()
        }
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    loadReadingLists()
  }

  private fun loadReadingLists() {
    lifecycleScope.launch {
      readingListsState.value = repository.getReadingLists()
    }
  }

  @Composable
  fun ReadingListContent() {
    Scaffold(
      topBar = { ReadingListTopBar() },
      floatingActionButton = { AddReadingListButton() }) {
      ReadingListContentWrapper()
    }
  }

  @Composable
  fun ReadingListContentWrapper() {
    Stack(
      modifier = Modifier.fillMaxSize()
        .align(Alignment.CenterVertically)
        .align(Alignment.CenterHorizontally)
    ) {
      ReadingLists(
        readingLists = readingListsState.value,
        onItemClick = { readingList -> onItemSelected(readingList) },
        onLongItemTap = { _deleteListState.value = it })

      val isShowingAddList = _isShowingAddReadingListState.value

      if (isShowingAddList) {
        AddReadingList(
          onDismiss = { _isShowingAddReadingListState.value = false },
          onAddList = { name ->
            addReadingList(name)
            _isShowingAddReadingListState.value = false
          })
      }

      val deleteList = _deleteListState.value

      if (deleteList != null) {
        DeleteDialog(
          item = deleteList,
          message = stringResource(id = R.string.delete_message, deleteList.name),
          onDeleteItem = { readingList ->
            deleteReadingList(readingList)
            _deleteListState.value = null
          },
          onDismiss = { _deleteListState.value = null }
        )
      }
    }
  }

  @Composable
  fun AddReadingListButton() {
    FloatingActionButton(onClick = {
      _isShowingAddReadingListState.value = true
    }) {
      Icon(asset = Icons.Default.Add)
    }
  }

  @Composable
  fun ReadingListTopBar() {
    TopBar(title = stringResource(id = R.string.reading_lists_title))
  }

  fun deleteReadingList(readingListsWithBooks: ReadingListsWithBooks) {
    lifecycleScope.launch {
      repository.removeReadingList(
        ReadingList(
          readingListsWithBooks.id,
          readingListsWithBooks.name,
          readingListsWithBooks.books.map { it.book.id }
        )
      )

      readingListsState.value = repository.getReadingLists()
    }
  }

  fun addReadingList(readingListName: String) {
    lifecycleScope.launch {
      repository.addReadingList(ReadingList(name = readingListName, bookIds = emptyList()))

      readingListsState.value = repository.getReadingLists()
    }
  }

  private fun onItemSelected(readingList: ReadingListsWithBooks) {
    startActivity(ReadingListDetailsActivity.getIntent(requireContext(), readingList))
  }
}
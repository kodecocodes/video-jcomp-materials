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

package com.raywenderlich.android.librarian.ui.readingList

import androidx.lifecycle.*
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingListViewModel @Inject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  val readingListsState: LiveData<List<ReadingListsWithBooks>> =
    repository.getReadingListsFlow().asLiveData(viewModelScope.coroutineContext)

  private val _isShowingAddReadingListState = MutableLiveData(false)
  val isShowingAddReadingListState: LiveData<Boolean> = _isShowingAddReadingListState

  private val _deleteReadingListState = MutableLiveData<ReadingListsWithBooks?>()
  val deleteReadingListState: LiveData<ReadingListsWithBooks?> = _deleteReadingListState

  fun onAddReadingListTapped() {
    this._isShowingAddReadingListState.value = true
  }

  fun deleteReadingList(readingListsWithBooks: ReadingListsWithBooks) {
    viewModelScope.launch {
      repository.removeReadingList(
        ReadingList(
          readingListsWithBooks.id,
          readingListsWithBooks.name,
          readingListsWithBooks.books.map { it.book.id }
        )
      )

      onDialogDismiss()
    }
  }

  fun onDeleteReadingList(readingListsWithBooks: ReadingListsWithBooks) {
    _deleteReadingListState.value = readingListsWithBooks
  }

  fun addReadingList(readingListName: String) {
    viewModelScope.launch {
      repository.addReadingList(ReadingList(name = readingListName, bookIds = emptyList()))

      onDialogDismiss()
    }
  }

  fun onDialogDismiss() {
    _isShowingAddReadingListState.value = false
    _deleteReadingListState.value = null
  }
}
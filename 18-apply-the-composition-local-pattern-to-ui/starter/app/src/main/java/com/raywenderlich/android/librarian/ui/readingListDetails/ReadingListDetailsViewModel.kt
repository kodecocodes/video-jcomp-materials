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

package com.raywenderlich.android.librarian.ui.readingListDetails

import androidx.lifecycle.*
import com.raywenderlich.android.librarian.model.BookItem
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingListDetailsViewModel @Inject constructor(
  private val repository: LibrarianRepository
) : ViewModel() {

  private val _addBookState = MutableLiveData<List<BookItem>>()
  val addBookState: LiveData<List<BookItem>> = _addBookState

  var readingListState: LiveData<ReadingListsWithBooks> = MutableLiveData()
    private set

  private val _deleteBookState = MutableLiveData<BookAndGenre?>()
  val deleteBookState: LiveData<BookAndGenre?> = _deleteBookState

  fun setReadingList(readingListsWithBooks: ReadingListsWithBooks) {
    readingListState = repository.getReadingListByIdFlow(readingListsWithBooks.id)
      .asLiveData(viewModelScope.coroutineContext)

    refreshBooks()
  }

  fun refreshBooks() {
    viewModelScope.launch {
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

  fun onItemLongTapped(bookAndGenre: BookAndGenre) {
    _deleteBookState.value = bookAndGenre
  }

  fun onDialogDismiss() {
    _deleteBookState.value = null
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
      onDialogDismiss()
    }
  }

  private fun updateReadingList(newReadingList: ReadingList) {
    viewModelScope.launch {
      repository.updateReadingList(newReadingList)

      refreshBooks()
    }
  }

  fun bookPickerItemSelected(bookItem: BookItem) {
    val books = _addBookState.value ?: return
    val newBooks = books.map { BookItem(it.bookId, it.name, it.bookId == bookItem.bookId) }

    _addBookState.value = newBooks
  }
}
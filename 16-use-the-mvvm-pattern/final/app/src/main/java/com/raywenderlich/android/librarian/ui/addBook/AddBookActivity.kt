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

package com.raywenderlich.android.librarian.ui.addBook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.state.AddBookState
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.composeUi.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddBookActivity : AppCompatActivity(), AddBookView {

  private val _addBookState = mutableStateOf(AddBookState())
  private val _genresState = mutableStateOf(emptyList<Genre>())

  @Inject
  lateinit var repository: LibrarianRepository

  private val addBookViewModel by viewModels<AddBookViewModel>()

  companion object {
    fun getIntent(context: Context): Intent = Intent(context, AddBookActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addBookViewModel.setView(this)
    addBookViewModel.loadGenres()

    setContent {
      LibrarianTheme {
        AddBookContent()
      }
    }
  }

  @Composable
  fun AddBookContent() {
    Scaffold(topBar = { AddBookTopBar() }) {
      AddBookFormContent()
    }
  }

  @Composable
  fun AddBookTopBar() {
    TopBar(
      title = stringResource(id = R.string.add_book_title),
      onBackPressed = { onBackPressed() }
    )
  }

  @Composable
  fun AddBookFormContent() {
    val genres: List<Genre> = _genresState.value
    val bookNameState = remember { mutableStateOf("") }
    val bookDescriptionState = remember { mutableStateOf("") }

    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      InputField(
        value = bookNameState.value,
        onStateChanged = { newValue ->
          bookNameState.value = newValue
          _addBookState.value = _addBookState.value.copy(name = newValue)
          addBookViewModel.onNameChanged(newValue)
        },
        label = stringResource(id = R.string.book_title_hint),
        isInputValid = bookNameState.value.isNotEmpty()
      )

      InputField(
        value = bookDescriptionState.value,
        onStateChanged = { newValue ->
          bookDescriptionState.value = newValue
          _addBookState.value = _addBookState.value.copy(description = newValue)
          addBookViewModel.onDescriptionChanged(newValue)
        },
        label = stringResource(id = R.string.book_description_hint),
        isInputValid = bookDescriptionState.value.isNotEmpty()
      )

      SpinnerPicker(
        pickerText = stringResource(id = R.string.genre_select),
        items = genres,
        itemToName = { it.name },
        onItemPicked = { genre ->
          _addBookState.value = _addBookState.value.copy(genreId = genre.id)
          addBookViewModel.genrePicked(genre)
        })

      ActionButton(
        text = stringResource(id = R.string.add_book_button_text),
        onClick = { addBookViewModel.onAddBookTapped() },
        modifier = Modifier.fillMaxWidth(),
        isEnabled = bookNameState.value.isNotEmpty()
            && bookDescriptionState.value.isNotEmpty()
            && _addBookState.value.genreId.isNotEmpty()
      )
    }
  }

  override fun onBookAdded() {
    setResult(RESULT_OK)
    finish()
  }
}
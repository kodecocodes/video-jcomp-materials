package com.raywenderlich.android.librarian.ui.books

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.raywenderlich.android.librarian.ui.addBook.AddBookActivity

class AddBookContract : ActivityResultContract<Int, Boolean>() {

  override fun createIntent(context: Context, input: Int): Intent {
    return AddBookActivity.getIntent(context)
  }

  override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
    return resultCode == Activity.RESULT_OK
  }
}
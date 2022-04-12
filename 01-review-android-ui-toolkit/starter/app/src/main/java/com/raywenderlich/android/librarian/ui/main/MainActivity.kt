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

package com.raywenderlich.android.librarian.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.databinding.ActivityMainBinding
import com.raywenderlich.android.librarian.databinding.ActivityMainBinding.inflate
import com.raywenderlich.android.librarian.ui.books.BooksFragment
import com.raywenderlich.android.librarian.ui.readingList.ReadingListFragment
import com.raywenderlich.android.librarian.ui.reviews.BookReviewsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  lateinit var binding : ActivityMainBinding

  private var reviewsFragment: BookReviewsFragment? = null
  private var readingListFragment: ReadingListFragment? = null
  private var booksFragment: BooksFragment? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme)
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    binding = inflate(layoutInflater)

    initUi()

    if (savedInstanceState == null) {
      displayNextFragment(R.id.books)
    }
  }

  private fun initUi() {
    val colorDrawable = R.color.bottom_view_selector

    binding.bottomNavigation.itemTextColor =
      ResourcesCompat.getColorStateList(resources, colorDrawable, theme)

    binding.bottomNavigation.itemIconTintList =
      ResourcesCompat.getColorStateList(resources, colorDrawable, theme)

    binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
      displayNextFragment(menuItem.itemId)
      true
    }
  }

  private fun displayNextFragment(itemId: Int) {
    when (itemId) {
      R.id.readingList -> {
        if (readingListFragment == null) {
          readingListFragment = ReadingListFragment()
        }

        displayFragment(readingListFragment!!)
      }

      R.id.bookReviews -> {
        if (reviewsFragment == null) {
          reviewsFragment = BookReviewsFragment()
        }

        displayFragment(reviewsFragment!!)
      }

      R.id.books -> {
        if (booksFragment == null) {
          booksFragment = BooksFragment()
        }

        displayFragment(booksFragment!!)
      }
    }
  }

  private fun displayFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
      .replace(R.id.fragmentContainer, fragment, null)
      .commit()
  }
}

package com.kodeco.android.yummyapp.data

import androidx.annotation.DrawableRes
import com.kodeco.android.yummyapp.R

data class Food(
    val name: String,
    val price: Double?,
    val originalPrice: Double,
    val rating: Double,
    val calories: Int,
    val description: String,
    val waitTime: Int,
    @DrawableRes val banner: Int
)

fun getFood(): List<Food>{
    return listOf(
        Food(
            name = "Burger",
            price = null,
            originalPrice = 15.0,
            rating = 4.3,
            calories = 200,
            description = "A big juicy cheese burger.",
            waitTime = 20,
            banner = R.drawable.food_burger
        ),
        Food(
            name = "English Breakfast",
            price = 17.99,
            originalPrice = 25.0,
            rating = 3.3,
            calories = 150,
            description = "Get your day started well with some English breakfast",
            waitTime = 35,
            banner = R.drawable.food_english_breakfast
        ),
        Food(
            name = "Watermelon",
            price = 3.0,
            originalPrice = 4.99,
            rating = 4.3,
            calories = 200,
            description = "A big juicy cheese burger.",
            waitTime = 8,
            banner = R.drawable.food_melon
        ),
        Food(
            name = "Ramen",
            price = null,
            originalPrice = 12.0,
            rating = 4.8,
            calories = 125,
            description = "Some noodles to fuel the day ahead.",
            waitTime = 13,
            banner = R.drawable.food_ramen
        ),
        Food(
            name = "Beef Steak",
            price = 15.99,
            originalPrice = 19.99,
            rating = 3.5,
            calories = 210,
            description = "Sit down and relax, your steak is on the way.",
            waitTime = 20,
            banner = R.drawable.food_beefsteak
        ),
    )
}
package com.kodeco.android.yummyapp.data

import androidx.annotation.DrawableRes
import com.kodeco.android.yummyapp.R

data class FoodCategory(
    val name: String,
    @DrawableRes val image: Int
)

fun getCategories(): List<FoodCategory>{
    return listOf(
        FoodCategory(name = "Beverages", image = R.drawable.ic_beverages),
        FoodCategory(name = "Fast Food", image = R.drawable.ic_fast_food),
        FoodCategory(name = "Fruits", image = R.drawable.ic_fruits),
        FoodCategory(name = "Pastries", image = R.drawable.ic_pastries),
        FoodCategory(name = "Salads", image = R.drawable.ic_salads),
        FoodCategory(name = "Sea Food", image = R.drawable.ic_sea_food),
        FoodCategory(name = "Others", image = R.drawable.ic_others),
    )
}
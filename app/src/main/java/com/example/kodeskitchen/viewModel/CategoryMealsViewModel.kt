package com.example.kodeskitchen.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kodeskitchen.pojo.MealsByCategory
import com.example.kodeskitchen.pojo.MealsByCategoryList
import com.example.kodeskitchen.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel : ViewModel() {
    val mealsLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getMealsByCategory(categoryName: String) {
        RetrofitInstance.api.getMealsByCategory(categoryName)
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    p0: Call<MealsByCategoryList>,
                    p1: Response<MealsByCategoryList>,
                ) {
                    p1.body()?.let { mealsList ->
                        mealsLiveData.postValue(mealsList.meals)
                    }
                }

                override fun onFailure(p0: Call<MealsByCategoryList>, p1: Throwable) {
                    Log.e("CategoryMealsViewModel", p1.message.toString())
                }

            })
    }

    fun observeMealsLiveData() : LiveData<List<MealsByCategory>>{
        return mealsLiveData
    }
}
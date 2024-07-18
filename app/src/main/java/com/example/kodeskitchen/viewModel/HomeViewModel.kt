package com.example.kodeskitchen.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kodeskitchen.pojo.Category
import com.example.kodeskitchen.pojo.CategoryList
import com.example.kodeskitchen.pojo.MealsByCategoryList
import com.example.kodeskitchen.pojo.MealsByCategory
import com.example.kodeskitchen.pojo.Meal
import com.example.kodeskitchen.pojo.MealList
import com.example.kodeskitchen.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel():ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object :Callback<MealsByCategoryList>{
            override fun onResponse(p0: Call<MealsByCategoryList>, p1: Response<MealsByCategoryList>) {
                if (p1.body()!=null){
                    popularItemsLiveData.value = p1.body()!!.meals
                }
            }

            override fun onFailure(p0: Call<MealsByCategoryList>, p1: Throwable) {
                Log.d("HomeFragment",p1.message.toString())
            }

        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(p0: Call<CategoryList>, p1: Response<CategoryList>) {
                p1.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(p0: Call<CategoryList>, p1: Throwable) {
                Log.e("HomeViewModel", p1.message.toString())
            }

        })
    }
    fun observeRandomMealLivedata() : LiveData<Meal>{
        return randomMealLiveData
    }

    fun observePopularItemsLiveData() : LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }
    fun obsereveCategoriesLiveData() : LiveData <List<Category>>{
        return categoriesLiveData
    }

}
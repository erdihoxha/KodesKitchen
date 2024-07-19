package com.example.kodeskitchen.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kodeskitchen.db.MealDatabase
import com.example.kodeskitchen.pojo.Category
import com.example.kodeskitchen.pojo.CategoryList
import com.example.kodeskitchen.pojo.MealsByCategoryList
import com.example.kodeskitchen.pojo.MealsByCategory
import com.example.kodeskitchen.pojo.Meal
import com.example.kodeskitchen.pojo.MealList
import com.example.kodeskitchen.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

class HomeViewModel(
    private val mealDatabase: MealDatabase
):ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLivedata = mealDatabase.mealDao().getAllMeals()
    private val searchedMealsLiveData = MutableLiveData<List<Meal>>()

    var saveStateRandomMeal : Meal ? = null
    fun getRandomMeal(){
        saveStateRandomMeal?.let { randomMeal ->
            randomMealLiveData.postValue(randomMeal)
            return

        }
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    saveStateRandomMeal = randomMeal
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
    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
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
    fun observeFavoritesMealsLiveData() : LiveData<List<Meal>>{
        return favoritesMealsLivedata
    }
    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }

    }

    fun searchMeals(searchQuery: String) = RetrofitInstance.api.searchMeals(searchQuery).enqueue(
        object : Callback<MealList>{
            override fun onResponse(p0: Call<MealList>, p1: Response<MealList>) {
                val mealsList = p1.body()?.meals
                mealsList?.let{
                    searchedMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(p0: Call<MealList>, p1: Throwable) {
                Log.d("HomeViewModel", p1.message.toString())
            }

        }
    )
    fun observeSearchedMealsLiveData() : LiveData<List<Meal>> = searchedMealsLiveData
}
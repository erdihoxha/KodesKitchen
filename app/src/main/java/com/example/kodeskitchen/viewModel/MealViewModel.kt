package com.example.kodeskitchen.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kodeskitchen.db.MealDatabase
import com.example.kodeskitchen.pojo.Meal
import com.example.kodeskitchen.pojo.MealList
import com.example.kodeskitchen.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    val mealDatabase: MealDatabase
):ViewModel() {
    private var mealDetailsLiveData = MutableLiveData<Meal>()
    fun getMealDetail(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(p0: Call<MealList>, p1: Response<MealList>) {
                if (p1.body()!=null){
                    mealDetailsLiveData.value = p1.body()!!.meals[0]
                }else{
                    return
                }
            }

            override fun onFailure(p0: Call<MealList>, p1: Throwable) {
                Log.d("MealActivity", p1.message.toString())
            }

        })
    }
    fun observeMealDetailLiveData() : LiveData<Meal>{
        return mealDetailsLiveData
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }
    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

}
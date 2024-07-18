package com.example.kodeskitchen.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kodeskitchen.activities.MealActivity
import com.example.kodeskitchen.adapters.MostPopularAdapter
import com.example.kodeskitchen.databinding.FragmentHomeBinding
import com.example.kodeskitchen.pojo.CategoryMeals
import com.example.kodeskitchen.pojo.Meal
import com.example.kodeskitchen.viewModel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdapter : MostPopularAdapter

    companion object {
        const val MEAL_ID = "com.example.kodeskitchen.fragments.idMeal"
        const val MEAL_NAME = "com.example.kodeskitchen.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.kodeskitchen.fragments.thumbMeal"


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]

        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparePopularItemsRecyclerView()
        observeRandomMeal()
        homeMvvm.getRandomMeal()
        onRandomMealClick()

        homeMvvm.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()

    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        homeMvvm.observePopularItemsLiveData().observe(viewLifecycleOwner
        ) { mealList ->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<CategoryMeals>)

        }
    }

    private fun onRandomMealClick(){
        binding.randomMealCard.setOnClickListener{
            val intent = Intent (activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)

            startActivity(intent)
        }
    }


//    private fun observeRandomMeal() {
//        homeMvvm.observeRandomMealLivedata().observe(viewLifecycleOwner,object : Observer<Meal>{
//            override fun onChanged(value: Meal) {
//                Glide.with(this@HomeFragment)
//                    .load(value!!.strMealThumb)
//                    .into(binding.imgRandomMeal)
//
//                this.
//            }
//        })
//    }

    private  fun observeRandomMeal(){
        homeMvvm.observeRandomMealLivedata().observe(viewLifecycleOwner,
            {meal -> Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomMeal)


                this.randomMeal = meal

        })
    }


}
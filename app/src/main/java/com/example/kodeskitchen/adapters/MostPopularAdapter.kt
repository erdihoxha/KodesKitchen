package com.example.kodeskitchen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kodeskitchen.databinding.PopularItemsBinding
import com.example.kodeskitchen.pojo.CategoryMeals

class MostPopularAdapter () : RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>(){
    private var mealsList = ArrayList<CategoryMeals>()
    lateinit var onItemClick: ((CategoryMeals) -> Unit)

    fun  setMeals(mealsList:ArrayList<CategoryMeals>){
        this.mealsList = mealsList
        notifyDataSetChanged()
    }
    class PopularMealViewHolder(val binding:PopularItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
    return mealsList.size
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }
    }
}
package com.example.kodeskitchen.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.kodeskitchen.databinding.MealItemBinding

class FavoritesMealsAdapter : RecyclerView.Adapter<FavoritesMealsAdapter.FavoritesMealsAdapterViewHolder>() {

    inner class FavoritesMealsAdapterViewHolder (binding: MealItemBinding) : RecyclerView.ViewHolder(binding.root)
}
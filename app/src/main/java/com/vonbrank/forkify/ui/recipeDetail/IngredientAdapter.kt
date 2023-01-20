package com.vonbrank.forkify.ui.recipeDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vonbrank.forkify.databinding.IngredientItemBinding
import com.vonbrank.forkify.databinding.RecipeIngredientsBinding
import com.vonbrank.forkify.databinding.RecipePreviewItemBinding
import com.vonbrank.forkify.logic.modal.Ingredient
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.ui.RecipePreviewAdapter

class IngredientAdapter(private val ingredientList: List<Ingredient>, val servings: Int) :
    RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {
    inner class ViewHolder(binding: IngredientItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val recipeIngredientTextView = binding.recipeIngredientText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            IngredientItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredientList[position]
        val count = ingredient.quantity * servings / 4.0;
        holder.recipeIngredientTextView.text =
            "${count} ${ingredient.unit} ${ingredient.description}"
    }

    override fun getItemCount(): Int = ingredientList.size
}
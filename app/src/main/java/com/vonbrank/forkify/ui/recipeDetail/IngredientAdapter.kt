package com.vonbrank.forkify.ui.recipeDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vonbrank.forkify.databinding.IngredientItemBinding
import com.vonbrank.forkify.logic.modal.Ingredient
import com.vonbrank.forkify.utils.Fraction

class IngredientAdapter(
    var ingredientList: List<Ingredient>,
    var currentServings: Int = 4,
    var initialServings: Int = 4
) :
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
        val countString =
            if (ingredient.quantity != null) "${Fraction(ingredient.quantity * currentServings / initialServings.toDouble())} " else ""
        val unitString =
            if (ingredient.unit != null && ingredient.unit.isNotEmpty()) "${ingredient.unit} " else ""
        val descriptionString = ingredient.description ?: ""
        holder.recipeIngredientTextView.text =
            "${countString}${unitString}${descriptionString}"
    }

    override fun getItemCount(): Int = ingredientList.size
}
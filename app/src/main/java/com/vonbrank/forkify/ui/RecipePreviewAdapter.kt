package com.vonbrank.forkify.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vonbrank.forkify.databinding.RecipePreviewItemBinding
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.ui.recipeDetail.RecipeDetailActivity

class RecipePreviewAdapter(val context: Context, private val recipeList: List<RecipePreview>) :
    RecyclerView.Adapter<RecipePreviewAdapter.ViewHolder>() {
    inner class ViewHolder(binding: RecipePreviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleTextView = binding.recipeTitle
        val contentTextView = binding.recipeContent
        val previewImage = binding.recipePreviewImage
        lateinit var recipePreview: RecipePreview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RecipePreviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        binding.root.setOnClickListener {
            RecipeDetailActivity.actionStart(context, viewHolder.recipePreview)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.titleTextView.text = recipe.title
        holder.contentTextView.text = recipe.publisher
        holder.recipePreview = recipe.copy()
        Glide.with(context)
            .load(recipe.imageUrl)
            .into(holder.previewImage)
    }

    override fun getItemCount(): Int = recipeList.size
}
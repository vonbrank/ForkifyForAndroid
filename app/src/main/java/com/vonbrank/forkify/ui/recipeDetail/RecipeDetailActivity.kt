package com.vonbrank.forkify.ui.recipeDetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.vonbrank.forkify.databinding.ActivityRecipeDetailBinding
import com.vonbrank.forkify.logic.modal.Ingredient
import com.vonbrank.forkify.logic.modal.RecipeDetail
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.ui.RecipePreviewAdapter

class RecipeDetailActivity : AppCompatActivity() {


    companion object {
        private const val RECIPE_PREVIEW_DATA = "recipe_preview_data"
        fun actionStart(context: Context, recipePreview: RecipePreview) {

            val intent = Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra(RECIPE_PREVIEW_DATA, recipePreview)
            context.startActivity(intent)
        }
    }

    lateinit var binding: ActivityRecipeDetailBinding
        private set

    private val recipeDetailTestData = RecipeDetail(
        60,
        "5ed6604591c37cdc054bcac4",
        "http://forkify-api.herokuapp.com/images/Pizza2BDip2B12B500c4c0a26c.jpg",
        listOf(
            Ingredient("large pitta breads", 4, ""),
            Ingredient("tomato pure", 2, "tbsps")
        ),
        "Closet Cooking",
        4,
        "http://www.closetcooking.com/2011/03/pizza-dip.html",
        "Pizza Dip"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipePreview = intent.getSerializableExtra(RECIPE_PREVIEW_DATA) as RecipePreview?
        if (recipePreview == null) {
            Toast.makeText(this, "No recipe preview data provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapsingToolbar.title = recipePreview.title
        Glide.with(this)
            .load(recipePreview.imageUrl)
            .into(binding.appBarImageView)


        val layoutManager = LinearLayoutManager(this)
        binding.recipeIngredientsLayout.ingredientsRecyclerView.layoutManager = layoutManager
        val adapter = IngredientAdapter(
            recipeDetailTestData.ingredients,
            4,
        )
        binding.recipeIngredientsLayout.ingredientsRecyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
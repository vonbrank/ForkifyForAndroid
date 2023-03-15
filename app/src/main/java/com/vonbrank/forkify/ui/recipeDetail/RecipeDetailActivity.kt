package com.vonbrank.forkify.ui.recipeDetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.vonbrank.forkify.databinding.ActivityRecipeDetailBinding
import com.vonbrank.forkify.logic.modal.RecipePreview

class RecipeDetailActivity : AppCompatActivity() {

    companion object {
        private const val RECIPE_PREVIEW_DATA = "recipe_preview_data"
        fun actionStart(
            context: Context,
            recipePreview: RecipePreview,
        ) {

            val intent = Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra(RECIPE_PREVIEW_DATA, recipePreview)
            context.startActivity(intent)
        }
    }

    private val viewModal by lazy { ViewModelProvider(this)[RecipeDetailViewModal::class.java] }

    lateinit var binding: ActivityRecipeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipePreview = intent.getSerializableExtra(RECIPE_PREVIEW_DATA) as RecipePreview?
        viewModal.recipePreview = recipePreview

        if (savedInstanceState == null) {
            val recipeDetailFragment = RecipeDetailFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(
                    binding.recipeDetailContainer.id,
                    recipeDetailFragment
                )
                .commit()

        }
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
package com.vonbrank.forkify.ui.recipeDetail

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.vonbrank.forkify.R
import com.vonbrank.forkify.databinding.ActivityRecipeDetailBinding
import com.vonbrank.forkify.logic.modal.Ingredient
import com.vonbrank.forkify.logic.modal.RecipeDetail
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.utils.setImageViewThemeColorFilter

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

    val viewModal by lazy { ViewModelProvider(this)[RecipeDetailViewModal::class.java] }

    lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.recipeIngredientsLayout.ingredientsRecyclerView.layoutManager = layoutManager
        ingredientAdapter = IngredientAdapter(ArrayList<Ingredient>(), 4)
        binding.recipeIngredientsLayout.ingredientsRecyclerView.adapter = ingredientAdapter

        if (!tryLoadData()) return

        initAppBar()
        initViewModalObserver()
        initButtonClickListener()
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

    private fun handleChangeServings(newServings: Int) {
        if (newServings < 1) return

        viewModal.servings.value = newServings
    }

    private fun refreshRecipeDetail(recipeDetail: RecipeDetail, currentServings: Int) {

        binding.apply {
            collapsingToolbar.title = recipeDetail.title
            Glide.with(this@RecipeDetailActivity)
                .load(recipeDetail.imageUrl)
                .placeholder(R.drawable.forkify_restaurant_menu_dark_24)
                .error(R.drawable.forkify_restaurant_menu_dark_24)
                .into(appBarImageView)

            recipeInfoLayout.clockTimeText.text = "${recipeDetail.cookingTime.toInt()} MINUTES"

            refreshRecipeIngredients(
                recipeDetail.ingredients,
                currentServings,
                recipeDetail.servings
            )

            howToCookItLayout.howToCookItContentText.text =
                "This recipe was carefully designed and tested by ${recipeDetail.publisher}. Please check out directions at their website."
        }
    }

    private fun refreshRecipeIngredients(
        ingredientList: List<Ingredient>,
        currentServings: Int,
        initialServings: Int = 4
    ) {
        ingredientAdapter.ingredientList = ingredientList
        ingredientAdapter.currentServings = currentServings
        ingredientAdapter.initialServings = initialServings
        ingredientAdapter.notifyDataSetChanged()
        binding.recipeInfoLayout.servingsText.text =
            "${viewModal.servings.value ?: viewModal.servingsDefaultValue} SERVINGS"
    }

    private fun tryLoadData(): Boolean {
        val recipePreview = intent.getSerializableExtra(RECIPE_PREVIEW_DATA) as RecipePreview?

        if (recipePreview != null) {
            binding.recipeDetailLoadingBar.visibility = View.VISIBLE
            binding.recipeDetailBody.visibility = View.GONE

            viewModal.getRecipeDetail(recipePreview.id)
            viewModal.recipeDetail.value = RecipeDetail(
                0.0, recipePreview.id, recipePreview.imageUrl,
                ArrayList<Ingredient>(), recipePreview.publisher, 0, "", recipePreview.title
            )
        } else if (viewModal.recipeDetail.value != null && viewModal.recipeDetail.value!!.id.isNotEmpty()) {
            viewModal.recipeDetail.value = viewModal.recipeDetail.value
            binding.recipeDetailLoadingBar.visibility = View.GONE
            binding.recipeDetailBody.visibility = View.VISIBLE
        } else {
            Toast.makeText(this, "No recipe preview data provided", Toast.LENGTH_SHORT).show()
            finish()
            return false
        }
        return true
    }

    private fun initAppBar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setImageViewThemeColorFilter(binding.appBarImageView)
    }

    private fun initViewModalObserver() {
        viewModal.recipeDetailLiveData.observe(this, Observer { result ->
            val recipe = result.getOrNull()
            if (recipe != null) {
                viewModal.recipeDetail.value = recipe
                ingredientAdapter.ingredientList = recipe.ingredients
                ingredientAdapter.notifyDataSetChanged()
                binding.recipeDetailLoadingBar.visibility = View.GONE
                binding.recipeDetailBody.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "Cannot load recipe detail", Toast.LENGTH_SHORT).show()
                finish()
                result.exceptionOrNull()?.printStackTrace()
            }
        })

        viewModal.recipeDetail.observe(this, Observer { recipeDetail ->
            if (recipeDetail == null) return@Observer

            refreshRecipeDetail(
                recipeDetail,
                viewModal.servings.value ?: viewModal.servingsDefaultValue
            )
        })

        viewModal.servings.observe(this, Observer { servings ->
            if (servings == null) return@Observer

            refreshRecipeIngredients(
                viewModal.recipeDetail.value?.ingredients ?: ArrayList<Ingredient>(), servings
            )
        })
    }

    private fun initButtonClickListener() {
        binding.recipeInfoLayout.increaseServingsButton.setOnClickListener {
            handleChangeServings((viewModal.servings.value ?: 4) + 1)
        }
        binding.recipeInfoLayout.decreaseServingsButton.setOnClickListener {
            handleChangeServings((viewModal.servings.value ?: 4) - 1)
        }
        binding.howToCookItLayout.howToCookItDirectionButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            if (viewModal.recipeDetail.value == null) return@setOnClickListener
            intent.data = Uri.parse(viewModal.recipeDetail.value!!.sourceUrl)
            startActivity(intent)
        }
    }
}
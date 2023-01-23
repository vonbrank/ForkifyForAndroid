package com.vonbrank.forkify.ui.recipeDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.vonbrank.forkify.R
import com.vonbrank.forkify.databinding.FragmentRecipeDetailBinding
import com.vonbrank.forkify.logic.modal.Ingredient
import com.vonbrank.forkify.logic.modal.RecipeDetail
import com.vonbrank.forkify.utils.setImageViewThemeColorFilter

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null

    private val binding get() = _binding!!

    private val viewModal by lazy { ViewModelProvider(requireActivity())[RecipeDetailViewModal::class.java] }

    lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        binding.recipeIngredientsLayout.ingredientsRecyclerView.layoutManager = layoutManager
        ingredientAdapter = IngredientAdapter(ArrayList<Ingredient>(), 4)
        binding.recipeIngredientsLayout.ingredientsRecyclerView.adapter = ingredientAdapter

        if (!tryLoadData()) return

        initAppBar()
        initViewModalObserver()
        initButtonClickListener()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param recipePreview Recipe Preview Data.
         * @return A new instance of fragment RecipeDetailFragment.
         */
        @JvmStatic
        fun newInstance() =
            RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun handleChangeServings(newServings: Int) {
        if (newServings < 1) return

        viewModal.servings.value = newServings
    }

    private fun refreshRecipeDetail(recipeDetail: RecipeDetail, currentServings: Int) {

        binding.apply {
            collapsingToolbar.title = recipeDetail.title
            Glide.with((activity as AppCompatActivity))
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

        if (viewModal.recipeDetail.value != null && viewModal.recipeDetail.value!!.id.isNotEmpty()) {
            viewModal.recipeDetail.value = viewModal.recipeDetail.value
            binding.recipeDetailLoadingBar.visibility = View.GONE
            binding.recipeDetailBody.visibility = View.VISIBLE
        } else if (viewModal.recipePreview != null) {
            binding.recipeDetailLoadingBar.visibility = View.VISIBLE
            binding.recipeDetailBody.visibility = View.GONE
            viewModal.apply {
                getRecipeDetail(recipePreview!!.id)
                recipeDetail.value = RecipeDetail(
                    0.0, recipePreview!!.id, recipePreview!!.imageUrl,
                    ArrayList<Ingredient>(), recipePreview!!.publisher, 0, "", recipePreview!!.title
                )
            }
        } else {
            Toast.makeText(activity, "No recipe preview data provided", Toast.LENGTH_SHORT).show()
            (activity as AppCompatActivity).finish()
            return false
        }
        return true
    }

    private fun initAppBar() {

        val recipeDetailActivity = activity as RecipeDetailActivity? ?: return

        recipeDetailActivity.setSupportActionBar(binding.toolBar)
        recipeDetailActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setImageViewThemeColorFilter(binding.appBarImageView)
    }

    private fun initViewModalObserver() {
        viewModal.recipeDetailLiveData.observe(viewLifecycleOwner, Observer { result ->
            val recipe = result.getOrNull()
            if (recipe != null) {
                viewModal.recipeDetail.value = recipe
                ingredientAdapter.ingredientList = recipe.ingredients
                ingredientAdapter.notifyDataSetChanged()
                binding.recipeDetailLoadingBar.visibility = View.GONE
                binding.recipeDetailBody.visibility = View.VISIBLE
            } else {
                Toast.makeText(activity, "Cannot load recipe detail", Toast.LENGTH_SHORT).show()
                (activity as AppCompatActivity).finish()
                result.exceptionOrNull()?.printStackTrace()
            }
        })

        viewModal.recipeDetail.observe(viewLifecycleOwner, Observer { recipeDetail ->
            if (recipeDetail == null) return@Observer

            refreshRecipeDetail(
                recipeDetail,
                viewModal.servings.value ?: viewModal.servingsDefaultValue
            )
        })

        viewModal.servings.observe(viewLifecycleOwner, Observer { servings ->
            if (servings == null) return@Observer

            refreshRecipeIngredients(
                viewModal.recipeDetail.value?.ingredients ?: ArrayList<Ingredient>(), servings
            )
        })

        viewModal.recipeBookmarkList.observe(viewLifecycleOwner) { recipeInBookmarkList ->
            Log.d("Recipe Detail Fragment", "recipeInBookmarkList = $recipeInBookmarkList")
        }

        viewModal.recipeInBookmarkList.observe(viewLifecycleOwner) { recipeInBookmarkList ->
            binding.addBookmarkButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    if (recipeInBookmarkList) R.drawable.forkify_bookmark_filled_24
                    else R.drawable.forkify_bookmark_border_24
                )
            )
        }
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

        binding.addBookmarkButton.setOnClickListener {
            viewModal.recipePreview?.let { recipePreview ->
                viewModal.toggleRecipeBookmarkItem(
                    recipePreview
                )
            }
        }


    }

}
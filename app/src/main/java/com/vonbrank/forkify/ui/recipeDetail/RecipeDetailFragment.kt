package com.vonbrank.forkify.ui.recipeDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.vonbrank.forkify.R
import com.vonbrank.forkify.databinding.FragmentRecipeDetailBinding
import com.vonbrank.forkify.logic.modal.Ingredient
import com.vonbrank.forkify.logic.modal.RecipeDetail
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.utils.setImageViewThemeColorFilter

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null

    private val binding get() = _binding!!

    private val viewModal by lazy { ViewModelProvider(requireActivity())[RecipeDetailViewModal::class.java] }

    lateinit var ingredientAdapter: IngredientAdapter

    private lateinit var bannerImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager =
            if (binding.recipeIngredientsLayout.recipeIngredientsTitle840dp == null)
                LinearLayoutManager(activity)
            else GridLayoutManager(activity, 2)
//        val layoutManager = GridLayoutManager(activity, 2)
        binding.recipeIngredientsLayout.ingredientsRecyclerView.layoutManager = layoutManager
        ingredientAdapter = IngredientAdapter(ArrayList<Ingredient>(), 4)
        binding.recipeIngredientsLayout.ingredientsRecyclerView.adapter = ingredientAdapter

        initAppBar()
        initViewModalObserver()
        initButtonClickListener()

        val currentRecipePreview = viewModal.recipePreview
        if (currentRecipePreview != null) {
            tryLoadData(currentRecipePreview)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
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

    private fun refreshBookmarkButton(
        recipePreview: RecipePreview?,
        recipeList: List<RecipePreview>
    ) {
        val recipeInBookmarkList =
            if (recipePreview != null) recipeList.find { it.id == recipePreview.id } != null else false
        binding.addBookmarkButton.setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                if (recipeInBookmarkList) R.drawable.forkify_bookmark_filled_24
                else R.drawable.forkify_bookmark_border_24
            )
        )
    }

    private fun refreshAppBar() {
        bannerImageView.setImageResource(R.drawable.forkify_restaurant_menu_dark_24)
        Glide.with((activity as AppCompatActivity))
            .load(viewModal.recipePreview?.imageUrl)
            .placeholder(R.drawable.forkify_restaurant_menu_dark_24)
            .error(R.drawable.forkify_restaurant_menu_dark_24)
            .into(bannerImageView)
        val recipePreview = viewModal.recipePreview
        val title = recipePreview?.title ?: ""
        binding.apply {
            if (viewModal.independentActivity) {
                collapsingToolbar.title = title
            } else {
                binding.recipeBanner.bannerTitleText.text = title
            }
        }
    }

    fun tryLoadData(recipePreview: RecipePreview?): Boolean {
        viewModal.recipePreview = recipePreview
        refreshAppBar()
        refreshBookmarkButton(
            viewModal.recipePreview,
            viewModal.recipeBookmarkList.value ?: ArrayList()
        )

        binding.apply {
            if (recipeDetailEmptyPlaceholderText != null && recipeDetailContainer != null) {
                recipeDetailEmptyPlaceholderText.visibility = View.GONE
                recipeDetailContainer.visibility = View.VISIBLE
                addBookmarkButton.visibility = View.VISIBLE
            }
        }



        if (recipePreview != null && recipePreview.id.isNotEmpty()) {
            binding.recipeDetailLoadingBar.visibility = View.VISIBLE
            binding.recipeDetailBody.visibility = View.GONE
            recipePreview.apply {
                viewModal.getRecipeDetail(id)
                viewModal.recipeDetail.value = RecipeDetail(
                    0.0, id, imageUrl,
                    ArrayList<Ingredient>(), publisher, 0, "", title
                )
            }
        } else {
            Toast.makeText(activity, "No recipe preview data provided", Toast.LENGTH_SHORT).show()
            if (viewModal.independentActivity) {
                (activity as AppCompatActivity).finish()
                return false
            }
        }
        return true
    }

    private fun initAppBar() {
        if (viewModal.independentActivity) {
            val recipeDetailActivity = activity as RecipeDetailActivity? ?: return
            recipeDetailActivity.setSupportActionBar(binding.toolBar)
            recipeDetailActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            bannerImageView = binding.appBarImageView
            binding.recipeBannerContainer.visibility = View.GONE
        } else {
            binding.appBar.visibility = View.GONE
            binding.recipeBannerContainer.visibility = View.VISIBLE
            bannerImageView = binding.recipeBanner.bannerImageView
        }
        setImageViewThemeColorFilter(bannerImageView)
        bannerImageView.setImageResource(R.drawable.forkify_restaurant_menu_dark_24)
    }

    private fun initViewModalObserver() {
        viewModal.recipeDetailLiveData.observe(viewLifecycleOwner) { result ->
            val recipe = result.getOrNull()
            if (recipe != null) {
                viewModal.recipeDetail.value = recipe
                ingredientAdapter.ingredientList = recipe.ingredients
                ingredientAdapter.notifyDataSetChanged()
                binding.recipeDetailLoadingBar.visibility = View.GONE
                binding.recipeDetailBody.visibility = View.VISIBLE
            } else {
                Toast.makeText(activity, "Cannot load recipe detail", Toast.LENGTH_SHORT).show()
                if (viewModal.independentActivity) {
                    (activity as AppCompatActivity).finish()
                }
                result.exceptionOrNull()?.printStackTrace()
            }
        }

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
            refreshBookmarkButton(viewModal.recipePreview, recipeInBookmarkList)
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
            val recipePreviewValue = viewModal.recipePreview
            recipePreviewValue?.let {
                viewModal.toggleRecipeBookmarkItem(
                    it
                )
            }
        }


    }

}
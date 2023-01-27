package com.vonbrank.forkify.ui.recipeSearch

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vonbrank.forkify.R
import com.vonbrank.forkify.databinding.FragmentRecipeSearchBinding
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.ui.recipePreview.RecipePreviewFragment

private const val ITEM_COUNT_PER_PAGE = 10

class RecipeSearchFragment : Fragment() {

    private var _binding: FragmentRecipeSearchBinding? = null

    private val binding get() = _binding!!

    val viewModel by lazy { ViewModelProvider(this)[RecipeSearchViewModal::class.java] }

    private var searchView: SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeSearchBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.appBar)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.recipeLoadResultLiveData.observe(viewLifecycleOwner, Observer { result ->
            val recipes = result.getOrNull()

            if (recipes != null) {
                viewModel.recipeListLivaData.value = ArrayList(recipes)
                searchView?.onActionViewCollapsed()
                if (recipes.isEmpty()) {
                    Toast.makeText(activity, "Cannot find out any recipe", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(activity, "Failed to search recipe", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.recipePreviewProgressBar.visibility = View.GONE
            if (recipes == null || recipes.isEmpty()) {
                binding.noRecipesPlaceholder.visibility = View.VISIBLE
                binding.recipeSearchResult.visibility = View.GONE
            } else {
                binding.noRecipesPlaceholder.visibility = View.GONE
                binding.recipeSearchResult.visibility = View.VISIBLE
            }
        })

        viewModel.recipeListLivaData.observe(viewLifecycleOwner) { recipeList ->

            if (recipeList.size <= ITEM_COUNT_PER_PAGE) {
                binding.searchResultPaginationPanel.visibility = View.GONE
            } else {
                binding.searchResultPaginationPanel.visibility = View.VISIBLE
            }
            viewModel.recipeListPaginationNumber.value = 1

        }

        viewModel.recipeListPaginationNumber.observe(viewLifecycleOwner) { number ->

            val newRecipeListToDisplay = viewModel.recipeListLivaData.value ?: ArrayList()

            refreshRecipeSearchResultFragment(
                newRecipeListToDisplay,
                number
            )

            if (number > 1) {
                binding.recipeSearchResultPaginationLeft.visibility = View.VISIBLE
            } else {
                binding.recipeSearchResultPaginationLeft.visibility = View.INVISIBLE
            }

            if (number * ITEM_COUNT_PER_PAGE >= newRecipeListToDisplay.size
            ) {
                binding.recipeSearchResultPaginationRight.visibility = View.INVISIBLE
            } else {
                binding.recipeSearchResultPaginationRight.visibility = View.VISIBLE
            }

            binding.recipeSearchResultPaginationLeft.text = "Page ${number - 1}"
            binding.recipeSearchResultPaginationRight.text = "Page ${number + 1}"
        }

        binding.recipeSearchResultPaginationLeft.setOnClickListener {
            if (viewModel.recipeListPaginationNumber.value == null) return@setOnClickListener
            viewModel.recipeListPaginationNumber.value =
                viewModel.recipeListPaginationNumber.value!! - 1
        }
        binding.recipeSearchResultPaginationRight.setOnClickListener {
            if (viewModel.recipeListPaginationNumber.value == null) return@setOnClickListener
            viewModel.recipeListPaginationNumber.value =
                viewModel.recipeListPaginationNumber.value!! + 1
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.forkify_toolbar_menu, menu)

        searchView = menu.findItem(R.id.menu_search).actionView as SearchView?

        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchRecipe(query)
                    binding.recipePreviewProgressBar.visibility = View.VISIBLE
                    binding.recipeSearchResult.visibility = View.GONE
                    binding.noRecipesPlaceholder.visibility = View.GONE
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun refreshRecipeSearchResultFragment(
        recipePreviewList: List<RecipePreview>,
        itemCountPerPage: Int
    ) {
        val newRecipeList = ArrayList<RecipePreview>()
        for (i in ((itemCountPerPage - 1) * ITEM_COUNT_PER_PAGE) until (itemCountPerPage) * ITEM_COUNT_PER_PAGE) {
            if (i >= recipePreviewList.size) break
            newRecipeList.add(recipePreviewList[i])
        }
        val recipeSearchResultFragment =
            childFragmentManager.findFragmentByTag(binding.recipeSearchResultFragment.tag as String?)
                    as RecipePreviewFragment?
        if (recipeSearchResultFragment != null) {
            recipeSearchResultFragment.recipePreviewList = newRecipeList
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.vonbrank.forkify.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vonbrank.forkify.R
import com.vonbrank.forkify.databinding.FragmentRecipeSearchBinding
import com.vonbrank.forkify.logic.modal.RecipePreview


class RecipeSearchFragment : Fragment() {

    private var _binding: FragmentRecipeSearchBinding? = null

    private val binding get() = _binding!!

    val viewModel by lazy { ViewModelProvider(this)[RecipePreviewViewModal::class.java] }

    private lateinit var adapter: RecipePreviewAdapter

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

        val layoutManager = LinearLayoutManager(activity)
        binding.recipePreviewRecyclerView.layoutManager = layoutManager
        adapter = RecipePreviewAdapter(
            this@RecipeSearchFragment.requireContext(),
            viewModel.recipeList
        )
        binding.recipePreviewRecyclerView.adapter = adapter

        viewModel.recipeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val recipes = result.getOrNull()
            if (recipes != null) {
                viewModel.recipeList.clear()
                viewModel.recipeList.addAll(recipes)
                adapter.notifyDataSetChanged()
                searchView?.onActionViewCollapsed()
            } else {
                Toast.makeText(activity, "Cannot find out any recipe", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.recipePreviewProgressBar.visibility = View.GONE
            binding.recipePreviewRecyclerView.visibility = View.VISIBLE
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.forkify_toolbar_menu, menu)

        searchView = MenuItemCompat.getActionView(menu.findItem(R.id.search)) as SearchView?

        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchRecipe(query)
                    binding.recipePreviewProgressBar.visibility = View.VISIBLE
                    binding.recipePreviewRecyclerView.visibility = View.GONE
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.vonbrank.forkify.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vonbrank.forkify.R
import com.vonbrank.forkify.databinding.FragmentRecipeSearchBinding
import com.vonbrank.forkify.logic.modal.Recipe


class RecipeSearchFragment : Fragment() {

    private var _binding: FragmentRecipeSearchBinding? = null

    private val binding get() = _binding!!

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

        val recipeList = listOf<Recipe>(
            Recipe(
                "5ed6604591c37cdc054bcd09",
                "http://forkify-api.herokuapp.com/images/BBQChickenPizzawithCauliflowerCrust5004699695624ce.jpg",
                "Cauliflower Pizza Crust (with BBQ Chicken Pizza)",
                "Closet Cooking",
            ), Recipe(
                "5ed6604591c37cdc054bcb34",
                "http://forkify-api.herokuapp.com/images/pizza292x2007a259a79.jpg",
                "Homemade Pizza",
                "Homemade Pizza",
            ), Recipe(
                "5ed6604591c37cdc054bcd09",
                "http://forkify-api.herokuapp.com/images/BBQChickenPizzawithCauliflowerCrust5004699695624ce.jpg",
                "Cauliflower Pizza Crust (with BBQ Chicken Pizza)",
                "Closet Cooking",
            )
        );

        val layoutManager = LinearLayoutManager(activity)
        binding.recipePreviewRecyclerView.layoutManager = layoutManager
        val adapter = RecipePreviewAdapter(this.requireContext(), recipeList)
        binding.recipePreviewRecyclerView.adapter = adapter

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.forkify_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
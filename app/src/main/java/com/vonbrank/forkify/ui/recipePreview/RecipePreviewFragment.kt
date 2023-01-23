package com.vonbrank.forkify.ui.recipePreview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vonbrank.forkify.databinding.FragmentRecipePreviewBinding
import com.vonbrank.forkify.logic.modal.RecipePreview

private const val RECIPE_PREVIEW_LIST = "recipe_preview_list"

class RecipePreviewFragment : Fragment() {

    private var _binding: FragmentRecipePreviewBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: RecipePreviewAdapter

    val viewModal by lazy { ViewModelProvider(this)[RecipePreviewViewModal::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { it ->
            val initialList =
                (it.getSerializable(RECIPE_PREVIEW_LIST) as ArrayList<*>?)?.filterIsInstance<RecipePreview>()
                    ?: ArrayList()

            viewModal.recipePreviewList.let {
                it.clear()
                it.addAll(initialList)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        binding.recipePreviewRecyclerView.layoutManager = layoutManager
        adapter = RecipePreviewAdapter(
            requireActivity(),
            viewModal.recipePreviewList
        )
        binding.recipePreviewRecyclerView.adapter = adapter
    }

    fun setRecipePreviewList(newRecipePreviewList: List<RecipePreview>) {
        viewModal.recipePreviewList.let {
            it.clear()
            it.addAll(newRecipePreviewList)
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(recipePreviewList: List<RecipePreview>) =
            RecipePreviewFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(RECIPE_PREVIEW_LIST, ArrayList(recipePreviewList))
                }
            }
    }
}
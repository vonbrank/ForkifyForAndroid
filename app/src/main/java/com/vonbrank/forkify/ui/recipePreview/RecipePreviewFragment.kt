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

    private lateinit var layoutManager: LinearLayoutManager

    private val recipePreviewViewModal by lazy { ViewModelProvider(this)[RecipePreviewViewModal::class.java] }

    private val recipePreviewClickViewModal by lazy { ViewModelProvider(requireActivity())[RecipePreviewActionViewModal::class.java] }

    var recipePreviewList
        get() = recipePreviewViewModal.recipePreviewList as List<RecipePreview>
        set(value) {
            recipePreviewViewModal.recipePreviewList.let {
                it.clear()
                it.addAll(value)
                adapter.notifyDataSetChanged()
            }
            binding.recipePreviewRecyclerView.smoothScrollToPosition(0)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { it ->
            val initialList =
                (it.getSerializable(RECIPE_PREVIEW_LIST) as ArrayList<*>?)?.filterIsInstance<RecipePreview>()
                    ?: ArrayList()

            recipePreviewViewModal.recipePreviewList.let {
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

        layoutManager = LinearLayoutManager(activity)
        binding.recipePreviewRecyclerView.layoutManager = layoutManager
        adapter = RecipePreviewAdapter(
            requireActivity(),
            recipePreviewViewModal.recipePreviewList
        ) {
            recipePreviewClickViewModal.handleClickRecipePreviewItem(it)
        }
        binding.recipePreviewRecyclerView.adapter = adapter
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
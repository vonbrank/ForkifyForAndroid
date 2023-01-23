package com.vonbrank.forkify.ui.bookmark

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vonbrank.forkify.databinding.FragmentBookmarkBinding
import com.vonbrank.forkify.ui.recipePreview.RecipePreviewAdapter

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null

    private val binding get() = _binding!!

    private val sharedBookmarkViewModal by lazy { ViewModelProvider(requireActivity())[BookmarkViewModal::class.java] }

    private lateinit var adapter: RecipePreviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        binding.recipeBookmarkRecyclerView.layoutManager = layoutManager
        adapter = RecipePreviewAdapter(
            this.requireContext(),
            sharedBookmarkViewModal.recipeBookmarkList.value ?: ArrayList()
        )
        binding.recipeBookmarkRecyclerView.adapter = adapter

        sharedBookmarkViewModal.recipeBookmarkList.observe(
            viewLifecycleOwner
        ) { recipeBookmarkList ->

            if (recipeBookmarkList.size == 0) {
                binding.recipeBookmarkEmptyPlaceholder.visibility = View.VISIBLE
                binding.recipeBookmarkRecyclerView.visibility = View.GONE
            } else {
                binding.recipeBookmarkEmptyPlaceholder.visibility = View.GONE
                binding.recipeBookmarkRecyclerView.visibility = View.VISIBLE
            }

            adapter.recipeList = recipeBookmarkList
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BookmarkFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
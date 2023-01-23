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
import com.vonbrank.forkify.ui.recipePreview.RecipePreviewFragment

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null

    private val binding get() = _binding!!

    private val sharedBookmarkViewModal by lazy { ViewModelProvider(requireActivity())[BookmarkViewModal::class.java] }

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

        sharedBookmarkViewModal.recipeBookmarkList.observe(
            viewLifecycleOwner
        ) { recipeBookmarkList ->

            val recipePreviewFragment =
                childFragmentManager.findFragmentByTag(binding.recipePreviewBookmarkFragment.tag as String) as RecipePreviewFragment?


            if (recipeBookmarkList.size == 0) {
                binding.recipeBookmarkEmptyPlaceholder.visibility = View.VISIBLE
                binding.recipePreviewBookmarkFragment.visibility = View.GONE
            } else {
                binding.recipeBookmarkEmptyPlaceholder.visibility = View.GONE
                binding.recipePreviewBookmarkFragment.visibility = View.VISIBLE
            }
            recipePreviewFragment?.recipePreviewList = recipeBookmarkList
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
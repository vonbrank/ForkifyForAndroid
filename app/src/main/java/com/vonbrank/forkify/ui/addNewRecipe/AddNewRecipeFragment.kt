package com.vonbrank.forkify.ui.addNewRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vonbrank.forkify.ForkifyApplication
import com.vonbrank.forkify.databinding.FragmentAddNewRecipeBinding
import com.vonbrank.forkify.databinding.FragmentRecipeDetailBinding

class AddNewRecipeFragment : Fragment() {

    private var _binding: FragmentAddNewRecipeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addNewRecipeUploadButton.setOnClickListener {
            Toast.makeText(
                ForkifyApplication.context,
                "⚠️ Uploads disabled. Build your application with your own API key :)",
                Toast.LENGTH_SHORT
            ).show()
        }

        val activity = (activity as AppCompatActivity)

        activity.setSupportActionBar(binding.appBar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
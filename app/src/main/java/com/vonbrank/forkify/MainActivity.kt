package com.vonbrank.forkify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.vonbrank.forkify.databinding.ActivityMainBinding
import com.vonbrank.forkify.ui.addNewRecipe.AddNewRecipeActivity
import com.vonbrank.forkify.ui.bookmark.*
import com.vonbrank.forkify.ui.recipeSearch.RecipeSearchFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
        private set

    val sharedBookmarkViewModal by lazy { ViewModelProvider(this)[BookmarkViewModal::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(binding.recipeSearchFragment.id, RecipeSearchFragment()).commit()
            supportFragmentManager.beginTransaction()
                .add(binding.recipeBookmarkFragment.id, BookmarkFragment()).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.menu_bookmark -> {
                binding.rootDrawerLayout.openDrawer(binding.recipeBookmarkFragment)
            }
            R.id.menu_add_recipe -> {
                AddNewRecipeActivity.actionStart(this)
            }
        }

        return true
    }

}
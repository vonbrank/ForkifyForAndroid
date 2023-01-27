package com.vonbrank.forkify.ui.addNewRecipe

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.vonbrank.forkify.databinding.ActivityAddNewRecipeBinding

class AddNewRecipeActivity : AppCompatActivity() {

    companion object {
        fun actionStart(
            context: Context,
        ) {

            val intent = Intent(context, AddNewRecipeActivity::class.java)
            context.startActivity(intent)
        }
    }

    lateinit var binding: ActivityAddNewRecipeBinding
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(binding.addNewRecipeContainer.id, AddNewRecipeFragment())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
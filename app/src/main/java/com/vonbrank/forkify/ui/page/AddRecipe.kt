package com.vonbrank.forkify.ui.page

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vonbrank.forkify.ForkifyApplication

@Composable
fun AddRecipe(modifier: Modifier = Modifier) {

    val addRecipeState = rememberAddRecipeState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),

        ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {

                Text(
                    text = "Recipe Data".uppercase(),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                )
            }
        }
        item {
            AddRecipeTextField(
                label = {
                    Text(text = "Title")
                },
                value = addRecipeState.title,
                onValueChange = { addRecipeState.title = it })
        }
        item {
            AddRecipeTextField(
                label = {
                    Text(text = "URL")
                },
                value = addRecipeState.url,
                onValueChange = { addRecipeState.url = it })
        }
        item {
            AddRecipeTextField(
                label = {
                    Text(text = "Image URL")
                },
                value = addRecipeState.imageUrl,
                onValueChange = { addRecipeState.imageUrl = it })
        }
        item {
            AddRecipeTextField(
                label = {
                    Text(text = "Publisher")
                },
                value = addRecipeState.publisher,
                onValueChange = { addRecipeState.publisher = it })
        }
        item {
            AddRecipeTextField(
                label = {
                    Text(text = "Prep time")
                },
                value = addRecipeState.prepTime,
                onValueChange = { addRecipeState.prepTime = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        item {
            AddRecipeTextField(
                label = {
                    Text(text = "Servings")
                },
                value = addRecipeState.servings,
                onValueChange = { addRecipeState.servings = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp)
            ) {

                Text(
                    text = "Ingredients".uppercase(),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                )
            }
        }
        itemsIndexed(addRecipeState.ingredients) { index: Int, _: String ->
            AddRecipeTextField(
                label = {
                    Text(text = "Ingredient $index")
                },
                value = addRecipeState.ingredients[index],
                onValueChange = {
                    val newList = ArrayList<String>()
                    for ((arrayIndex, value) in addRecipeState.ingredients.withIndex()) {
                        if (arrayIndex == index) {
                            newList.add(it)
                        } else {
                            newList.add(value)
                        }
                    }
                    addRecipeState.ingredients = newList
                },
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        Toast.makeText(
                            ForkifyApplication.context,
                            "⚠️ Uploads disabled. Build your application with your own API key :)",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CloudUpload,
                        contentDescription = "Upload icon"
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Upload".uppercase())
                }
            }
        }
    }
}

@Composable
fun rememberAddRecipeState(): AddRecipeState = rememberSaveable(saver = AddRecipeState.Saver) {
    AddRecipeState()
}

class AddRecipeState() {

    var title by mutableStateOf("")
    var url by mutableStateOf("")
    var imageUrl by mutableStateOf("")
    var publisher by mutableStateOf("")
    var prepTime by mutableStateOf("")
    var servings by mutableStateOf("")
    var ingredients by mutableStateOf(listOf("", "", "", "", "", ""))

    companion object {
        val Saver: Saver<AddRecipeState, *> = listSaver(
            save = {
                listOf(
                    it.title,
                    it.url,
                    it.imageUrl,
                    it.publisher,
                    it.prepTime,
                    it.servings,
                    it.ingredients
                )
            },
            restore = {
                val state = AddRecipeState()
                state.title = it[0] as String
                state.url = it[1] as String
                state.imageUrl = it[2] as String
                state.publisher = it[3] as String
                state.prepTime = it[4] as String
                state.servings = it[5] as String
                val ingredients = it[6]
                if (ingredients is List<*>) {
                    state.ingredients = ingredients.filterIsInstance<String>()
                }

                state
            }
        )
    }
}

@Composable
fun AddRecipeTextField(
    value: String,
    onValueChange: (newValue: String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        label = label,
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
    )
}
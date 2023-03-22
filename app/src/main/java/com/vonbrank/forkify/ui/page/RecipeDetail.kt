package com.vonbrank.forkify.ui.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.ui.theme.ForkifyOrangeBackgroundDark
import java.util.*

@Composable
fun RecipeDetail(
    navController: NavController,
    recipePreview: RecipePreview
) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = recipePreview.title)
        })
    }) { paddingValue ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .padding(paddingValue)
        ) {
            item {
                RecipeInfo()
            }
            item {
                RecipeIngredients()
            }
            item {
                HowToCookSection()
            }
        }
    }
}

@Composable
fun IngredientItem(text: String) {
    Row() {
        Icon(
            imageVector = Icons.Outlined.Done, contentDescription = "Ingredient icon",
            modifier = Modifier.padding(end = 12.dp),
            tint = MaterialTheme.colors.primary
        )
        Text(text = text)
    }
}

@Composable
private fun SectionHeading(headingText: String) {
    Text(
        text = headingText.uppercase(),
        style = MaterialTheme.typography.h5,
        color = MaterialTheme.colors.primary
    )
}

@Composable
fun RecipeInfo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.weight(2f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = "Time icon",
                tint = MaterialTheme.colors.primary
            )
            Text(text = "75 Minutes".uppercase(Locale.ROOT))
        }
        Row(
            modifier = Modifier.weight(2f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.People,
                contentDescription = "Servings icon",
                tint = MaterialTheme.colors.primary
            )
            Text(text = "6 Servings".uppercase(Locale.ROOT))
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .border(
                        BorderStroke(
                            2.dp,
                            MaterialTheme.colors.primary
                        ), shape = CircleShape
                    )
                    .size(24.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add icon",
                    tint = MaterialTheme.colors.primary
                )
            }
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .border(
                        BorderStroke(
                            2.dp,
                            MaterialTheme.colors.primary
                        ), shape = CircleShape
                    )
                    .size(24.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Remove,
                    contentDescription = "Remove icon",
                    tint = MaterialTheme.colors.primary
                )
            }

        }
    }
}

@Composable
fun RecipeIngredients() {
    val ingredients = listOf(
        "lorem ipsum text.lorem ipsum text.lorem ipsum text.",
        "lorem ipsum text.lorem ipsum text.lorem ipsum text.",
        "lorem ipsum text.lorem ipsum text.lorem ipsum text.",
        "lorem ipsum text.lorem ipsum text.lorem ipsum text.",
        "lorem ipsum text.lorem ipsum text.lorem ipsum text.",
        "lorem ipsum text.lorem ipsum text.lorem ipsum text.",
        "lorem ipsum text.lorem ipsum text.lorem ipsum text.",
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                ForkifyOrangeBackgroundDark
            )
            .padding(24.dp)
    ) {

        SectionHeading(headingText = "Recipe Ingredients")

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            val mid =
                if ((ingredients.size.mod(2)) == 1)
                    (ingredients.size + 1) / 2
                else (ingredients.size) / 2
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (element in ingredients) {
                    IngredientItem(element)
                }
            }
        }
    }
}

@Composable
fun HowToCookSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(24.dp)
    ) {
        SectionHeading(headingText = "How to Cook it")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Lorem ipsum text. Lorem ipsum text. Lorem ipsum text. Lorem ipsum text. Lorem ipsum text.",
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Button(
                onClick = {},
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(text = "Direction".uppercase())
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Direction icon"
                )
            }
        }
    }
}
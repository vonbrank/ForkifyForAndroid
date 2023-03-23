package com.vonbrank.forkify.ui.page

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vonbrank.forkify.ForkifyApplication
import com.vonbrank.forkify.logic.modal.Ingredient
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.ui.component.CollapsingToolbarLayout
import com.vonbrank.forkify.ui.component.RecipePreviewImage
import com.vonbrank.forkify.ui.theme.ForkifyOrangeBackgroundDark
import com.vonbrank.forkify.ui.viewmodel.RecipeDetailViewModal
import com.vonbrank.forkify.utils.Fraction
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import java.util.*
import com.vonbrank.forkify.ui.route.RecipeDetail as RecipeDetailDestination

@Composable
fun RecipeDetail(
    navController: NavController,
    recipeDetailViewModal: RecipeDetailViewModal = viewModel(),
) {

    var recipePreview: RecipePreview? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit) {
        RecipeDetailDestination.receiveArguments(navController) { recipePreviewArg ->
            recipePreview = recipePreviewArg
        }
        if (recipePreview == null) {
            navController.popBackStack()
            Toast.makeText(
                ForkifyApplication.context,
                "Failed to navigate to recipe detail.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            recipeDetailViewModal.getRecipeDetail(recipePreview!!.id)
        }
    }
    val defaultServings = 4
    val currentServings by recipeDetailViewModal.servings.observeAsState()
    val recipeDetailData by recipeDetailViewModal.recipeDetailLiveData.observeAsState()
    val loadingRecipeDetail by recipeDetailViewModal.loadingRecipeDetail.observeAsState()

    if (recipePreview != null && recipeDetailData != null) {
        val state = rememberCollapsingToolbarScaffoldState()
        CollapsingToolbarScaffold(
            modifier = Modifier.fillMaxSize(),
            state = state,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {
                CollapsingToolbarLayout(
                    state = state.toolbarState,
                    title = recipePreview!!.title,
                    expendedHeight = 250.dp,
                    onClickMenuButton = {
                        navController.popBackStack()
                    }
                ) {
                    RecipePreviewImage(
                        imageUrl = recipePreview!!.imageUrl,
                        modifier = Modifier.background(MaterialTheme.colors.primary)
                    )
                }
            }) {

            if (loadingRecipeDetail == true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.surface)
                ) {
                    item {
                        RecipeInfo(
                            servings = currentServings ?: defaultServings,
                            time = recipeDetailData!!.cookingTime,
                            onServingsChange = { newValue ->
                                if (newValue > 0) {
                                    recipeDetailViewModal.servings.value = newValue
                                }
                            }
                        )
                    }
                    item {
                        RecipeIngredients(
                            ingredients = recipeDetailData!!.ingredients,
                            currentServings = currentServings ?: defaultServings,
                            initialServings = recipeDetailData!!.servings
                        )
                    }
                    item {
                        HowToCookSection(
                            recipeDetailData!!.publisher,
                            recipeDetailData!!.sourceUrl
                        )
                    }
                }
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
fun RecipeInfo(servings: Int, time: Double, onServingsChange: (newValue: Int) -> Unit = {}) {
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
            Text(text = "${time.toInt()} Minutes".uppercase(Locale.ROOT))
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
            Text(text = "$servings Servings".uppercase(Locale.ROOT))
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = {
                    onServingsChange(servings + 1)
                },
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
                onClick = {
                    onServingsChange(servings - 1)
                },
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
fun RecipeIngredients(ingredients: List<Ingredient>, initialServings: Int, currentServings: Int) {
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
                for (ingredient in ingredients) {
                    val countString =
                        if (ingredient.quantity != null) "${Fraction(ingredient.quantity * currentServings / initialServings.toDouble())} " else ""
                    val unitString =
                        if (ingredient.unit != null && ingredient.unit.isNotEmpty()) "${ingredient.unit} " else ""
                    val descriptionString = ingredient.description ?: ""
                    IngredientItem("${countString}${unitString}${descriptionString}")
                }
            }
        }
    }
}

@Composable
fun HowToCookSection(publisher: String, sourceUrl: String) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(24.dp)
    ) {
        SectionHeading(headingText = "How to Cook it")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "This recipe was carefully designed and tested by ${publisher}. Please check out directions at their website.",
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(sourceUrl)
                    context.startActivity(intent)
                },
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
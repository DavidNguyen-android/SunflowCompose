package com.google.samples.apps.sunflower

import androidx.annotation.PluralsRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.android.material.color.MaterialColors
import com.google.samples.apps.sunflower.data.PlantAndGardenPlantings
import com.google.samples.apps.sunflower.viewmodels.GardenPlantingListViewModel
import com.google.samples.apps.sunflower.viewmodels.PlantAndGardenPlantingsViewModel


@ExperimentalFoundationApi
@Composable
fun GardenCompose(
    gardenPlantingListViewModel: GardenPlantingListViewModel, nav: NavController,
    onAddPlantClick: () -> Unit
) {
    // Observes values coming from the VM's LiveData<Plant> field
    val plantAndGardenPlantings by gardenPlantingListViewModel.plantAndGardenPlantings.observeAsState()

    // If plant is not null, display the content
    plantAndGardenPlantings?.let {
        PlantAndGardenPlantingsContent(it, nav, onAddPlantClick)
    }
}

@ExperimentalFoundationApi
@Composable
fun PlantAndGardenPlantingsContent(
    plantAndGardenPlantings: List<PlantAndGardenPlantings>,
    nav: NavController,
    onAddPlantClick: () -> Unit
) {
    ListGardenPlanting(plantAndGardenPlantings, nav)
    GardenEmpty(hasPlantings = !plantAndGardenPlantings.isNullOrEmpty(), onAddPlantClick)
}

@ExperimentalFoundationApi
@Composable
fun ListGardenPlanting(plantAndGardenPlantings: List<PlantAndGardenPlantings>, nav: NavController) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2)
    ) {
        items(plantAndGardenPlantings) { item ->
            ListItemGardenPlantingContent(item, nav)
        }
    }
}

@Composable
fun ListItemGardenPlantingContent(
    plantAndGardenPlantings: PlantAndGardenPlantings,
    nav: NavController
) {
    val viewModel = PlantAndGardenPlantingsViewModel(plantAndGardenPlantings)
    Card(
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 0.dp
        ),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable(
                onClick = {
                    val direction = HomeViewPagerFragmentDirections
                        .actionViewPagerFragmentToPlantDetailFragment(viewModel.plantId)
                    nav.navigate(direction)
                }
            )
    ) {
        Column {
            Image(
                painter = rememberImagePainter(viewModel.imageUrl),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(95.dp)
            )
            Box(Modifier.padding(top = 16.dp)) {
                Text(
                    text = viewModel.plantName,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.margin_small))
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
            Box(Modifier.padding(top = 16.dp)) {
                Text(
                    text = stringResource(R.string.plant_date_header),
                    style = MaterialTheme.typography.subtitle2,
                    color = getThemeAccentColor(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.margin_small))
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
            Text(
                text = viewModel.plantDateString,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.margin_small))
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            Box(Modifier.padding(top = 16.dp)) {
                Text(
                    text = stringResource(R.string.watered_date_header),
                    color = getThemeAccentColor(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.margin_small))
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
            Text(
                text = viewModel.waterDateString,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.margin_small))
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            Box(Modifier.padding(bottom = 16.dp)) {
                Text(
                    text = quantityStringResource(
                        id = R.plurals.watering_next,
                        viewModel.wateringInterval,
                        viewModel.wateringInterval
                    ),
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.margin_small))
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }

}

@Composable
fun GardenEmpty(hasPlantings: Boolean, onClick: () -> Unit) {
    if (!hasPlantings) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.garden_empty),
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.margin_small))
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            Row {
                // Create a Main Button or Normal Button
                Button(
                    onClick = onClick,
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = getThemeSecondaryColor()),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 0.dp
                    )
                ) {
                    Text(text = stringResource(id = R.string.add_plant))
                }
            }
        }
    }
}

@Composable
fun getThemeAccentColor(): Color {
    return Color(
        MaterialColors.getColor(
            LocalContext.current,
            R.attr.colorAccent,
            ContextCompat.getColor(LocalContext.current, android.R.color.black)
        )
    )
}

@Composable
fun getThemeSecondaryColor(): Color {
    return Color(
        MaterialColors.getColor(
            LocalContext.current,
            R.attr.colorSecondary,
            ContextCompat.getColor(LocalContext.current, android.R.color.black)
        )
    )
}

@Composable
fun quantityStringResource(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): String {
    return LocalContext.current.resources.getQuantityString(id, quantity, *formatArgs)
}
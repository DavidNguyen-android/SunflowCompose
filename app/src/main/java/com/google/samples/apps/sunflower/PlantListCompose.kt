package com.google.samples.apps.sunflower

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.viewmodels.PlantListViewModel


@ExperimentalFoundationApi
@Composable
fun PlanListCompose(
    plantingListViewModel: PlantListViewModel, nav: NavController
) {
    // Observes values coming from the VM's LiveData<Plant> field
    val plantAndGardenPlantings by plantingListViewModel.plants.observeAsState()

    // If plant is not null, display the content
    plantAndGardenPlantings?.let {
        PlantListContent(it, nav)
    }
}

@ExperimentalFoundationApi
@Composable
fun PlantListContent(
    plants: List<Plant>,
    nav: NavController
) {
    ListPlants(plants, nav)
}

@ExperimentalFoundationApi
@Composable
fun ListPlants(plants: List<Plant>, nav: NavController) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2)
    ) {
        items(plants) { item ->
            ListItemPlantContent(item, nav)
        }
    }
}

@Composable
fun ListItemPlantContent(
    plant: Plant,
    nav: NavController
) {
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
                    val direction =
                        HomeViewPagerFragmentDirections.actionViewPagerFragmentToPlantDetailFragment(
                            plant.plantId
                        )
                    nav.navigate(direction)
                }
            )
    ) {
        Column {
            Image(
                painter = rememberImagePainter(plant.imageUrl),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(95.dp)
            )
            Box(Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.margin_small))
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }

        }
    }

}
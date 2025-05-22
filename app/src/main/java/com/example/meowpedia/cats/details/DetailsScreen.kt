package com.example.meowpedia.cats.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil3.compose.AsyncImage
import com.example.meowpedia.cats.details.Details.DetailsUiState
import androidx.compose.ui.layout.ContentScale
import com.example.meowpedia.core.compose.CharacteristicBar
import com.example.meowpedia.core.compose.LoadingIndicator
import com.example.meowpedia.core.compose.NoDataContent
import com.example.meowpedia.core.compose.SectionChip
import com.example.meowpedia.core.compose.SectionText
import androidx.compose.material3.MaterialTheme.colorScheme

fun NavGraphBuilder.breedDetails(
    route: String,
    onClose: () -> Unit,
) = composable(
    route = route,
) { navBackStackEntry ->
    val breedId = navBackStackEntry.arguments?.getString("Id")
        ?: throw IllegalStateException("breedId required")

    val detailsViewModel = viewModel<DetailsViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailsViewModel(breedId = breedId) as T
            }
        }
    )

    val state = detailsViewModel.state.collectAsState()

    DetailsScreen(
        state = state.value,
        onClose = onClose,
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailsScreen(
    state: DetailsUiState,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = state.data?.name ?: "Loading")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primaryContainer,
                    scrolledContainerColor = colorScheme.primaryContainer,
                ),
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = colorScheme.primary
                        )
                    }
                }

            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (state.fetching) {
                    LoadingIndicator()
                } else if (state.error != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        val errorMessage = when (state.error) {
                            is DetailsUiState.DetailsError.DataUpdateFailed ->
                                "Failed to load. Error message: ${state.error.cause?.message}."
                        }
                        Text(text = errorMessage)
                    }
                } else if (state.data != null) {
                    val data = state.data
                    val uriHandler = LocalUriHandler.current

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .padding(16.dp)
                        ) {
                            AsyncImage(
                                model = data.image,
                                contentDescription = "${data.name} image",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(2.dp, colorScheme.secondary, RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Fit
                            )


                        }

                        SectionText("Description", data.description)
                        SectionChip("Origin", data.origin)
                        SectionChip("Life Span", "${data.life_span} years")
                        SectionChip("Weight", "${data.weight.metric} kg / ${data.weight.imperial} lbs")

                        if (data.rare > 0) {
                            SectionChip("Rarity", "Rare Breed")
                        }

                        SectionText("Temperaments")
                        FlowRow(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            data.temperament.split(",").map { it.trim() }.forEach { temperament ->
                                AssistChip(
                                    onClick = {},
                                    label = { Text(text = temperament) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = Color.Transparent,
                                        labelColor = colorScheme.onSurface,
                                        leadingIconContentColor = colorScheme.onSurfaceVariant,
                                        trailingIconContentColor = colorScheme.onSurfaceVariant,
                                        disabledContainerColor = Color.Transparent,
                                        disabledLabelColor = colorScheme.outline,
                                        disabledLeadingIconContentColor = colorScheme.outline,
                                        disabledTrailingIconContentColor = colorScheme.outline,
                                    ),
                                    border = BorderStroke(1.dp, colorScheme.secondary)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        CharacteristicBar(label = "Adaptability", level = data.adaptability)
                        CharacteristicBar(label = "Affection Level", level = data.affection_level)
                        CharacteristicBar(label = "Child Friendly", level = data.child_friendly)
                        CharacteristicBar(label = "Dog Friendly", level = data.dog_friendly)
                        CharacteristicBar(label = "Energy Level", level = data.energy_level)

                        Spacer(modifier = Modifier.height(24.dp))

                        data.wikipedia_url?.let {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(
                                    onClick = { uriHandler.openUri(it) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorScheme.secondary,
                                        contentColor = colorScheme.onPrimary
                                    )
                                ) {
                                    Icon(Icons.Filled.Info, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Learn More on Wikipedia")
                                }
                            }
                        }
                    }
                } else {
                    NoDataContent("There is no data for this cat")
                }
            }
        }
    )
}

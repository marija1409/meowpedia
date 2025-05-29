package com.example.meowpedia.cats.list

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil3.compose.AsyncImage
import com.example.meowpedia.cats.list.CatList.CatListState
import com.example.meowpedia.cats.list.CatList.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meowpedia.core.compose.LoadingIndicator
import com.example.meowpedia.core.compose.TemperamentChips



@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.cats(
    route: String,
    onBreedClick: (String) -> Unit,
) = composable(
    route = route
) {

    val breedListViewModel: CatListViewModel = hiltViewModel()


    val state = breedListViewModel.state.collectAsState()

    BreedListScreen(
        state = state.value,
        eventPublisher = {
            breedListViewModel.setEvent(it)
        },
        onBreedClick = onBreedClick,
    )
}

@ExperimentalMaterial3Api
@Composable
fun BreedListScreen(
    state: CatListState,
    eventPublisher: (uiEvent: CatListUIEvent) -> Unit,
    onBreedClick: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Breeds",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            })
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {

                OutlinedTextField(
                    value = state.query,
                    onValueChange = {
                        eventPublisher(CatListUIEvent.SearchQueryChanged(it))
                    },
                    label = { Text("Search") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            eventPublisher(CatListUIEvent.ClearSearch)
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Clear Search"
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.surface,
                        unfocusedContainerColor = colorScheme.surface,
                        focusedTextColor = colorScheme.primary,
                        unfocusedTextColor = colorScheme.primary,
                        focusedPlaceholderColor = colorScheme.primary,
                        unfocusedPlaceholderColor = colorScheme.primary,
                        unfocusedTrailingIconColor = colorScheme.primary,
                        focusedTrailingIconColor = colorScheme.primary,
                        focusedIndicatorColor = colorScheme.primary,
                        unfocusedIndicatorColor = colorScheme.secondary,
                        cursorColor = colorScheme.primary,
                        focusedLeadingIconColor = colorScheme.primary,
                        unfocusedLeadingIconColor = colorScheme.primary,
                        focusedLabelColor = colorScheme.primary,
                        unfocusedLabelColor = colorScheme.onSurface.copy(alpha = 0.6f),
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                when {
                    state.error != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            val errorMessage = when (state.error) {
                                is ListError.ListUpdateFailed ->
                                    "Failed to load. Please try again later. Error message: ${state.error.cause?.message}."
                            }
                            Text(text = errorMessage, color = colorScheme.error)
                        }
                    }

                    state.loading -> {
                        LoadingIndicator()
                    }

                    else -> {
                        val breedsToShow = if (state.query.isNotEmpty()) state.filteredBreeds else state.breeds
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(
                                items = breedsToShow,
                                key = { it.id }
                            ) { breed ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp)
                                        .padding(bottom = 16.dp)
                                        .clickable { onBreedClick(breed.id) },
                                    colors = CardDefaults.cardColors(
                                        containerColor = colorScheme.primary.copy(alpha = 0.1f)
                                    ),
                                ) {
                                    Column {
                                        breed.image?.let {
                                            AsyncImage(
                                                model = it,
                                                contentDescription = "${breed.name} image",
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .border(
                                                        2.dp,
                                                        colorScheme.secondary,
                                                        RoundedCornerShape(12.dp)
                                                    ),
                                            )
                                        }
                                        Text(
                                            text = breed.name,
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                color = colorScheme.primary
                                            ),
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .padding(top = 12.dp)
                                        )

                                        if (!breed.alternativeNames.isNullOrEmpty()) {
                                            Text(
                                                text = "Also known as: ${breed.alternativeNames}",
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = colorScheme.onBackground
                                            )
                                        }

                                        Text(
                                            text = breed.description.take(250),
                                            modifier = Modifier
                                                .padding(horizontal = 16.dp, vertical = 8.dp),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = colorScheme.onBackground
                                        )
                                        if (breed.temperaments.isNotBlank()) {
                                            TemperamentChips(
                                                temperamentString = breed.temperaments,
                                                maxTemperaments = 3,
                                                modifier = Modifier
                                                    .padding(16.dp)
                                                    .fillMaxWidth()
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

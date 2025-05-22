package com.example.meowpedia.core.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.foundation.layout.FlowRow



@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TemperamentChips(
    temperamentString: String,
    maxTemperaments: Int? = null,
    modifier: Modifier = Modifier
) {
    val temperaments = temperamentString.split(",").map { it.trim() }
    val temperamentsToShow = maxTemperaments?.let { temperaments.take(it) } ?: temperaments

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        temperamentsToShow.forEach { temperament ->
            AssistChip(
                onClick = {},
                label = { Text(text = temperament) },
                shape = RoundedCornerShape(12.dp),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color.Transparent,
                    labelColor = colorScheme.onSurface,
                    leadingIconContentColor = colorScheme.onSurface,
                    trailingIconContentColor = colorScheme.onSurface,
                    disabledContainerColor = Color.Transparent,
                    disabledLabelColor = colorScheme.onSurface.copy(alpha = 0.4f),
                    disabledLeadingIconContentColor = colorScheme.onSurface.copy(alpha = 0.4f),
                    disabledTrailingIconContentColor = colorScheme.onSurface.copy(alpha = 0.4f)
                ),
                border = BorderStroke(1.dp, colorScheme.secondary)
            )
        }
    }
}

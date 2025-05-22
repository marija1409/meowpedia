package com.example.meowpedia.core.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun CharacteristicBar(label: String, level: Int) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "$label: $level/5",
            color = MaterialTheme.colorScheme.primary
        )
        LinearProgressIndicator(
            progress = { level / 5f },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = Color.Gray,
            strokeCap = StrokeCap.Round,
        )
    }
}
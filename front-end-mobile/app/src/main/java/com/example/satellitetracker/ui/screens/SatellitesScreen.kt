package com.example.satellitetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.satellitetracker.data.Repository
import com.example.satellitetracker.data.UiState
import com.example.satellitetracker.data.apiErrorMessage
import com.example.satellitetracker.data.model.Satellite
import com.example.satellitetracker.ui.components.ErrorState
import com.example.satellitetracker.ui.components.LoadingState
import com.example.satellitetracker.ui.components.SatelliteCard
import com.example.satellitetracker.ui.components.ScreenHeader
import com.example.satellitetracker.ui.components.SpaceBackground

/**
 * Lista de satélites públicos vinda da API (GET /satelites), com pesquisa pelo nome.
 * Cada card mostra em que região do mundo o satélite está voando.
 */
@Composable
fun SatellitesScreen(onBack: () -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var reloadKey by remember { mutableStateOf(0) }
    var state by remember { mutableStateOf<UiState<List<Satellite>>>(UiState.Loading) }

    // Carrega os satélites do backend (recarrega quando reloadKey muda).
    LaunchedEffect(reloadKey) {
        state = UiState.Loading
        state = try {
            UiState.Success(Repository.getSatellites())
        } catch (e: Exception) {
            UiState.Error(apiErrorMessage(e))
        }
    }

    SpaceBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ScreenHeader(title = "Satélites públicos 🌍", onBack = onBack)
            Text(
                text = "Veja em que região do mundo cada satélite está voando agora",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Pesquisar satélite pelo nome") },
                leadingIcon = { Text("🔍") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            when (val current = state) {
                is UiState.Loading -> LoadingState()
                is UiState.Error -> ErrorState(message = current.message) { reloadKey++ }
                is UiState.Success -> {
                    val filtered = current.data.filter {
                        it.name.contains(query.trim(), ignoreCase = true)
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(filtered, key = { it.id }) { satellite ->
                            SatelliteCard(satellite = satellite)
                        }
                        if (filtered.isEmpty()) {
                            item {
                                Text(
                                    text = "Nenhum satélite encontrado para \"$query\" 🌌",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

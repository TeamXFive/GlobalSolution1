package com.example.satellitetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satellitetracker.data.HomeData
import com.example.satellitetracker.data.MockData
import com.example.satellitetracker.data.Repository
import com.example.satellitetracker.data.UiState
import com.example.satellitetracker.data.apiErrorMessage
import com.example.satellitetracker.data.model.Satellite
import com.example.satellitetracker.data.model.SatellitePass
import com.example.satellitetracker.ui.components.ErrorState
import com.example.satellitetracker.ui.components.InfoRow
import com.example.satellitetracker.ui.components.LoadingState
import com.example.satellitetracker.ui.components.PrimaryActionButton
import com.example.satellitetracker.ui.components.SatelliteCard
import com.example.satellitetracker.ui.components.SensorStatusCard
import com.example.satellitetracker.ui.components.SpaceBackground
import com.example.satellitetracker.ui.components.SpaceCard
import java.text.Normalizer

@Composable
fun HomeScreen(
    onOpenSatellites: () -> Unit,
    onOpenAlerts: () -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var searchedLocation by rememberSaveable { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current

    // Dados vindos da API (satélites + próxima passagem + condição do céu).
    var reloadKey by remember { mutableStateOf(0) }
    var satellites by remember { mutableStateOf<List<Satellite>>(emptyList()) }
    var homeData by remember { mutableStateOf<HomeData?>(null) }
    var loadState by remember { mutableStateOf<UiState<Unit>>(UiState.Loading) }

    LaunchedEffect(reloadKey) {
        loadState = UiState.Loading
        loadState = try {
            satellites = Repository.getSatellites()
            homeData = Repository.getHomeData()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Error(apiErrorMessage(e))
        }
    }

    fun searchRegion(region: String = searchText) {
        val location = region.trim().ifBlank { MockData.DEFAULT_LOCATION }
        searchText = location
        searchedLocation = location
        keyboard?.hide()
    }

    SpaceBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "🛰️", fontSize = 44.sp)
            Text(
                text = "Tem Satélite Passando\npor Mim Agora?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Pesquise sua região e descubra quais satélites vão cruzar o seu céu",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    if (it.isBlank()) searchedLocation = ""
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ex: ${MockData.DEFAULT_LOCATION}") },
                leadingIcon = { Text("📍") },
                trailingIcon = {
                    TextButton(onClick = { searchRegion() }) { Text("Buscar") }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { searchRegion() })
            )

            when (val s = loadState) {
                is UiState.Loading -> LoadingState()
                is UiState.Error -> ErrorState(message = s.message) { reloadKey++ }
                is UiState.Success -> {
                    if (searchedLocation.isBlank()) {
                        RegionSuggestions(
                            title = if (searchText.isBlank()) "Sugestões: regiões e satélites" else "Resultados da busca",
                            suggestions = buildSuggestions(searchText, satellites),
                            onSelect = { searchRegion(it) }
                        )
                        SatelliteGallery()
                    } else {
                        val satellite = satellites.find {
                            it.name.semAcento() == searchedLocation.semAcento()
                        }
                        if (satellite != null) {
                            Text(
                                text = "Onde o ${satellite.name} está agora",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.fillMaxWidth()
                            )
                            SatelliteCard(satellite = satellite)
                            PrimaryActionButton(text = "Ver todos os satélites", onClick = onOpenSatellites)
                            PrimaryActionButton(
                                text = "Ver alertas e sensores",
                                tonal = true,
                                onClick = onOpenAlerts
                            )
                        } else {
                            Text(
                                text = "Resultados para $searchedLocation",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.fillMaxWidth()
                            )
                            val pass = homeData?.nextPass
                            if (pass != null) {
                                NextPassCard(pass = pass)
                            } else {
                                Text(
                                    text = "Sem passagem prevista para esta região agora 🌌",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            homeData?.reading?.let { SensorStatusCard(reading = it) }
                            PrimaryActionButton(text = "Ver satélites próximos", onClick = onOpenSatellites)
                            PrimaryActionButton(
                                text = "Ver alertas e sensores",
                                tonal = true,
                                onClick = onOpenAlerts
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private data class SearchSuggestion(val label: String, val emoji: String)

/** Monta as sugestões a partir do texto digitado e dos satélites carregados. */
private fun buildSuggestions(searchText: String, satellites: List<Satellite>): List<SearchSuggestion> {
    if (searchText.isBlank()) {
        return MockData.SUGGESTED_LOCATIONS.take(6).map { SearchSuggestion(it, "📍") }
    }
    val query = searchText.trim().semAcento()
    val satMatches = satellites
        .filter { it.name.semAcento().contains(query) }
        .map { SearchSuggestion(it.name, "🛰️") }
    val regionMatches = MockData.SUGGESTED_LOCATIONS
        .filter { it.semAcento().contains(query) }
        .map { SearchSuggestion(it, "📍") }
    return satMatches + regionMatches
}

private fun String.semAcento(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .lowercase()

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RegionSuggestions(
    title: String,
    suggestions: List<SearchSuggestion>,
    onSelect: (String) -> Unit
) {
    if (suggestions.isEmpty()) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            suggestions.forEach { suggestion ->
                AssistChip(
                    onClick = { onSelect(suggestion.label) },
                    label = { Text(suggestion.label) },
                    leadingIcon = { Text(suggestion.emoji) }
                )
            }
        }
    }
}

@Composable
private fun NextPassCard(pass: SatellitePass) {
    SpaceCard {
        Text(
            text = "🛰️ Próxima passagem",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        InfoRow(label = "Satélite", value = pass.satelliteName)
        InfoRow(label = "Horário", value = pass.time)
        InfoRow(label = "Direção", value = pass.direction)
        InfoRow(label = "Visível", value = if (pass.isVisible) "Sim" else "Não")
    }
}

@Composable
private fun SatelliteGallery() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GalleryItem(emoji = "🛰️", name = "ISS", modifier = Modifier.weight(1f))
            GalleryItem(emoji = "📡", name = "Sentinel-2", modifier = Modifier.weight(1f))
            GalleryItem(emoji = "🚀", name = "Amazonia-1", modifier = Modifier.weight(1f))
        }
        Text(
            text = "Pesquise uma região para ver a próxima passagem 🌌",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GalleryItem(emoji: String, name: String, modifier: Modifier = Modifier) {
    SpaceCard(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = 32.sp)
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

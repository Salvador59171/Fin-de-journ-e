package com.example.findejournee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import android.content.Context

private val Context.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

@Composable
fun App() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface(Modifier.fillMaxSize()) {
            HomeScreen()
        }
    }
}

private val KEY_HOUR = intPreferencesKey("arr_hour")
private val KEY_MIN = intPreferencesKey("arr_min")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val ds = remember { ctx.dataStore }

    // Charger valeur mémorisée
    var arrival by remember { mutableStateOf(LocalTime.of(9, 0)) }
    LaunchedEffect(Unit) {
        val prefs = ds.data.first()
        val h = prefs[KEY_HOUR]
        val m = prefs[KEY_MIN]
        if (h != null && m != null) arrival = LocalTime.of(h, m)
    }

    val formatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    val departure = remember(arrival) { arrival.plusHours(8).plusMinutes(27) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = stringResource(id = R.string.subtitle),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(stringResource(id = R.string.arrival_label), fontWeight = FontWeight.SemiBold)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            val initialHour = arrival.hour
                            val initialMinute = arrival.minute
                            android.app.TimePickerDialog(
                                ctx,
                                { _, h, m ->
                                    arrival = LocalTime.of(h, m)
                                    scope.launch {
                                        ds.edit { it[KEY_HOUR] = h; it[KEY_MIN] = m }
                                    }
                                },
                                initialHour,
                                initialMinute,
                                true
                            ).show()
                        }
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Text(arrival.format(formatter), fontSize = 22.sp)
                }

                Divider()

                Text(stringResource(id = R.string.rule_label), color = MaterialTheme.colorScheme.primary)
                Text(
                    text = stringResource(id = R.string.departure_result, departure.format(formatter)),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        Text(stringResource(id = R.string.footer_hint), style = MaterialTheme.typography.labelMedium)
    }
}

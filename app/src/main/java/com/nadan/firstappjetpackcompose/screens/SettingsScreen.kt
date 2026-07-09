package com.nadan.firstappjetpackcompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var emailUpdatesEnabled by remember { mutableStateOf(false) }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), MaterialTheme.colorScheme.surface)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Paramètres", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        modifier = Modifier.background(backgroundBrush)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Préférences",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp)
            )

            SettingsCard {
                SettingsRow(
                    icon = Icons.Rounded.Notifications,
                    title = "Notifications",
                    subtitle = "Recevoir des rappels pour vos tâches",
                    action = {
                        Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                SettingsRow(
                    icon = Icons.Rounded.Email,
                    title = "Mises à jour par email",
                    subtitle = "Résumé hebdomadaire de votre productivité",
                    action = {
                        Switch(checked = emailUpdatesEnabled, onCheckedChange = { emailUpdatesEnabled = it })
                    }
                )
            }

            Text(
                "Compte",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )

            SettingsCard {
                SettingsRow(
                    icon = Icons.Rounded.Person,
                    title = "Profil",
                    subtitle = "Modifier vos informations personnelles",
                    onClick = { /* Action */ }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                SettingsRow(
                    icon = Icons.Rounded.Security,
                    title = "Sécurité",
                    subtitle = "Changer votre mot de passe",
                    onClick = { /* Action */ }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                "Version 1.0.0 (naDan Build)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        content = content
    )
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    action: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val modifier = if (onClick != null) Modifier.fillMaxWidth().height(72.dp).background(Color.Transparent).padding(horizontal = 16.dp)
    else Modifier.fillMaxWidth().height(72.dp).padding(horizontal = 16.dp)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        action?.invoke()
    }
}

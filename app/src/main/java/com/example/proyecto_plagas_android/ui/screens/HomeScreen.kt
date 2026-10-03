package com.example.proyecto_plagas_android.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.proyecto_plagas_android.R
import com.example.proyecto_plagas_android.ui.state.AgriculturalNews
import com.example.proyecto_plagas_android.ui.state.AlertType
import com.example.proyecto_plagas_android.ui.state.DashboardUiState
import com.example.proyecto_plagas_android.ui.state.PestAlert
import com.example.proyecto_plagas_android.ui.viewmodel.AgroScanViewModel

// --- Custom Colors based on the provided image ---
private val CreamBackground = Color(0xFFF9FAEF)
private val DarkGreenText = Color(0xFF163E21)
private val GreenBar = Color(0xFF4CAF50)
private val RedBar = Color(0xFFEF5350)
private val BadgeBg = Color(0xFFE9ECE3)
private val OrangeWarning = Color(0xFFFF7043)
private val BlueInfo = Color(0xFF42A5F5)
private val GreenTip = Color(0xFF66BB6A)
private val ChartLineGreen = Color(0xFF81C784)
private val ChartFillGreen = Color(0xFFC8E6C9).copy(alpha = 0.4f)
private val NavBg = Color(0xFFF1F3E9)

@Composable
fun HomeScreen(
    viewModel: AgroScanViewModel,
    onNavigateToCapture: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToResult: (String) -> Unit,
) {
    val dashboardUiState by viewModel.dashboardUiState.collectAsState()

    Scaffold(
        containerColor = CreamBackground,
        bottomBar = {
            CustomBottomNavigationBar(
                onHomeClick = { /* Already here */ },
                onCameraClick = onNavigateToCapture,
                onSettingsClick = onNavigateToSettings,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Header with Official Logo & Weather
            item {
                HeaderCard()
            }

            // Crop Health Overview
            item {
                HealthStatusCard(dashboardUiState)
            }

            // Giant Camera Scan Action
            item {
                ScanActionCard(onClick = onNavigateToCapture)
            }

            // Field Weather & Crop Data Section
            item {
                CropFieldEnvironmentCard()
            }

            // Statistics Section
            item {
                Text(
                    text = "Estadísticas del Campo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkGreenText,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            item {
                FieldStatisticsGrid(dashboardUiState)
            }

            // Regional Pest Alerts
            item {
                Text(
                    text = "Avisos de la Zona",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkGreenText,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            item {
                AlertsList(dashboardUiState.activePestAlerts)
            }

            // Regional News Section
            item {
                Text(
                    text = "Noticias Agrícolas de la Zona",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkGreenText,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            item {
                RegionalNewsSection(dashboardUiState.regionalNews)
            }
        }
    }
}

@Composable
private fun HeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Official App Logo Image
                    Image(
                        painter = painterResource(id = R.drawable.ic_app_logo),
                        contentDescription = "Logo AgroScan AI",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "¡Hola, Agricultor!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkGreenText,
                        )
                        Text(
                            text = "AgroScan AI • ONNX Mobile",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BadgeBg,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = null,
                            tint = DarkGreenText,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Listo sin\ninternet",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreenText,
                            lineHeight = MaterialTheme.typography.labelSmall.lineHeight * 0.9f,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = "Soleado",
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(40.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Día Soleado, 28°C",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                    Text(
                        text = "Ideal para riego y pulverización",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray,
                    )
                }

                // Mini line chart
                MiniWeatherChart(modifier = Modifier.width(70.dp).height(30.dp))
            }
        }
    }
}

@Composable
private fun CropFieldEnvironmentCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Información del Suelo y Parcela",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DarkGreenText,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                EnvironmentItem(
                    icon = Icons.Default.WaterDrop,
                    iconTint = BlueInfo,
                    label = "Humedad Suelo",
                    value = "64%",
                )
                EnvironmentItem(
                    icon = Icons.Default.Thermostat,
                    iconTint = OrangeWarning,
                    label = "Temp. Suelo",
                    value = "22°C",
                )
                EnvironmentItem(
                    icon = Icons.Default.Psychology,
                    iconTint = GreenBar,
                    label = "Inferencia IA",
                    value = "ONNX 377ms",
                )
            }
        }
    }
}

@Composable
private fun EnvironmentItem(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = CircleShape,
            color = iconTint.copy(alpha = 0.15f),
            modifier = Modifier.size(32.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
            )
        }
    }
}

@Composable
private fun FieldStatisticsGrid(uiState: DashboardUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = null,
                    tint = DarkGreenText,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${uiState.totalScansCount} revisiones",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkGreenText,
                )
                Text(
                    text = "Totales este mes",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                )
            }
        }

        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "🌱 Cultivos",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreenText,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Café & Maíz",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                )
                Text(
                    text = "Principales revisados",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                )
            }
        }
    }
}

@Composable
private fun RegionalNewsSection(newsList: List<AgriculturalNews>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        newsList.forEach { newsItem ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GreenBar.copy(alpha = 0.15f),
                        ) {
                            Text(
                                text = newsItem.category,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = DarkGreenText,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            )
                        }
                        Text(
                            text = newsItem.date,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Article,
                            contentDescription = null,
                            tint = DarkGreenText,
                            modifier = Modifier.size(22.dp).offset(y = 2.dp),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = newsItem.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = newsItem.summary,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.DarkGray,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniWeatherChart(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val path = Path()
        val w = size.width
        val h = size.height

        val points = listOf(
            Offset(0f, h * 0.7f),
            Offset(w * 0.2f, h * 0.5f),
            Offset(w * 0.4f, h * 0.1f),
            Offset(w * 0.6f, h * 0.4f),
            Offset(w * 0.8f, h * 0.6f),
            Offset(w, h * 0.3f),
        )

        path.moveTo(points.first().x, points.first().y)
        for (i in 1 until points.size) {
            path.lineTo(points[i].x, points[i].y)
        }

        // Draw Line
        drawPath(
            path = path,
            color = ChartLineGreen,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
        )

        // Draw fill
        val fillPath = Path().apply {
            addPath(path)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(fillPath, color = ChartFillGreen)

        // Vertical faint lines
        for (i in 1..4) {
            val lx = w * (i / 5f)
            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = Offset(lx, 0f),
                end = Offset(lx, h),
                strokeWidth = 1.dp.toPx(),
            )
        }
    }
}

@Composable
private fun HealthStatusCard(uiState: DashboardUiState) {
    val healthyRatio = if (uiState.totalScansCount > 0) uiState.overallHealthPercentage else 80
    val sickRatio = 100 - healthyRatio

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CreamBackground),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Salud del Cultivo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Cultivo Sano",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkGreenText,
                )
                Text(
                    text = "$healthyRatio%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = GreenBar,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Atención Necesaria",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$sickRatio%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = RedBar,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth().height(14.dp)) {
                Box(
                    modifier = Modifier
                        .weight(healthyRatio.toFloat())
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                        .background(GreenBar),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .weight(sickRatio.toFloat().coerceAtLeast(1f))
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                        .background(RedBar.copy(alpha = 0.8f)),
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Buen estado general. ${uiState.diseasedCropsCount} parcelas necesitan revisión.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
            )
        }
    }
}

@Composable
private fun ScanActionCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                shape = CircleShape,
                color = DarkGreenText,
                modifier = Modifier.size(110.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Cámara",
                        tint = Color.White,
                        modifier = Modifier.size(54.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "ESCANEAR HOJA\nENFERMA",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = DarkGreenText,
                textAlign = TextAlign.Center,
                lineHeight = MaterialTheme.typography.titleLarge.lineHeight * 0.9f,
            )
        }
    }
}

@Composable
private fun AlertsList(alerts: List<PestAlert>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        alerts.forEach { alert ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BadgeBg),
                elevation = CardDefaults.cardElevation(0.dp),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    val (icon, color) = when (alert.type) {
                        AlertType.WARNING -> Icons.Default.Warning to OrangeWarning
                        AlertType.INFO -> Icons.Default.Info to BlueInfo
                        AlertType.TIP -> Icons.Default.Lightbulb to GreenTip
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(28.dp).offset(y = 2.dp),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = alert.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )
                        Text(
                            text = alert.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(top = 2.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomBottomNavigationBar(
    onHomeClick: () -> Unit,
    onCameraClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        // Bottom Bar Background
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp),
            color = NavBg,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            shadowElevation = 8.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Home Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onHomeClick() },
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Inicio",
                        tint = DarkGreenText,
                        modifier = Modifier.size(28.dp),
                    )
                    Text(
                        text = "Inicio",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreenText,
                    )
                }

                // Spacer for center FAB
                Spacer(modifier = Modifier.width(60.dp))

                // Settings Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onSettingsClick() },
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp),
                    )
                    Text(
                        text = "Ajustes",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }
            }
        }

        // Center Floating Camera Button
        FloatingActionButton(
            onClick = onCameraClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(70.dp),
            shape = CircleShape,
            containerColor = DarkGreenText,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 8.dp,
            ),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Cámara",
                    modifier = Modifier.size(24.dp),
                )
                Text(
                    text = "Cámara",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

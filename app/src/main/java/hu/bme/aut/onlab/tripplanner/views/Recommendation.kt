package hu.bme.aut.onlab.tripplanner.views

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.onlab.tripplanner.data.recommender.RecommendationResult
import hu.bme.aut.onlab.tripplanner.views.theme.BrandPrimary
import hu.bme.aut.onlab.tripplanner.views.theme.OnSurface

@Composable
fun RecommendationView(
    items: List<RecommendationResult>,
    onSelect: (RecommendationResult) -> Unit,
    onFeedback: (RecommendationResult, liked: Boolean) -> Unit
) {
    var selected by remember { mutableStateOf<RecommendationResult?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 50.dp),
    ) {

        Text(
            text = "Ajánlott helyek neked",
            style = MaterialTheme.typography.h5,
            color = BrandPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { rec ->
                RecommendationCard(
                    rec = rec,
                    onSelect = { selected = rec },
                    onFeedback = onFeedback
                )
            }
        }
    }

    if (selected != null) {
        RecommendationReasonDialog(
            result = selected!!,
            onDismiss = { selected = null }
        )
    }
}

@Composable
fun RecommendationCard(
    rec: RecommendationResult,
    onSelect: (RecommendationResult) -> Unit,
    onFeedback: (RecommendationResult, Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp)
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onSelect(rec)
            },
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(BrandPrimary.copy(alpha = 0.12f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = rec.destination.name.take(1).uppercase(),
                        fontSize = 20.sp,
                        color = BrandPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = rec.destination.name,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = rec.destination.country,
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = rec.destination.description,
                style = MaterialTheme.typography.body2,
                maxLines = 3,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Pontszám: ${"%.2f".format(rec.score)}",
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray
                )

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = Icons.Filled.ThumbUp,
                        contentDescription = "Like",
                        tint = Color(0xFF1E88E5),
                        modifier = Modifier
                            .size(26.dp)
                            .clickable(
                                indication = LocalIndication.current,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onFeedback(rec, true)
                            }
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Icon(
                        imageVector = Icons.Filled.ThumbDown,
                        contentDescription = "Dislike",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier
                            .size(26.dp)
                            .clickable(
                                indication = LocalIndication.current,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onFeedback(rec, false)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendationReasonDialog(
    result: RecommendationResult,
    onDismiss: () -> Unit
) {
    androidx.compose.material.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = result.destination.name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "Miért ajánljuk neked:",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                result.reasons.forEach { reason ->
                    Text("• $reason", fontSize = 14.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Összesített pontszám: %.2f".format(result.score),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            androidx.compose.material.TextButton(onClick = onDismiss) {
                Text("Bezárás")
            }
        }
    )
}


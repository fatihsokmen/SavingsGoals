package com.github.fatihsokmen.savingsgoals.presentation.home

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.fatihsokmen.savingsgoals.R
import com.github.fatihsokmen.savingsgoals.domain.model.SavingsGoal
import com.github.fatihsokmen.savingsgoals.presentation.util.ShimmerAnimation
import java.util.Calendar

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onCreateNewGoal: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        isGoalsLoading = state.goalState is GoalsState.Loading,
        isRoundupLoading = state.roundupState is RoundupState.Loading,
        isError = state.goalState is GoalsState.Error,
        savingsGoals = (state.goalState as? GoalsState.Success)?.goals ?: emptyList(),
        roundUpAmount = if (state.roundupState is RoundupState.Success) (state.roundupState as RoundupState.Success).roundup.toString() else "",
        onCreateNewGoal = onCreateNewGoal,
        onDateSelected = viewModel::onDateSelected,
        onTransfer = viewModel::onTransfer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    isGoalsLoading: Boolean,
    isRoundupLoading: Boolean,
    isError: Boolean,
    savingsGoals: List<SavingsGoal>,
    roundUpAmount: String,
    onCreateNewGoal: () -> Unit,
    onDateSelected: (Int, Int, Int) -> Unit,
    onTransfer: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.home_toolbar_title)) }
            )
        }
    ) { padding ->
        when {
            isGoalsLoading -> GoalsShimmer(padding)
            isError -> GoalsLoadingError(padding) {}
            else -> if (savingsGoals.isEmpty()) {
                NoGoalsItem(padding, onCreateNewGoal)
            } else {
                Column {
                    GoalsContent(padding, savingsGoals)
                    DatePickerContent(onDateSelected)
                    if (isRoundupLoading) {
                        RoundupShimmer(padding)
                    }
                    if (roundUpAmount.isNotEmpty()) {
                        RoundupItem(roundUpAmount, onTransfer)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerContent(onDateSelected: (Int, Int, Int) -> Unit) {
    var selection by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.home_round_up_calculation_title),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(R.string.home_round_up_calculation_description),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            val datePicker = DatePickerDialog(
                LocalContext.current,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    selection = "$dayOfMonth/${month + 1}/$year"
                    onDateSelected(year, month, dayOfMonth)
                },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            )
            TextField(
                value = selection,
                onValueChange = {},
                readOnly = true
            )
            IconButton(onClick = { datePicker.show() }) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = stringResource(R.string.home_select_date_label)
                )
            }
        }
    }
}

@Composable
private fun GoalsContent(
    padding: PaddingValues,
    goals: List<SavingsGoal>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(goals.size) {
            with(goals[it]) {
                GoalItem(
                    name = name,
                    amount = "${totalSaved.minorUnits} ${totalSaved.currency}"
                )
            }
        }
    }
}

@Composable
private fun GoalItem(name: String, amount: String) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(16.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = stringResource(id = R.string.home_saving_spaces_label)
            )
            Spacer(
                modifier = Modifier.height(4.dp)
            )
            Text(
                text = amount,
                style = MaterialTheme.typography.displaySmall,
            )
        }
    }
}

@Composable
private fun GoalsShimmer(padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(1) {
            ShimmerAnimation {
                GoalShimmerItem(brush = it)
            }
        }
    }
}

@Composable
private fun GoalShimmerItem(brush: Brush) {
    val shape = RoundedCornerShape(12.dp)
    Card(
        Modifier.fillMaxWidth(),
        shape = shape
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                Modifier
                    .fillMaxWidth(fraction = 0.6f)
                    .height(20.dp)
                    .clip(shape)
                    .background(brush)
            )
            Box(
                Modifier
                    .fillMaxWidth(fraction = 0.3f)
                    .height(16.dp)
                    .clip(shape)
                    .background(brush)
            )
            Box(
                Modifier
                    .fillMaxWidth(fraction = 0.6f)
                    .height(32.dp)
                    .clip(shape)
                    .background(brush)
            )
        }
    }
}

@Composable
private fun NoGoalsItem(padding: PaddingValues, onClick: () -> Unit) {
    Row(
        Modifier.padding(horizontal = 16.dp),
    ) {
        ElevatedCard(
            Modifier
                .padding(padding)
                .clickable {
                    onClick()
                },
        ) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.home_saving_spaces_label),
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = stringResource(id = R.string.home_saving_spaces_description),
                )
            }
        }
    }
}

@Composable
private fun GoalsLoadingError(padding: PaddingValues, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
        ) {
            Text(
                text = stringResource(id = R.string.home_loading_error)
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            FilledTonalButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onClick
            ) {
                Text(stringResource(id = R.string.home_action_retry))
            }
        }
    }
}

@Composable
private fun RoundupShimmer(padding: PaddingValues) {
    ShimmerAnimation {
        GoalShimmerItem(brush = it)
    }
}

@Composable
private fun RoundupItem(amount: String, onTransfer: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Amount to transfer in goal",
            style = MaterialTheme.typography.titleMedium
        )
        Card {
            Column(
                Modifier.padding(16.dp)
            ) {
                Text(
                    text = amount,
                    style = MaterialTheme.typography.displaySmall,
                )
            }
        }
        Button(onClick = onTransfer) {
            Text(text = "Transfer")
        }
    }
}


package com.github.fatihsokmen.savingsgoals.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.fatihsokmen.savingsgoals.R
import com.github.fatihsokmen.savingsgoals.presentation.newspace.NewSpaceScreen
import com.github.fatihsokmen.savingsgoals.presentation.home.HomeScreen

@Composable
fun SavingsGoalsApp(
    appState: SavingGoalsAppState = rememberSavingGoalsAppState()
) {
    if (appState.isOnline) {
        NavHost(
            navController = appState.navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) { entry ->
                HomeScreen {
                    appState.navController.navigate(Screen.AddGoal.route)
                }
            }
            composable(Screen.AddGoal.route) { _ ->
                NewSpaceScreen(appState.navController)
            }
        }
    } else {
        OfflineDialog {
            appState.refreshOnline()
        }
    }
}

@Composable
fun OfflineDialog(onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.connection_error_title)) },
        text = { Text(text = stringResource(R.string.connection_error_message)) },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text(stringResource(R.string.connection_retry_label))
            }
        }
    )
}

@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.fatihsokmen.savingsgoals.presentation.newspace

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.fatihsokmen.savingsgoals.R

@Composable
fun NewSpaceScreen(
    navigationController: NavHostController = rememberNavController(),
    viewModel: NewSpaceViewModel = hiltViewModel()
) {
    AddGoalContent(
        isLoading = viewModel.uiState.value == NewSpaceState.Loading,
        onBack = { navigationController.popBackStack() },
        onCrateNewSpace = viewModel::onCreateNewSpace
    )

    LaunchedEffect(viewModel.uiState.value) {
        if (viewModel.uiState.value == NewSpaceState.Success) {
            navigationController.popBackStack()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalContent(
    isLoading: Boolean,
    onBack: () -> Unit,
    onCrateNewSpace: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.new_space_input_label))
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            GoalNameInput(onCrateNewSpace, padding)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalNameInput(onCrateNewSpace: (String) -> Unit, padding: PaddingValues) {
    var goalName by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(id = R.string.new_space_input_description)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = goalName,
            onValueChange = { goalName = it },
            label = { Text("Goal Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { onCrateNewSpace(goalName) }
            ) {
                Text(text = stringResource(R.string.new_space_submit_label))
            }
        }
    }
}
package com.github.fatihsokmen.savingsgoals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.fatihsokmen.savingsgoals.presentation.SavingsGoalsApp
import com.github.fatihsokmen.savingsgoals.presentation.theme.SavingsGoalsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SavingsGoalsTheme {
                SavingsGoalsApp()
            }
        }
    }
}
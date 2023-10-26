package io.ssafy.mogeun.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.ssafy.mogeun.ui.screens.addroutine.AddRoutineScreen
import io.ssafy.mogeun.ui.screens.login.LoginScreen
import io.ssafy.mogeun.ui.screens.record.RecordScreen
import io.ssafy.mogeun.ui.screens.routine.RoutineScreen
import io.ssafy.mogeun.ui.screens.routine.execution.ExecutionScreen
import io.ssafy.mogeun.ui.screens.setting.SettingScreen
import io.ssafy.mogeun.ui.screens.signup.SignupScreen
import io.ssafy.mogeun.ui.screens.summary.SummaryScreen

@Composable
fun MogeunNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Routine.route) {
        composable(Screen.Routine.route) { RoutineScreen() }
        composable(Screen.Execution.route) { ExecutionScreen() }
        composable(Screen.Record.route) { RecordScreen(navController = navController) }
        composable(Screen.Summary.route) { SummaryScreen() }
        composable(Screen.Setting.route) { SettingScreen() }
        composable(Screen.Login.route) { LoginScreen(navController = navController) }
        composable(Screen.Signup.route) { SignupScreen(navController = navController) }
        composable(Screen.AddRoutine.route) { AddRoutineScreen(navController = navController) }
    }
}
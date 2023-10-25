package io.ssafy.mogeun.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.ssafy.mogeun.R
import io.ssafy.mogeun.ui.screens.record.RecordScreen
import io.ssafy.mogeun.ui.screens.routine.RoutineScreen
import io.ssafy.mogeun.ui.screens.setting.SettingScreen
import io.ssafy.mogeun.ui.screens.summary.SummaryScreen
import io.ssafy.mogeun.ui.screens.login.LoginScreen

sealed class Screen(
    val route: String,
    val title: String,
    val vectorId: Int,
) {
    object Routine : Screen("routine", "루틴", R.drawable.icon_routine)
    object Record : Screen("record", "기록", R.drawable.icon_record)
    object Summary : Screen("summary", "요약", R.drawable.icon_summary)
    object Setting : Screen("setting", "설정", R.drawable.icon_setting)
    object Login : Screen("login", "로그인", R.drawable.icon_setting)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController: NavHostController = rememberNavController()

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("test") },
                navigationIcon = {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "")
                    }
                }
            )
        },
        bottomBar = {
            bottomBar(navController)
        }
    ) {innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationGraph(navController = navController)
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Routine.route) {
        composable(Screen.Routine.route) { RoutineScreen() }
        composable(Screen.Record.route) { RecordScreen(navController = navController) }
        composable(Screen.Summary.route) { SummaryScreen() }
        composable(Screen.Setting.route) { SettingScreen() }
        composable(Screen.Login.route) { LoginScreen(navController = navController)}
    }
}

@Composable
fun bottomBar(navController: NavHostController) {

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val rootScreen = arrayOf(Screen.Routine, Screen.Record, Screen.Summary, Screen.Setting)

    NavigationBar {
        rootScreen.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = { Image(imageVector = ImageVector.vectorResource(id = screen.vectorId), contentDescription = screen.route) },
                label = { Text(screen.title) }
            )
        }
    }
}

package io.ssafy.mogeun.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.MutableState
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
import io.ssafy.mogeun.ui.screens.addroutine.AddRoutineScreen
import io.ssafy.mogeun.ui.screens.record.RecordScreen
import io.ssafy.mogeun.ui.screens.routine.RoutineScreen
import io.ssafy.mogeun.ui.screens.routine.execution.ExecutionScreen
import io.ssafy.mogeun.ui.screens.setting.SettingScreen
import io.ssafy.mogeun.ui.screens.summary.SummaryScreen
import io.ssafy.mogeun.ui.screens.login.LoginScreen
import io.ssafy.mogeun.ui.screens.signup.SignupScreen

sealed class Screen(
    val route: String,
    val title: String,
    val vectorId: Int? = null
) {
    object Routine : Screen("routine", "루틴", R.drawable.icon_routine)
    object Execution : Screen("execution", "운동 진행")
    object Record : Screen("record", "기록", R.drawable.icon_record)
    object Summary : Screen("summary", "요약", R.drawable.icon_summary)
    object Setting : Screen("setting", "설정", R.drawable.icon_setting)
    object Login : Screen("login", "로그인", R.drawable.icon_setting)
    object Signup : Screen("signup", "회원가입", R.drawable.icon_setting)
    object AddRoutine : Screen("addroutine", "루틴추가", R.drawable.icon_setting)
}

data class TopBarState(
    val visibility: Boolean,
    val title: String,
)

@Composable
fun Navigation() {
    val navController: NavHostController = rememberNavController()

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val topBarState = rememberSaveable { (mutableStateOf(true))}
    val bottomBarState = rememberSaveable { (mutableStateOf(true))}

    when (currentRoute) {
        "execution" -> {
            bottomBarState.value = false
            topBarState.value = false
        }
    }

    Scaffold (
        topBar = {
            TopBar(navController, topBarState)
        },
        bottomBar = {
            BottomBar(navController, bottomBarState, currentRoute)
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
        composable(Screen.Execution.route) { ExecutionScreen() }
        composable(Screen.Record.route) { RecordScreen(navController = navController) }
        composable(Screen.Summary.route) { SummaryScreen() }
        composable(Screen.Setting.route) { SettingScreen() }
        composable(Screen.Login.route) { LoginScreen(navController = navController)}
        composable(Screen.Signup.route) { SignupScreen(navController = navController)}
        composable(Screen.AddRoutine.route) { AddRoutineScreen(navController = navController)}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController, topBarState: MutableState<Boolean>) {
    AnimatedVisibility(
        visible = topBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
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
    }
}

@Composable
fun BottomBar(navController: NavHostController, bottomBarState: MutableState<Boolean>, currentRoute: String?) {

    val rootScreen = arrayOf(Screen.Routine, Screen.Record, Screen.Summary, Screen.Setting)



    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
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
                    icon = { Image(imageVector = ImageVector.vectorResource(id = screen.vectorId!!), contentDescription = screen.route) },
                    label = { Text(screen.title) }
                )
            }
        }
    }
}

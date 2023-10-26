package io.ssafy.mogeun.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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



@Composable
fun Navigation() {
    val navController: NavHostController = rememberNavController()

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val currentScreen = screens.find { it.route == currentRoute } ?: Screen.Login

    val topBarState = rememberSaveable { (mutableStateOf(true))}
    val bottomBarState = rememberSaveable { (mutableStateOf(true))}

    Scaffold (
        topBar = {
            TopBar(navController, currentScreen)
        },
        bottomBar = {
            BottomBar(navController, currentScreen)
        }
    ) {innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MogeunNavHost(navController = navController)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController, currentScreen: Screen) {
    AnimatedVisibility(
        visible = currentScreen.topBarState.visibility,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        TopAppBar(
            title = { Text(currentScreen.title) },
            navigationIcon = {
                if (currentScreen.topBarState.backBtnVisibility) {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "")
                    }
                }
            }
        )
    }
}

@Composable
fun BottomBar(navController: NavHostController, currentScreen: Screen) {

    AnimatedVisibility(
        visible = currentScreen.bottomBarState.visibility,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        NavigationBar {
            rootScreen.forEach { screen ->
                NavigationBarItem(
                    selected = currentScreen.bottomBarState.originRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    icon = { Image(imageVector = ImageVector.vectorResource(id = screen.bottomBarState.vectorId!!), contentDescription = screen.route) },
                    label = { Text(screen.title) }
                )
            }
        }
    }
}

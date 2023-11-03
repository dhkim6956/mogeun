package io.ssafy.mogeun

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.ssafy.mogeun.ui.MogeunNavHost
import io.ssafy.mogeun.ui.Screen
import io.ssafy.mogeun.ui.rootScreen


@Composable
fun Navigation() {
    val snackbarHostState = remember{ SnackbarHostState() }

    val navController: NavHostController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()

    val screens = arrayOf(Screen.ExplainExercise)
    val currentRoute = navBackStackEntry.value?.destination?.route

    val currentScreen = try {
        Screen.valueOf(
            currentRoute ?: Screen.Login.name
        )
    } catch (e: Exception) {
        screens.find { it.route == currentRoute } ?: Screen.Login
    }

    Scaffold (
        topBar = {
            TopBar(navController, currentScreen)
        },
        bottomBar = {
            BottomBar(navController, currentScreen)
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MogeunNavHost(navController, snackbarHostState)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController, currentScreen: Screen) {
    AnimatedVisibility(
        visible = currentScreen.topBarState.visibility,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        TopAppBar(
            title = { Text(currentScreen.title) },
            navigationIcon = {
                if (currentScreen.topBarState.backBtnVisibility) {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "")
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
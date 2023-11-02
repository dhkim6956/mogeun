package io.ssafy.mogeun.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator


@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun Navigation() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController: NavHostController = rememberNavController(bottomSheetNavigator)

    val navBackStackEntry = navController.currentBackStackEntryAsState()

    val currentScreen = Screen.valueOf(
        navBackStackEntry.value?.destination?.route ?: Screen.Login.name
    )

    ModalBottomSheetLayout(bottomSheetNavigator) {
        Scaffold (
            topBar = {
                TopBar(navController, currentScreen)
            },
            bottomBar = {
                BottomBar(navController, currentScreen)
            }
        ) {innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                MogeunNavHost(navController)
            }
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
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
package io.ssafy.mogeun.ui

import io.ssafy.mogeun.R

data class TopBarState(
    val visibility: Boolean,
    val backBtnVisibility: Boolean
)

data class BottomBarState(
    val visibility: Boolean,
    val vectorId: Int? = null,
    val originRoute: String? = null,
)

sealed class Screen(
    val route: String,
    val title: String,
    val topBarState: TopBarState,
    val bottomBarState: BottomBarState
) {
    object Routine : Screen(
        route = "routine",
        title = "루틴",
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, R.drawable.icon_routine, "routine")
    )
    object Execution : Screen(
        route = "execution",
        title = "운동 진행",
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(false)
    )
    object Record : Screen(
        route = "record",
        title = "기록",
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, R.drawable.icon_record, "record")
    )
    object RecordDetail : Screen(
        route = "recorddetail",
        title = "루틴 상세 정보",
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, R.drawable.icon_record, "record")
    )

    object ExerciseDetail : Screen(
        route = "exercisedetail",
        title = "운동 상세 정보",
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, R.drawable.icon_record, "record")
    )
    object Summary : Screen(
        route = "summary",
        title = "요약",
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, R.drawable.icon_summary, "summary")
    )
    object Setting : Screen(
        route = "setting",
        title = "설정",
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, R.drawable.icon_setting, "setting")
    )
    object Login : Screen(
        route = "login",
        title = "로그인",
        topBarState = TopBarState(visibility = false, backBtnVisibility = false),
        bottomBarState = BottomBarState(false)
    )
    object Signup : Screen(
        route = "signup",
        title = "회원가입",
        topBarState = TopBarState(visibility = false, backBtnVisibility = false),
        bottomBarState = BottomBarState(false)
    )
    object AddRoutine : Screen(
        route = "addroutine",
        title = "루틴 추가",
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, R.drawable.icon_setting)
    )
    object AddExercise : Screen(
        route = "addexercise",
        title = "운동 추가",
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, R.drawable.icon_setting)
    )
    object ExplainExercise : Screen(
        route = "explainexercise",
        title = "운동 설명",
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, R.drawable.icon_setting)
    )
}

val rootScreen = arrayOf(Screen.Routine, Screen.Record, Screen.Summary, Screen.Setting)
val screens = arrayOf(Screen.Routine, Screen.Execution, Screen.Record, Screen.Summary, Screen.Setting, Screen.Login, Screen.Signup, Screen.AddRoutine, Screen.AddExercise, Screen.ExplainExercise)
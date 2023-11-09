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

enum class Screen(
    val route: String,
    val title: String,
    val topBarState: TopBarState,
    val bottomBarState: BottomBarState
) {
    Splash(
        route = "Splash",
        title = "로딩화면",
        topBarState = TopBarState(visibility = false, backBtnVisibility = false),
        bottomBarState = BottomBarState(false)
    ),
    Routine(
        route = "Routine",
        title = "루틴",
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, R.drawable.icon_routine, "routine")
    ),
    Execution(
    route = "Execution",
    title = "운동 진행",
    topBarState = TopBarState(visibility = true, backBtnVisibility = false),
    bottomBarState = BottomBarState(false)
    ),
    Record(
        route = "Record",
        title = "기록",
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, R.drawable.icon_record, "record")
    ),
    RecordDetail(
        route = "RecordDetail/{reportKey}",
        title = "루틴 상세 정보",
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, R.drawable.icon_record, "record")
    ),
    ExerciseDetail(
        route = "ExerciseDetail",
        title = "운동 상세 정보",
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, R.drawable.icon_record, "record")
    ),
    Summary(
        route = "Summary",
        title = "요약",
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, R.drawable.icon_summary, "summary")
    ),
    Setting(
        route = "Setting",
        title = "설정",
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, R.drawable.icon_setting, "setting")
    ),
    User(
        route = "User",
        title = "유저 정보 변경",
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, originRoute = "setting")
    ),
    Login(
        route = "Login",
        title = "로그인",
        topBarState = TopBarState(visibility = false, backBtnVisibility = false),
        bottomBarState = BottomBarState(false)
    ),
    Signup(
        route = "Signup",
        title = "회원가입",
        topBarState = TopBarState(visibility = false, backBtnVisibility = false),
        bottomBarState = BottomBarState(false)
    ),
    AddRoutine(
        route = "AddRoutine/{routineKey}",
        title = "루틴 관리",
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, R.drawable.icon_setting)
    ),
    AddExercise(
        route = "AddExercise",
        title = "운동 추가",
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, R.drawable.icon_setting)
    ),
    ExplainExercise(
        route = "ExplainExercise/{image}",
        title = "운동 설명",
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, R.drawable.icon_setting)
    ),
    SqlSample(
        route = "SqlSample",
        title = "sql 테스트 페이지",
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true)
    ),
    Connection(
        route = "Connection",
        title = "기기 연결",
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true)
    )
}

val rootScreen = arrayOf(Screen.Routine, Screen.Record, Screen.Summary, Screen.Setting)
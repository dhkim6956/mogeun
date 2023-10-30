package io.ssafy.mogeun.ui.screens.record

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class Exercise(
    val time: LocalDate,
    val recordTime: String,
    val name: String,
)

fun generateExercises(): List<Exercise> = buildList {
    val currentMonth = YearMonth.now()

    currentMonth.atDay(17).also { date ->
        add(
            Exercise(
                date,
                "09:10 ~ 10:21",
                "내가 만든 루틴",
            ),
        )
        add(
            Exercise(
                date,
                "09:10 ~ 10:21",
                "내가 만든 루틴",
            ),
        )
    }

    currentMonth.atDay(22).also { date ->
        add(
            Exercise(
                date,
                "09:10 ~ 10:21",
                "내가 만든 루틴",
            ),
        )
        add(
            Exercise(
                date,
                "09:10 ~ 10:21",
                "내가 만든 루틴",
            ),
        )
    }

    currentMonth.atDay(3).also { date ->
        add(
            Exercise(
                date,
                "09:10 ~ 10:21",
                "내가 만든 루틴",
            ),
        )
    }

    currentMonth.atDay(12).also { date ->
        add(
            Exercise(
                date,
                "09:10 ~ 10:21",
                "내가 만든 루틴",
            ),
        )
    }

    currentMonth.plusMonths(1).atDay(13).also { date ->
        add(
            Exercise(
                date,
                "09:10 ~ 10:21",
                "내가 만든 루틴",
            ),
        )
        add(
            Exercise(
                date,
                "09:10 ~ 10:21",
                "내가 만든 루틴",
            ),
        )
    }

    currentMonth.minusMonths(1).atDay(9).also { date ->
        add(
            Exercise(
                date,
                "09:10 ~ 10:21",
                "내가 만든 루틴",
            ),
        )
    }
}

val exerciseDateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'HH:mm")

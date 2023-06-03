package pt.ipc_app.service.models.dailyList

import pt.ipc_app.domain.exercise.Exercise

data class DailyListInput(val exercises: List<Exercise>)
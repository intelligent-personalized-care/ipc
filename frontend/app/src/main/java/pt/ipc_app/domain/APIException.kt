package pt.ipc_app.domain

import pt.ipc_app.service.utils.ProblemJson

class APIException(val error: ProblemJson) : Exception()
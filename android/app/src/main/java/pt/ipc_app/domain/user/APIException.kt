package pt.ipc_app.domain.user

import pt.ipc_app.service.models.ProblemJson

class APIException(val error: ProblemJson) : Exception()
package pt.ipc_app.domain

import pt.ipc_app.service.utils.ResponseError

class APIException(val error: ResponseError) : Exception()
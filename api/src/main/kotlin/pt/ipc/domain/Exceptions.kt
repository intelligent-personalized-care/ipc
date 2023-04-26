package pt.ipc.domain

abstract class BadRequest(msg : String) : Exception(msg)

abstract class NotFound(msg : String) : Exception(msg)

abstract class Conflit(msg : String) : Exception(msg)

abstract class UnauthorizedRequest(msg : String) : Exception(msg)

abstract class Forbidden(msg : String) : Exception(msg)

class Unauthenticated : UnauthorizedRequest("Unauthenticated")

class EmailAlreadyInUse : Conflit("Email already in Use")

class BadEmail : BadRequest("Bad Email")

class WeakPassword : BadRequest("Password too weak")

class Unauthorized : Forbidden("You cannot access this resource")

class UserNotExists : NotFound("This User does Not Exists")

class RequestNotExists : NotFound("This Request does not exists")
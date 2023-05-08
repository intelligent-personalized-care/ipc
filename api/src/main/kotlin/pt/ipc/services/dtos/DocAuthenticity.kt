package pt.ipc.services.dtos

import java.time.LocalDate
import java.util.*

data class DocAuthenticity(val monitorID: UUID, val dateSubmit: LocalDate)

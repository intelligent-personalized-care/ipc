package pt.ipc.http.models

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.util.*

data class LoginOutput(val id : UUID, @ColumnName("token_hash") val token : String )
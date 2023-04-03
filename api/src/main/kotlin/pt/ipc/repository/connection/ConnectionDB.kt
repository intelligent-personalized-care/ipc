package pt.ipc.repository.connection

import java.io.Closeable
import java.sql.Connection

/**
 * Database connection representation.
 */
abstract class ConnectionDB

/**
 * Postgres database connection implementation.
 */
class PostgresConnectionDB(val connection: Connection) : ConnectionDB(), Closeable {

    override fun close() {
        connection.close()
    }
}

/**
 * Dummy memory connection implementation.
 */
class DataMemConnectionDB : ConnectionDB()

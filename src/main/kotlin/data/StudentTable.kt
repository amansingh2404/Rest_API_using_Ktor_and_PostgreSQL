package data

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object StudentTable:Table() {
    val userId: Column<Int> = integer("userId").autoIncrement()
    val name: Column<String> = varchar("name", 512)
    val age: Column<Int> = integer("age")

    override val primaryKey: PrimaryKey = PrimaryKey(userId)
}
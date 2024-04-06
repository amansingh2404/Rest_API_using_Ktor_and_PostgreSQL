package data.repository

import data.Student
import data.StudentTable
import data.dao.StudentDao
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement
import javax.xml.crypto.Data

class StudentRepository: StudentDao {
    override suspend fun insert(name: String, age: Int): Student? {

        var statement: InsertStatement<Number>? = null
        DatabaseFactory.dbQuery {
            statement = StudentTable.insert {
                student->
                student[StudentTable.name] = name
                student[StudentTable.age] = age
            }
        }
        return rowToStudent(statement?.resultedValues?.get(0))

    }

    override suspend fun getAllStudent(): List<Student> =
        DatabaseFactory.dbQuery {
            StudentTable.selectAll().mapNotNull {
                rowToStudent(it)
            }
        }


    override suspend fun getStudentById(userid: Int): Student? =
        DatabaseFactory.dbQuery {
            StudentTable.select{StudentTable.userId.eq(userid)}
                .map {
                    rowToStudent(it)
                }.singleOrNull()
        }

    override suspend fun deleteById(userId: Int): Int =
        DatabaseFactory.dbQuery {
            StudentTable.deleteWhere { StudentTable.userId.eq(userId) }
        }

    override suspend fun update(userId: Int, name: String, age: Int): Int =
        DatabaseFactory.dbQuery {
            StudentTable.update({
                StudentTable.userId.eq(userId)
            }) { student ->
                student[StudentTable.name] = name
                student[StudentTable.age] = age
            }

        }



    private fun rowToStudent(row: ResultRow?): Student? {
        if(row == null){
            return null
        }
        return Student(
            userId = row[StudentTable.userId],
            name = row[StudentTable.name],
            age = row[StudentTable.age]
        )
    }
}
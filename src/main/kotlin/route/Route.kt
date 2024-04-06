package route


import com.example.API_VERSION
import data.Student
import data.repository.StudentRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post


const val CREATE_STUDENT = "$API_VERSION/student"

@OptIn(KtorExperimentalLocationsAPI::class)
@Location(CREATE_STUDENT)
class CreateStudent

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.student(
    db: StudentRepository
) {
    post("/student") {
        val parameter = call.receive<Parameters>()
        val name = parameter["name"] ?: return@post call.respondText(
            "Missing Field",
            status = HttpStatusCode.Unauthorized
        )
        val age = parameter["age"] ?: return@post call.respondText(
            "MISSING FIELD",
            status = HttpStatusCode.Unauthorized
        )
        try {
            val student: Student? = db.insert(name, age.toInt())

            student?.userId?.let {
                call.respond(status = HttpStatusCode.OK,student)
            }

        }catch (e: Throwable){
            call.respondText("${e.message}")
        }
    }
}
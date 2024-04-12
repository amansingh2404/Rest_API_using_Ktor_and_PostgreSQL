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
    post("/student/insert") {
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
    get("/student"){
        try {
            val allStudent = db.getAllStudent()
            if(allStudent.isNotEmpty()){
                call.respond(allStudent)
            }else{
                call.respond("No data found")
            }
        }catch (e: Throwable){
            application.log.error("Failed to get user", e)
            call.respond(HttpStatusCode.BadRequest, "Problem in finding user")
        }
    }
    get("student/{id}"){
        val parameter = call.parameters["id"]
        try{
            val user = parameter?.toInt()?.let { it1 -> db.getStudentById(it1) } ?: return@get call.respondText(
                "Invalid Id",
                status = HttpStatusCode.BadRequest
            )
            user.userId.let{
                call.respond(status = HttpStatusCode.OK, user)
            }
        }catch (e: Throwable){
            application.log.error("Failed to register User", e)
            call.respond(HttpStatusCode.BadRequest, "Problems creating user")
        }
    }
    delete("student/{id}"){
        val parameter = call.parameters["id"]
        try{
            val user = parameter?.toInt()?.let { it1-> db.deleteById(it1) } ?: return@delete call.respondText(
                "No id found...",
                status = HttpStatusCode.BadRequest
            )
            if(user == 1){
                call.respondText("Deleted Successfully", status = HttpStatusCode.OK)

            }else{
                call.respondText("Id not found ", status = HttpStatusCode.BadRequest)

            }
        }catch (e: Throwable){
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problem creating user")
        }
    }
}
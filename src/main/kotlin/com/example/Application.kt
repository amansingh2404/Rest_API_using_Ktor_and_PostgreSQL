package com.example

import com.example.plugins.*
import data.repository.DatabaseFactory
import data.repository.StudentRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import route.student

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    intercept(ApplicationCallPipeline.Fallback) {
        if (call.isHandled) return@intercept
        val status = call.response.status() ?: HttpStatusCode.NotFound
        call.respond(status)
    }
    configureSerialization()
    configureRouting()

    DatabaseFactory.init()

    val db = StudentRepository()

    routing {
        student(db)
    }
}


const val API_VERSION = "v1/"

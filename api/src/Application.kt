package com.nathanblaubach

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import com.nathanblaubach.dao.*
import com.nathanblaubach.repositories.RecipeRepository
import io.ktor.jackson.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Paths

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(Authentication) {
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        get("/") {
            call.respondText("Welcome to the cookbook!", contentType = ContentType.Text.Plain)
        }

        get("/init") {
            RecipeRepository(SQLiteDb("/path/to/cookbook.db").retrieve()).init()
            call.respond(mapOf("success" to true))
        }

        route("/json/recipes") {
            get {
                val recipeRepository = RecipeRepository(SQLiteDb("/path/to/cookbook.db").retrieve())
                val requiredParameters = listOf("id")
                when {
                    this.context.parameters.entries().map{ it.key }.containsAll(requiredParameters) ->
                        call.respond(mapOf("success" to true, "recipe" to recipeRepository.read(this.context.parameters["id"]?.toIntOrNull() ?: -1)))
                    else ->
                        call.respond(mapOf("success" to true, "recipes" to recipeRepository.read()))
                }
            }
            put {
                TODO("Save a record to the database")
            }
        }
    }
}

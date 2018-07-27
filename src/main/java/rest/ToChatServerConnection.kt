package rest

import org.apache.http.HttpHost
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils

class ToChatServerConnection(val hostName: String = "localhost", val port: Int = 8080, val scheme: String = "http") {

    private val authenticationUri = "/api/auth/login"

    // TODO: Return the client
    fun authenticate(entity: StringEntity) {
        val target = HttpHost(hostName, port, scheme)
        val request = HttpPost(authenticationUri)
        request.entity = entity

        var client = HttpClientBuilder.create().build()

        client.use {
            val response = client.execute(target, request)

            // TODO: Remove prints
            println("Received the following response:")
            println(response.statusLine)
            response.allHeaders.forEach { println("$it") }
            println(EntityUtils.toString(response.entity))
        }
    }
}
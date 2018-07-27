import mapper.XmlJsonMapper
import rest.ToChatServerConnection
import socket.io.XmppPrintWriter
import socket.XmppServerSocket
import java.net.Socket

class XmppMapper {

    // TODO: Implement Logger and replace prints
//    companion object : KLogging()

    fun main(args : Array<String>) {
        // Establish a connection with the client, handshake and get authentication xml
        val credentialXML = authenticateNewClient()

        // Transform XMPP XML message into a StringEntity, following our Servers JSON request pattern
        val mapper = XmlJsonMapper()
        val authenticationJson = mapper.convertAuthenticationMessage(credentialXML)

        // Send the JSON to our Server to authenticate
        val serverConnection = ToChatServerConnection()
        serverConnection.authenticate(authenticationJson)
    }

    private fun authenticateNewClient(): String {
        val client = openConnectionForClient()
        println("Client connected to Socket, waiting for authentication message")

        val writer = XmppPrintWriter(client.getOutputStream())

        // TODO: Implement reader (@Lars this is one of the two issues I mentioned). Need to read the incoming messages in order to send the according response
//        val reader = XmppBufferedReader(client.getInputStream())
//        val response = reader.readResponse() // This will block

        writer.sendHandshakeResponse()

        // TODO: Send more messages (@Lars this was the more important issue. Currently, one one message is sent)

        // TODO: The current Idea is to return the xml response from the client, containing the login information
        return ""
    }

    private fun openConnectionForClient(): Socket {
        val socket = XmppServerSocket()
        println("Connection open - waiting for client to connect")
        return socket.createSocketForNextClient()
    }
}
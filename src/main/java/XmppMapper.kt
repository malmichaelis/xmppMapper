import mu.KLogging
import socket.io.XmppBufferedReader
import socket.io.XmppPrintWriter
import socket.XmppServerSocket
import java.net.Socket

class XmppMapper {

//    companion object : KLogging()

    fun main(args : Array<String>) {
        val client = openConnectionForClient()
        println("Client connected to Socket, waiting for authentication message")

        val writer = XmppPrintWriter(client.getOutputStream())
        val reader = XmppBufferedReader(client.getInputStream())

        println("Received from Client: " + reader.readResponse())
        println("Sending to Client: <stream:stream from='localhost' id='someid' xmlns=\"jabber:client\" xmlns:stream=\"http://etherx.jabber.org/streams\" version=\"1.0\"></stream:stream>")
        writer.sendConnectionAcceptance()

        println("Received from Client: " + reader.readResponse())
        println("Sending to Client: <success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>")
        writer.sendToClient("<success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>")

        println(reader.readResponse().toString())
        writer.sendToClient("<iq type=\"result\"><bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\"><jid>SENDER_ID@gcm.googleapis.com/RESOURCE</jid></bind></iq>")
    }

    private fun openConnectionForClient(): Socket {
        val socket = XmppServerSocket()
        println("Connection open - waiting for client to connect")
        return socket.createSocketForNextClient()
    }
}
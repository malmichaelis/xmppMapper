import io.XmppBufferedReader
import io.XmppPrintWriter
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.XMPPConnection
import org.junit.Test
import socket.XmppServerSocket

class e2eTest {

    @Test
    fun `test a converstation server side`() {
        val socket = XmppServerSocket()
        println("Connection open - waiting for client to connect")
        var client = socket.createSocketForNextClient()
        println("Client connected to Socket, waiting for authentication message")

        var writer = XmppPrintWriter(client.getOutputStream())
        var reader = XmppBufferedReader(client.getInputStream())

        println("Received from Client: " + reader.readResponse())
        println("Sending to Client: <stream:stream from='localhost' id='someid' xmlns=\"jabber:client\" xmlns:stream=\"http://etherx.jabber.org/streams\" version=\"1.0\"></stream:stream>")
        writer.sendConnectionAcceptance()

        println("Received from Client: " + reader.readResponse())
        println("Sending to Client: <success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>")
        writer.sendToClient("<success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>")

        println(reader.readResponse())
        writer.sendToClient("<iq type=\"result\"><bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\"><jid>SENDER_ID@gcm.googleapis.com/RESOURCE</jid></bind></iq>")
    }

    @Test
    fun `test a conversation client side`() {
        val config = ConnectionConfiguration("localhost", 9092)

        config.setSASLAuthenticationEnabled(false)
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)

        val connection = XMPPConnection(config)
        connection.connect()
    }
}
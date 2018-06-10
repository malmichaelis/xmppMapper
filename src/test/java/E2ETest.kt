import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.XMPPConnection
import socket.io.XmppBufferedReader
import socket.io.XmppPrintWriter
import socket.XmppServerSocket
import org.junit.Test

class E2ETest {

    // TODO: Since it has to be in another thread, the first test currently starts the mapper-server
    // and waits for a connection. It proceeds to send messages to the client test below
    @Test
    fun `test a conversation server side`() {
        val socket = XmppServerSocket()
        println("Connection open - waiting for client to connect")
        var client = socket.createSocketForNextClient()
        println("Client connected to Socket, waiting for authentication message")

        var writer = XmppPrintWriter(client.getOutputStream())
        var reader = XmppBufferedReader(client.getInputStream())

        print("Received from Client: ")
        println(reader.readResponse())
        println("Sending to Client: <stream:stream from='localhost' id='someid' xmlns=\"jabber:client\" xmlns:stream=\"http://etherx.jabber.org/streams\" version=\"1.0\"></stream:stream>")
        writer.sendConnectionAcceptance()

        println("Received from Client: " + reader.readResponse())
        println("Sending to Client: <success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>")
        writer.sendToClient("<success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>")

        println("Received from Client: " + reader.readResponse())
        println("Sending to Client: <iq type=\"result\"><bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\"><jid>SENDER_ID@gcm.googleapis.com/RESOURCE</jid></bind></iq>")
        writer.sendToClient("<iq type=\"result\"><bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\"><jid>SENDER_ID@gcm.googleapis.com/RESOURCE</jid></bind></iq>")
    }

    // This test acts as an XMPP-client, calling the mapper-server requesting authentication
    @Test
    fun `test setup connection`() {
        val config = ConnectionConfiguration("localhost", 9092)

        config.isSASLAuthenticationEnabled = false
        config.securityMode = ConnectionConfiguration.SecurityMode.disabled

        val connection = XMPPConnection(config)
        connection.connect()
    }
}
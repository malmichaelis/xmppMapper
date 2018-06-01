package socket.io

import java.io.OutputStream
import java.io.PrintWriter

class XmppPrintWriter(outputStream: OutputStream) {

    var out = PrintWriter(outputStream, true)

    fun sendToClient(message: String) {
        out.println(message)
    }

    fun sendConnectionAcceptance() {
        // possible stream features: <stream:features><mechanisms xmlns="urn:ietf:params:xml:ns:xmpp-sasl"><mechanism>PLAIN</mechanism></mechanisms></stream:features>
        val response = "<stream:stream from='localhost' id='someid' xmlns=\"jabber:client\" xmlns:stream=\"http://etherx.jabber.org/streams\" version=\"1.0\"></stream:stream>"
        sendToClient(response)
    }
}
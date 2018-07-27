package socket.io

import java.io.OutputStream
import java.io.PrintWriter

class XmppPrintWriter(outputStream: OutputStream) {

    // TODO: Replace prints with a proper logger

    private var out = PrintWriter(outputStream, true)

    fun sendToClient(message: String) {
        out.write(message)
        out.flush()
    }

    fun sendHandshakeResponse() {
        val message = "<stream:stream from='localhost' id='someid' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' version='1.0'></stream:stream>"
        print(message)
        sendToClient(message)
    }

    fun sendAcknowledgement() {
        val message = "<a xmlns='http://jabber.org/protocol/ack'/>"
        print(message)
        sendToClient(message)
    }

    fun sendAuthenticationMechanisms(mechanisms: String = "<mechanism>PLAIN</mechanism>") {
        val message = "<stream:features><mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>$mechanisms</mechanisms></stream:features>"
        print(message)
        sendToClient(message)
    }

    fun sendAuthenticationMechanisms(mechanisms: List<String>) {
        if(mechanisms.isEmpty()) {
            throw IllegalArgumentException("At least one mechanisms needs to be available.")
        }

        val printableMechanisms = mechanisms.map{ mechanism: String -> "<mechanism>$mechanism</mechanism>" }.formatToPlainString()

        val message = "<stream:features><mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>$printableMechanisms</mechanisms></stream:features>"
        print(message)
        sendToClient(message)
    }

    fun List<String>.formatToPlainString(): String {
        var printableFields = ""
        for (field in this) {
            printableFields += field
        }

        return printableFields
    }

    // Can be the response when a client requests authentication fields
    // <iq type='get' to='shakespeare.lit' id='auth1'>
    //  <query xmlns='jabber:iq:auth'/>
    //</iq>
    fun sendAuthenticationFields(authenticationFields: String = "<username/><password/><digest/><resource/>") {
        val message = "<iq type='result' id='auth1'><query xmlns='jabber:iq:auth'>$authenticationFields</query></iq>"
        print(message)
        sendToClient(message)
    }

    fun sendAuthenticationFields(authenticationFields: List<String>) {
        if(authenticationFields.isEmpty()) {
            throw IllegalArgumentException("At least one mechanisms needs to be available.")
        }

        val message = "<iq type='result' id='auth1'><query xmlns='jabber:iq:auth'>${authenticationFields.formatToPlainString()}</query></iq>"
        print(message)
        sendToClient(message)
    }

    fun sendAuthenticationSuccess() {
        // TODO: Might also be <iq type='result' id='auth2'/>
        val message = "<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/>"
        print(message)
        sendToClient(message)
    }

    fun sendXmppBind() {
        val message = "<iq type='result'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><jid>SENDER_ID@gcm.googleapis.com/RESOURCE</jid></bind></iq>"
        print(message)
        sendToClient(message)
    }

    // TODO: Failed authentication
}
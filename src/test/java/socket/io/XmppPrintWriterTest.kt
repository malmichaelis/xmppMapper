package socket.io

import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.io.File
import java.io.FileOutputStream
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class XmppPrintWriterTest {

    private var underTest: XmppPrintWriter? = null
    private var file: File = File.createTempFile("test", "xmppWriter")

    @Before
    fun beforeMethod() {
        val os = FileOutputStream(file)
        underTest = XmppPrintWriter(os)
    }

    @Test
    fun `test construct`() {
        assertNotNull(underTest)
    }

    @Test
    fun `test sending a response to a handshake from an XMPP client`() {
        underTest?.sendHandshakeResponse()
        assertEquals("<stream:stream from='localhost' id='someid' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' version='1.0'></stream:stream>", file.readText())
    }

    @Test
    fun `test sending an acknowledgement`() {
        underTest?.sendAcknowledgement()
        assertEquals("<a xmlns='http://jabber.org/protocol/ack'/>", file.readText())
    }

    @Test
    fun `test sending authentication mechnisms for non-sasl authentication`() {
        underTest?.sendAuthenticationMechanisms()
        assertEquals("<stream:features><mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'><mechanism>PLAIN</mechanism></mechanisms></stream:features>", file.readText())
    }

    // TODO: Is this the expected behaviour?
    @Test
    fun `test sending authentication mechnisms for non-sasl authentication with empty String`() {
        underTest?.sendAuthenticationMechanisms("")
        assertEquals("<stream:features><mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'></mechanisms></stream:features>", file.readText())
    }

    @Test
    fun `test sending authentication mechnisms for non-sasl authentication as a List`() {
        val mechanisms = listOf("PLAIN", "DIGEST-MD5", "EXTERNAL", "ANONYMOUS")
        underTest?.sendAuthenticationMechanisms(mechanisms)
        assertEquals("<stream:features><mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'><mechanism>PLAIN</mechanism><mechanism>DIGEST-MD5</mechanism><mechanism>EXTERNAL</mechanism><mechanism>ANONYMOUS</mechanism></mechanisms></stream:features>", file.readText())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test sending authentication mechnisms for non-sasl authentication with empty List`() {
        val mechanisms: List<String> = listOf()
        underTest?.sendAuthenticationMechanisms(mechanisms)
        assertEquals("<stream:features><mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'></mechanisms></stream:features>", file.readText())
    }

    @Test
    fun `test sending authentication mechnisms for non-sasl authentication with List of empty String`() {
        val mechanisms = listOf("")
        underTest?.sendAuthenticationMechanisms(mechanisms)
    }

    @Test
    fun `test sending requested authentication fields`() {
        underTest?.sendAuthenticationFields()
        assertEquals("<iq type='result' id='auth1'><query xmlns='jabber:iq:auth'><username/><password/><digest/><resource/></query></iq>", file.readText())
    }

    @Test
    fun `test sending requested authentication fields with empty String`() {
        underTest?.sendAuthenticationFields("")
        assertEquals("<iq type='result' id='auth1'><query xmlns='jabber:iq:auth'></query></iq>", file.readText())
    }

    @Test
    fun `test sending requested authentication fields as List`() {
        val fields = listOf("<username/>", "<password/>", "<digest/>", "<resource/>")
        underTest?.sendAuthenticationFields(fields)
        assertEquals("<iq type='result' id='auth1'><query xmlns='jabber:iq:auth'><username/><password/><digest/><resource/></query></iq>", file.readText())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test sending requested authentication fields with empty List`() {
        val fields: List<String> = listOf()
        underTest?.sendAuthenticationFields(fields)
    }

    @Test
    fun `test sending requested authentication fields with List of empty String`() {
        val fields: List<String> = listOf("")
        underTest?.sendAuthenticationFields(fields)
        assertEquals("<iq type='result' id='auth1'><query xmlns='jabber:iq:auth'></query></iq>", file.readText())
    }

    @Test
    fun `test sending a success message`() {
        underTest?.sendAuthenticationSuccess()
        assertEquals("<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/>", file.readText())
    }

    @Test
    fun `test sending an XMPP bind message`() {
        underTest?.sendXmppBind()
        assertEquals("<iq type='result'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><jid>SENDER_ID@gcm.googleapis.com/RESOURCE</jid></bind></iq>", file.readText())
    }

    @Test
    fun `test sending multiple messages`() {
        underTest?.sendXmppBind()
        assertEquals("<iq type='result'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><jid>SENDER_ID@gcm.googleapis.com/RESOURCE</jid></bind></iq>", file.readText())

        underTest?.sendAuthenticationSuccess()
        assertEquals("<iq type='result'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><jid>SENDER_ID@gcm.googleapis.com/RESOURCE</jid></bind></iq><success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/>", file.readText())
    }

    @Test
    fun `test sending multiple messages with wait time`() {
        underTest?.sendXmppBind()
        assertEquals("<iq type='result'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><jid>SENDER_ID@gcm.googleapis.com/RESOURCE</jid></bind></iq>", file.readText())
        Thread.sleep(2000)

        underTest?.sendAuthenticationSuccess()
        assertEquals("<iq type='result'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><jid>SENDER_ID@gcm.googleapis.com/RESOURCE</jid></bind></iq><success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/>", file.readText())
        Thread.sleep(2000)

        underTest?.sendAuthenticationSuccess()
        assertEquals("<iq type='result'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><jid>SENDER_ID@gcm.googleapis.com/RESOURCE</jid></bind></iq><success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/><success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/>", file.readText())
    }
}
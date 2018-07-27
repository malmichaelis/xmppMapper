package mapper

import org.junit.Ignore
import org.junit.jupiter.api.Test

class XmlJsonMapperTest {

    val _underTest = XmlJsonMapper()

    @Test
    @Ignore // TODO: Since we don't have the full authentication message here, we don't know exactly what to map
    fun `test mapping of an authentication request xml into a JSON, our server can understand`() {
        val authenticationMessage = "<?xml version='1.0' ?><stream:stream to='localhost' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' from='su@localhost' version='1.0' xml:lang='en'></stream:stream>"
        val result = _underTest.convertAuthenticationMessage(authenticationMessage)
        print(result)
    }
}
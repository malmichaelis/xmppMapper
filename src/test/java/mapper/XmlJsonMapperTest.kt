package mapper

import org.junit.jupiter.api.Test

class XmlJsonMapperTest {

    val _underTest = XmlJsonMapper()

    @Test
    fun `testMapXmlAuthenticationtoJSON`() {
        val authenticationMessage = "<stream:stream to=\"localhost\" xmlns=\"jabber:client\" xmlns:stream=\"http://etherx.jabber.org/streams\" version=\"1.0\"></stream:stream>"
    }
}
package rest

import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.junit.Ignore
import org.junit.jupiter.api.Test

class ToChatServerConnectionTest {

    private val underTest = ToChatServerConnection()

    @Test
    @Ignore // TODO: Currently only works, if our Server runs locally
    fun `test authentication`() {
         val entity = StringEntity("{\"identifier\":\"admin@iconect.io\",\"password\":\"admin\"}", ContentType.create("application/json"))
        underTest.authenticate(entity)
    }
}
package rest

import org.apache.http.util.EntityUtils
import org.junit.jupiter.api.Test

class ToChatServerConnectionTest {

    val _underTest = ToChatServerConnection()

    @Test
    fun `testAuthenticate`() {
        _underTest.authenticate()
    }
}
package rest

import org.junit.jupiter.api.Test

class ToChatServerConnectionTest {

    val _underTest = ToChatServerConnection()

    @Test
    fun `testAuthenticate`() {
        _underTest.authenticate()
    }
}
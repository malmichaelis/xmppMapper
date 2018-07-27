package socket

import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.net.Socket
import kotlin.test.assertNotNull

class XmppServerSocketTest {

    private var underTest: XmppServerSocket? = null

    @Before
    fun beforeMethod() {
        underTest = XmppServerSocket()
    }

    @After
    fun afterMethod() {
        underTest?.closeConnection()
    }

    @Test
    fun `test construct`() {
        assertNotNull(underTest)
    }

    @Test
    fun `test construct with port`() {
        underTest?.closeConnection()
        underTest = XmppServerSocket(1234)
        assertNotNull(underTest)
    }

    @Test
    @Ignore // This would currently just run into infinity
    fun `test creating a socket`() {
        underTest?.createSocketForNextClient()
    }

    @Test
    @Ignore // TODO: This is an infinite-loop - should the Server close automatically, when the client connects, or wait for the next one
    fun `test creating a socket with connection`() {
        underTest?.createSocketForNextClient()

        val client = Socket("localhost", 9092)
        client.close()
    }
}
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.XMPPConnection
import org.junit.Ignore
import org.junit.Test

class E2ETest {

    // This test acts as an XMPP-client, calling the mapper-server requesting authentication
    @Test
    @Ignore // TODO: E2E does not work yet (needs the full authentication process implemented)
    fun `test setup connection`() {
        val config = ConnectionConfiguration("localhost", 9092)

        config.isSASLAuthenticationEnabled = false
        config.securityMode = ConnectionConfiguration.SecurityMode.disabled

        val connection = XMPPConnection(config)
        connection.connect()
    }
}
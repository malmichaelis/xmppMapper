package socket.io

import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileInputStream
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class XmppBufferedReaderTest {

    private var underTest: XmppBufferedReader? = null
    private var file: File = File.createTempFile("test", "xmppWriter")

    @Before
    fun beforeMethod() {
        val iStream = FileInputStream(file)
        underTest = XmppBufferedReader(iStream)
    }

    @Test
    fun `test construct`() {
        assertNotNull(underTest)
    }

    @Test(expected = IllegalStateException::class)
    fun `test reading a response of an not filled file`() {
        underTest?.readResponse()
    }

    @Test(expected = IllegalStateException::class)
    fun `test reading a response of an empty file`() {
        file.writeText("")
        underTest?.readResponse()
    }

    @Test
    fun `test reading a response of a file with a single line`() {
        val text = "abc"
        file.writeText(text)
        assertEquals(text, underTest?.readResponse())
    }

    @Test
    fun `test reading a response of a file with multiple lines`() {
        val text = "abc\ndef\nghi"
        file.writeText(text)
        assertEquals("abc", underTest?.readResponse())
        assertEquals("def", underTest?.readResponse())
        assertEquals("ghi", underTest?.readResponse())
    }
}
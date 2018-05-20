package socket

import java.net.ServerSocket
import java.net.Socket

class XmppServerSocket(port: Int = 9092) {

    // TODO: Is it ok this way, or do I need an init-function to initialize the socket value? (same for all clases)
    private val socket: ServerSocket = ServerSocket(port)

    // The server currently only accepts a single client
    fun createSocketForNextClient(): Socket = socket.accept()
}
package socket

import java.net.ServerSocket
import java.net.Socket

class XmppServerSocket(port: Int = 9092) {

    private val socket: ServerSocket = ServerSocket(port)

    // TODO: The server currently only accepts a single client
    fun createSocketForNextClient(): Socket = socket.accept()

    fun closeConnection() {
        socket.close()
    }

    // TODO: implement function to get status
}
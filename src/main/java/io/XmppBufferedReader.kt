package io

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class XmppBufferedReader(inputStream: InputStream) {

    var input = BufferedReader(InputStreamReader(inputStream))

    // Waits for an incoming response and returns it
    fun readResponse() = input.readLines()
}
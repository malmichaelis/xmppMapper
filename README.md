# xmppMapper
#Setup:
This project is managed by Maven and has no staging properties yet. So just download and run.

The XmppMapper-class contains the *main*-method, but the functionality can just be run in the tests. **A general
end-to-end solution does not work yet and the main-method therefore just shows an idea of how the workflow could
look like**

#Functionality:
The mapper consists of three different parts, the **Socket**-Connection to an XMPP-Client (e.g. Pidgin or Spark), a 
**REST**-Connection to out *"toChat"* Server, and a **mapper** that maps messages between them, to allow them to
communicate. The XmppMapper-class at the root level contains a *main*-method that contains the currently implemented
workflow.

**Important Note: Not every message needs to be transformed and sent between Client and Server(!) XMPP often requires
more messages to fulfill a task, than our Server does. As an example: The authentication is a multi-step-process
in XMPP, where Client and Mapper do a handshake, discuss the parameters of the connection and in a very last
(and optional) step, send credentials - only after the credentials were received, the Mapper contacts the Server
to authenticate.**

## The Socket
The socket-package contains a **Reader**, a **Writer** and the **ServerSocket** itself. The latter is used to open and 
establish a connection to the client as well as close that connection. 
Future functionality include reading a status, allow multiple clients at once, allow to re-establish a lost connection, ...

### The Reader
**The Reader is currently buggy.** It is supposed to read the messages from the client (one at a time) to allow a constant
communication by reacting to the messages from the Client and referring them to the Server. It takes the `InputSteam`
from the Client to read the messages.

The idea is to have the Reader run in an infinite loop (e.g in it's own thread), waiting and listening to the client.
Whenever it receives a message, it should just return it to the caller and wait for the next. The first approach was
to use the `readText`-method of the `BufferedReader`. However, that automatically closes the connection afterwards.
Unfortunately, the current solution does not allow to re-open a closed connection. Therefore, it was changed to
`readLine`.
The corresponding test-class tests different scenarios, but is based on a local `FileInputStream`. It makes sure, that
exactly one line of the Steam is read at a time and only, if it's not empty.

**Using `readText` works fine for a single message. It is possible, to send a messages to the Client (the first one
is received as discussed below), read the response and send a second message. However, that would close the 
connection. `readLine` instead just completely blocks everything, since it waits for the client to close the connection,
which we do not want as well.**

### The Writer
The `XmppPrintWriter` is used to send messages to the Client. It takes the `OutputStream` from the client and uses
a `PrintWriter` to get the needed functionality to send the messages. For all messages that do not need to be 
communicated with the Server (see Important Note above) it has it's own function, therefore containing all needed
information to send the the client. In the future, the existing `sendToClient`-method will be leveraged for all
messages from the Server that already got transformed by the Mapper.

Each function (besides `sendToClient`, which could be private in the current implementation) has it's own set of tests.
They run against a local temp file, making sure that the correct text was written.

**Problem: The `flush`-method was needed to be used to sent a message, even though `autoflush` was set to true. This
allows exactly one message to be written. Interestingly enough, if the `sendXXX`-methods are called very fast in a
row, the messages seem to all be written. But if some time collapses (e.g. bz waiting for the Client's reaction),
no further messages are sent. This is** not **reflected by the tests, who work fine, but was recognized by checking
the** *Spark* **Debug-Console, recognizing that the Client received the first message, but not the second.**

**The last Test checks that writing messages with a delay works (and is green). If the same code as in the test
is used to send messages to the Client, only the first one is received.**

## The Server-Connection
The implementation of the `ToChatServerConnection` is currently very rudimentary. It allows to authenticate against
our Server. The `authenticate`-method expects the JSON (as a `StringEntity`), sends it to the Server and prints the
response. Future implementation should allow to send more than just authentication messages as well as actually reading
and returning a response.

The corresponding test uses the default *admin* credentials to test a happy-path working authentication 
against the server. **However, the test is currently ignored, since it requires a local instance of our server
running. If the server runs, the `@Ignore` on the test can be removed and it should work fine.** 

## The Mapper
The `XmlJsonMapper` is supposed to take any message from either the Client or the Server and transform it. The required
XML messages from the Client (see the Important Note above) can be transformed into JSON StringEntities and JSON messages
from the Server can be transformed into XMPP XML responses.

**The Mapper is currently an empty shell and only shows the idea of the setup!**

## The main method
The `main`-method in the `XmppMapper` shows the full workflow (at least the current idea) **but does not work yet**. It
establishes the connections to the XMPP clients, opens the connections to our Server and leverages the `XmlJsonMapper`
to allow communication between both.

#Open Tasks
- [ ] Allow for the `XmppPrintWriter` to send more than one message to the Client
- [ ] Allow for the `XmppBufferedReader` to read messages from the Client, without blocking everything else
- [ ] Allow multiple Clients at once
- [ ] Allow to re-connect after a connection was closed
- [ ] Implement the Mapper between XML and JSON
- [ ] Extend the `ServerSocket` (to be able to read a status, allow multiple clients at once, allow to re-establish a lost connection, ...)
- [ ] Extend the `main`-method to have a full e-to-e communication
- [ ] Implement remaining XML messages in the `XmppPrintWriter`
- [ ] Implement proper logging
- [ ] Implement a `ToChatServerConnection`-Test that does not need a local instance of the Server running
- [ ] ...
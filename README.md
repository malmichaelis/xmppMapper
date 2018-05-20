# xmppMapper
Setup:
This project is managed by Maven and has no staging properties yet. So just download and run
There is currently no main class - the code is run through tests

Status:
* Opens a Socket on the default port 9092
* Waits for a client to connect 
** only a single client for now
** Works only for one connection (no reconnect) for now
* Waits for a message from the client (usually authentication)
* Sends a hard-coded response
package mapper

import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.json.JSONObject

class XmlJsonMapper() {

    // TODO: Create functionality that takes the XMPP XML and maps it to a REST "toChat" JSON
    // and the other way around

    fun convertAuthenticationMessage(xml: String): StringEntity {
        val entity = StringEntity("{\"identifier\":\"admin@iconect.io\",\"password\":\"admin\"}", ContentType.create("application/json"))
        throw NotImplementedError()
    }
}
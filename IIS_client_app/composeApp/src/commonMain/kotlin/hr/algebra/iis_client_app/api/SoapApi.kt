package hr.algebra.iis_client_app.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class SoapApi(private val client: HttpClient) {
    private val url = "http://localhost:8080/ws"

    suspend fun searchColors(xpathQuery: String): String {
        val envelope = """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://algebra.hr/iisbackend/soap">
               <soapenv:Header/>
               <soapenv:Body>
                  <soap:GetColorRequest>
                     <soap:xpathQuery>$xpathQuery</soap:xpathQuery>
                  </soap:GetColorRequest>
               </soapenv:Body>
            </soapenv:Envelope>
        """.trimIndent()

        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Text.Xml)
            setBody(envelope)
        }
        
        val responseText = response.bodyAsText()
        // Simple Regex to extract the <ns2:result> content (or similar namespace)
        val regex = "<.*?:result>(.*?)</.*?:result>".toRegex(RegexOption.DOT_MATCHES_ALL)
        val match = regex.find(responseText)
        return match?.groups?.get(1)?.value?.trim() ?: "No result extracted. Raw response: $responseText"
    }
}

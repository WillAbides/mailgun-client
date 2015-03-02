package io.github.willroden.mailgun.client

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient

class MailgunClient {

    String apiKey
    String apiEndpoint = 'https://api.mailgun.net'
    String domain
    Boolean testMode

    private RESTClient restClient() {
        def restClient = new RESTClient(apiEndpoint)
        restClient.auth.basic 'api', apiKey
        return restClient
    }

    public void sendMessage(MailgunMessage message) {
        if (testMode) {
            message.testmode = testMode
        }

        restClient().post(
                path: "/v2/${domain}/messages",
                body: message.toMap(),
                requestContentType: ContentType.URLENC
        )
    }

}
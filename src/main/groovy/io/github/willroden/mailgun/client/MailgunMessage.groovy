package io.github.willroden.mailgun.client

import org.apache.commons.collections.map.MultiValueMap

class MailgunMessage {
    String from, to, cc, bcc

    String subject

    String text

    String html

    // o: options
    String campaign
    public Boolean testmode, tracking,
                   trackingClicks = false,
                   trackingOpens

    // h: headers
    protected Map<String, String> headers = [:]

    public void setReplyTo(String replyAddress) {
        headers.put("Reply-To", replyAddress)
    }

    public String getReplyTo() {
        headers.get("Reply-To")
    }

    public MultiValueMap toMap() {
        def multiValueMap = new MultiValueMap()


        ["from", "to", "cc", "bcc", "subject", "text", "html"].each {
            def value = this."$it"
            if (value != null) {
                multiValueMap.put(it, value)
            }
        }

        ["campaign", "testmode", "tracking", "trackingClicks", "trackingOpens"]
                .collect {new MapEntry("o:${camelCaseToDashes(it)}", this."$it")}.findAll {it.value != null}.each {

            switch (it.value) {
                case Boolean:
                    multiValueMap.put(it.key, it.value ? "yes" : "no")
                    break
                default:
                    multiValueMap.put(it.key, it.value)
                    break
            }
        }

        headers.each{
            multiValueMap.put("h:${it.key}", it.value)
        }

        return multiValueMap
    }

    protected static String camelCaseToDashes( String text ) {
        text.replaceAll( /([A-Z])/, /-$1/ ).toLowerCase().replaceAll( /^-/, '' )
    }
}

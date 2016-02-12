# licensing-demo

This is a simple licensing server which demonstrates how to impelement a third party license server.

If your bitdash license has third-party-license-checking enabled all license calls are passed to your license server.
To do that you have to enable third-party-license-checking, specify an API path which implements a certain structure described below, a timeout which tells our license server how long to wait for your license server to respond and some default actions which tell our license server what to do if an error or timeout occurs.

Your license server is called by a HTTP/POST request with the following payload:

{<br>
	"domain":"dummydomain.com",<br>
	"key": "your_bitdash_licensekey",<br>
	"version": "bitdash_version",<br>
	"customData":{"thirdPartyKey":"key1", "param2":"data2"}<br>
}

The content of the "customData" field is specified by yourself in the bitdash player configuration. It is an array of key/value pairs of type Object.

Response-codes:
The license of a bitdash player is allowed/denied based on the following HTTP response codes of your licensing server:

HTTP code 2xx -> allow license<br>
HTTP code 4xx -> deny license<br>
HTTP code 5xxx -> allow/deny based on datastore configuration<br>


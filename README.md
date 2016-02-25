# 3rd Party Bitmovin Player Licensing

### This is a simple implementation of a 3rd party licensing server which demonstrates the usage in combination with the Bitmovin Adaptive Streaming Player and our backend application.

If a player instance has third-party-license-checking enabled all license calls are passed to a specified 3rd party license server.
To enable third-party-license-checking, simply specify an API path which implements a certain structure described below, a timeout which tells our backend application how long to wait for the 3rd party license server to respond and some default actions which specify what to do if an error or timeout occurs.

The 3rd party license server is called by a HTTP/POST request with the following payload:

{<br>
   "domain"    :"dummydomain.com",<br>
   "key"       : "your_player_licensekey",<br>
   "version"   : "player_version",<br>
   "customData": {<br>
     "thirdPartyKey" :"key1",<br> 
     "param2"        :"data2"<br>
   }<br>
}

The content of the 'customData' field can be of arbitrary and is specified in the player configuration. It's structure is designed as an array of key/value pairs of type 'Object' which is passed through to the 3rd party licensing server.

Response-codes:
The license of a player instance is allowed/denied based on the following HTTP response codes of the 3rd party licensing server:

HTTP code 2xx  -> allow license<br>
HTTP code 4xx  -> deny license<br>
HTTP code 5xxx -> allow/deny based on data-store configuration<br>


To make sure the player always gets a license decision, two default actions have to be configured.

1) Default response on timeout (ALLOW/DENY)<br>
Allows or denies a license if the response time exceeds your configured timeout (default is 200ms).

2) Default response on server error (ALLOW/DENY)<br>
Allows or denies a license if the Bitmovin backend application receives a HTTP code 5xx.

The HTTP response of the 3rd party licensing server can have a "customData" field just as the HTTP call which is passed
through to the player.
   

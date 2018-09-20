# KARA
## Kara, another request analyzer
an HTTP bin in Java

Test it out at [https://kara.rest](https://kara.rest)

I made this for me an my colleagues, tailored to our needs.

## Features
 * Websocket interface
 * Custom response bodies
 * REST api for retrieving bins, results or parts thereof (See the openApi definition)
 * MQTT support
 * docker container available at https://hub.docker.com/r/yunzlez/kara/
 
## Stuff I wanna do at some point
 * Replace thymeleaf interface with React, or something
 * Support custom headers & cookies in the response
 * Support simple template responses (Some work already done, such as an ANTLR4 grammar)
 * Statistics (With fancy graphs)
 * Scriptable responses
 * Externalize configuration
 * Support for something else than MySQL

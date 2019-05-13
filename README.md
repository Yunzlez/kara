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
 
## How to run
 * mvn package
 * just `java -jar kara-bin-1.3.0.jar`, although setting an XMX is a good idea. I found 128M to be a good value. I use:`java -Xmx128M -XX:+UseG1GC -XX:+UseStringDeduplication -XX:-TieredCompilation -jar kara-bin-1.3.0.jar -Dspring.profiles.active=remote`
 
You can pass the following options using -D :
* spring.datasource.url -> JDBC connections string. Use 'kara' as the schema, other names are not yet supported
* spring.datasource.username -> DB username
* spring.datasource.password -> DB Pass
* server.use-forward-headers -> true if running behind proxy (default). Kara needs this to detect the base URL.
* mqtt.broker.url -> broker to use if MQTT is enabled
* mqtt.enabled -> enable MQTT
* mqtt.clientid -> ClientId to use for publishing
* logging.level.be.zlz.kara -> The logging level

You can also run using  `-Dspring.profiles.active=docker`, which allows you to use environment variables for some of these settings:
* DATABASE_URI -> The database URI (not a jdbc string, just the host)
* DATABASE_USR -> DB user
* DATABASE_PWD -> DB password
* MQTT_ENDPOINT -> The MQTT broker uri
* MQTT_ENABLED -> Whether to enable MQTT (default true)

Obviously, you can mix these up with the previous set of settings.
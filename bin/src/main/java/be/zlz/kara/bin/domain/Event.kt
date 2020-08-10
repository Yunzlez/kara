package be.zlz.kara.bin.domain

import be.zlz.kara.bin.domain.converter.MapToSmileConverter
import be.zlz.kara.bin.domain.enums.Source
import org.springframework.http.HttpMethod
import java.time.LocalDateTime
import javax.persistence.*


@Entity
open class Event {

    @Id
    open var id: String? = null

    open lateinit var body: ByteArray

    @Enumerated(EnumType.STRING)
    open var method: HttpMethod? = null

    @Enumerated(EnumType.STRING)
    open var source: Source? = null

    open var location: String? = null

    @Convert(converter = MapToSmileConverter::class)
    open  var metadata: Map<String, String>? = null
    @Convert(converter = MapToSmileConverter::class)
    @Column(name = "additional_data")
    open var additionalData: Map<String, String>? = null

    @Column(name = "content_type")
    open var contentType: String? = null

    @Column(name = "event_time")
    open var eventTime: LocalDateTime? = null

    open var origin: String? = null

    @Column(name = "body_size")
    open var bodySize: Long = 0

    @Column(name = "protocol_version")
    open var protocolVersion: String? = null

    @ManyToOne
    open var bin: Bin? = null

    protected constructor()

    constructor(id: String?, body: ByteArray, method: HttpMethod?, source: Source?, location: String?, metadata: Map<String, String>?, additionalData: Map<String, String>?, contentType: String?, eventTime: LocalDateTime?, origin: String?, bodySize: Long, protocolVersion: String?, bin: Bin?) {
        this.id = id
        this.body = body
        this.method = method
        this.source = source
        this.location = location
        this.metadata = metadata
        this.additionalData = additionalData
        this.contentType = contentType
        this.eventTime = eventTime
        this.origin = origin
        this.bodySize = bodySize
        this.protocolVersion = protocolVersion
        this.bin = bin
    }

}
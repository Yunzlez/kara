package be.zlz.kara.bin.domain

import be.zlz.kara.bin.domain.converter.MapToSmileConverter
import be.zlz.kara.bin.domain.enums.Source
import org.springframework.http.HttpMethod
import java.time.LocalDateTime
import javax.persistence.*


@Entity
class Event {

    @Id
    var id: String? = null
        private set

    lateinit var body: ByteArray
        private set

    @Enumerated(EnumType.STRING)
    var method: HttpMethod? = null
        private set

    @Enumerated(EnumType.STRING)
    var source: Source? = null
        private set

    var location: String? = null
        private set

    @Convert(converter = MapToSmileConverter::class)
    var metadata: Map<String, String>? = null
        private set

    @Convert(converter = MapToSmileConverter::class)
    @Column(name = "additional_data")
    var additionalData: Map<String, String>? = null
        private set

    @Column(name = "content_type")
    var contentType: String? = null
        private set

    @Column(name = "event_time")
    var eventTime: LocalDateTime? = null
        private set

    var origin: String? = null
        private set

    @Column(name = "body_size")
    var bodySize: Long = 0
        private set

    @Column(name = "protocol_version")
    var protocolVersion: String? = null
        private set

    @ManyToOne
    var bin: Bin? = null
        private set

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
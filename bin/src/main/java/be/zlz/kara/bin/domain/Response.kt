package be.zlz.kara.bin.domain

import be.zlz.kara.bin.domain.converter.MapToSmileConverter
import be.zlz.kara.bin.domain.enums.ContentType
import be.zlz.kara.bin.dto.v11.ResponseOrigin
import org.springframework.http.HttpStatus
import javax.persistence.*

@Entity
open class Response {

    @Id
    open var id: Long? = null

    @Enumerated(EnumType.STRING)
    open var code: HttpStatus? = null

    @Column(name = "content_type")
    open var contentType: String? = null

    open var body: ByteArray? = null

    @Convert(converter = MapToSmileConverter::class)
    open var headers: Map<String, String>? = null

    @Column(name = "content_type")
    open var responseType: ContentType? = null

    @Column(name = "response_origin")
    @Enumerated(EnumType.STRING)
    open var responseOrigin: ResponseOrigin? = null

    protected constructor()

    constructor(id: Long, code: HttpStatus, contentType: String, body: ByteArray?, headers: Map<String, String>, responseType: ContentType, responseOrigin: ResponseOrigin) {
        this.id = id
        this.code = code
        this.contentType = contentType
        this.body = body
        this.headers = headers
        this.responseType = responseType
        this.responseOrigin = responseOrigin
    }


}
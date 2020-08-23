package be.zlz.kara.bin.domain

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
open class ModuleEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    open var binId: Long = 0

    open var timestamp: LocalDateTime = LocalDateTime.now()

    open var message: String = ""

    open var isError: Boolean = false

    protected constructor()

    constructor(id: Long, binId: Long, timestamp: LocalDateTime, message: String, isError: Boolean) {
        this.id = id
        this.binId = binId
        this.timestamp = timestamp
        this.message = message
        this.isError = isError
    }


}
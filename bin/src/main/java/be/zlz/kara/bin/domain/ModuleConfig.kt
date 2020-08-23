package be.zlz.kara.bin.domain

import javax.persistence.*

@Entity
open class ModuleConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @Column(name = "bin_id")
    open var binId: Long = 0

    @Column(name = "module_key")
    open var moduleKey: String? = null

    open var config: String? = null

    open var sync: Boolean = true

    protected constructor()

    constructor(id: Long, binId: Long, moduleKey: String?, config: String?, sync: Boolean) {
        this.id = id
        this.binId = binId
        this.moduleKey = moduleKey
        this.config = config
        this.sync = sync
    }
}
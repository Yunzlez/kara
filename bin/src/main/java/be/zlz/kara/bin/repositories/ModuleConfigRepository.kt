package be.zlz.kara.bin.repositories

import be.zlz.kara.bin.domain.ModuleConfig
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ModuleConfigRepository: JpaRepository<ModuleConfig, Long> {

    fun getModuleConfigByBinIdAndModuleKey(binId: Long, moduleKey: String): Optional<ModuleConfig>

    fun getModuleConfigByBinId(binId: Long): List<ModuleConfig>

}
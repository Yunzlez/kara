package be.zlz.kara.bin.services

import be.zlz.kara.bin.config.logger
import be.zlz.kara.bin.domain.Bin
import be.zlz.kara.bin.domain.Event
import be.zlz.kara.bin.domain.Reply
import be.zlz.kara.bin.modules.KaraModule
import be.zlz.kara.bin.repositories.ModuleConfigRepository
import be.zlz.kara.bin.repositories.ModuleEventRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ModuleServiceImpl(
        @Qualifier("moduleMap") val loadedModules: Map<String, KaraModule>,
        val moduleConfigRepository: ModuleConfigRepository,
        val moduleEventRepository: ModuleEventRepository
): ModuleService {

    //todo cache moduleConfigs/bin

    //todo clear cache when updating config for bin

    val log by logger()

    override fun handleModulesForBin(bin: Bin, event: Event): Reply? {
        val moduleConfigs = moduleConfigRepository.getModuleConfigByBinId(bin.id)
        if (moduleConfigs.isEmpty()) {
            return null
        }

        val syncModule = moduleConfigs.stream()
                .filter { it.sync }
                .findFirst() //todo for now, since there's only 1 module
        if (syncModule.isEmpty) {
            return null
        }

        val syncModuleData = syncModule.get()
        val module = loadedModules[syncModuleData.moduleKey]
        if (module == null) {
            log.warn("Module ${syncModuleData.moduleKey} not available (requested in config ${syncModuleData.id})")
            return null
        }

        return syncModuleData.config?.let { module.handleEventSync(it, event) }
    }

}
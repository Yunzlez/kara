package be.zlz.kara.bin.services

import be.zlz.kara.bin.domain.*

interface ModuleService {

    fun handleModulesForBin(bin: Bin, event: Event): Response?

    fun retrieveModuleConfig(bin: Bin, moduleKey: String): ModuleConfig?
    //module CRUD

}

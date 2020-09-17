package be.zlz.kara.bin.services

import be.zlz.kara.bin.domain.Bin
import be.zlz.kara.bin.domain.Event
import be.zlz.kara.bin.domain.Reply
import be.zlz.kara.bin.domain.Response

interface ModuleService {

    fun handleModulesForBin(bin: Bin, event: Event): Response?

    //module CRUD

}

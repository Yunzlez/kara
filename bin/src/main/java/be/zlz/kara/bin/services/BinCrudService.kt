package be.zlz.kara.bin.services

import be.zlz.kara.bin.dto.BinListDto
import be.zlz.kara.bin.dto.PagedList
import be.zlz.kara.bin.dto.v11.BinSettingsDto

interface BinCrudService {

    fun createBin(): String

    fun pagedBinList(page: Int, limit: Int): PagedList<BinListDto?>

    fun deleteBinIfEmpty(binName: String)

    fun getBinSettings(binName: String): BinSettingsDto

    fun updateSettings(name: String, updated: BinSettingsDto): BinSettingsDto

}
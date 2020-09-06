package be.zlz.kara.bin.services

import be.zlz.kara.bin.domain.Bin
import be.zlz.kara.bin.dto.BinListDto
import be.zlz.kara.bin.dto.PagedList
import be.zlz.kara.bin.dto.RequestCountDto
import be.zlz.kara.bin.dto.v11.BinSettingsDto
import be.zlz.kara.bin.exceptions.BadRequestException
import be.zlz.kara.bin.exceptions.ResourceNotFoundException
import be.zlz.kara.bin.repositories.BinRepository
import be.zlz.kara.bin.util.PagingUtils
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

@Service
class BinCrudServiceImpl(
        private val binRepository: BinRepository
): BinCrudService {

    val logger by logger()

    override fun createBin(): String {
        val name = UUID.randomUUID().toString()

        val bin = Bin(name)
        binRepository.save(bin)
        logger.info("created bin with UUID {}", name)
        return name
    }

    override fun pagedBinList(page: Int, limit: Int): PagedList<BinListDto> {
        val bins: Page<Bin> = binRepository.findAll(PagingUtils.getPageable(page, limit))
        val pageMeta = PagingUtils.createPageMeta(
                page,
                bins.totalElements,
                bins.totalPages,
                "/bins",
                limit
        )
        return PagedList(toBinListDto(bins), pageMeta)
    }

    override fun deleteBinIfEmpty(binName: String) {
        val bin = binRepository.findBinByName(binName)
        if (bin.isEmpty) {
            return
        }
        if (bin.get().requestCount != 0) {
            throw BadRequestException("Bin cannot be deleted: Not empty")
        }
        binRepository.delete(bin.get())
    }

    override fun getBinSettings(binName: String): BinSettingsDto {
        val bin = binRepository.findBinByName(binName)
        if (bin.isEmpty) {
            throw ResourceNotFoundException("No bin with name $binName exists")
        }
        // - create new response object & table
        // - update webhook settings
        // - update response settings
        TODO()
    }

    override fun updateSettings(name: String, updated: BinSettingsDto): BinSettingsDto {
        TODO("Not yet implemented")
    }

    private fun toBinListDto(bins: Page<Bin>): List<BinListDto?>? {
        return bins.stream().map { bin: Bin ->
            BinListDto(
                    bin.name,
                    RequestCountDto(bin.requestCount, bin.requestMetric.counts)
            )
        }.collect(Collectors.toList())
    }
}
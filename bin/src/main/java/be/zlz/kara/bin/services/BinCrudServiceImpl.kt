package be.zlz.kara.bin.services

import be.zlz.kara.bin.config.logger
import be.zlz.kara.bin.domain.Bin
import be.zlz.kara.bin.dto.BinListDto
import be.zlz.kara.bin.dto.PagedList
import be.zlz.kara.bin.dto.RequestCountDto
import be.zlz.kara.bin.dto.v11.BinSettingsDto
import be.zlz.kara.bin.dto.v11.ResponseSettingsDto
import be.zlz.kara.bin.dto.v11.WebhookSettingsDto
import be.zlz.kara.bin.exceptions.BadRequestException
import be.zlz.kara.bin.exceptions.ResourceNotFoundException
import be.zlz.kara.bin.modules.webhook.Webhook
import be.zlz.kara.bin.modules.webhook.WebhookSettings
import be.zlz.kara.bin.repositories.BinRepository
import be.zlz.kara.bin.util.PagingUtils
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

@Service
class BinCrudServiceImpl(
        private val binRepository: BinRepository,
        private val moduleService: ModuleService
): BinCrudService {

    val logger by logger()

    companion object {
        private val om = jacksonObjectMapper()
    }

    override fun createBin(): String {
        val name = UUID.randomUUID().toString()

        val bin = Bin(name)
        binRepository.save(bin)
        logger.info("created bin with UUID {}", name)
        return name
    }

    override fun pagedBinList(page: Int, limit: Int): PagedList<BinListDto?> {
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
        val binOptional = binRepository.findBinByName(binName)
        if (binOptional.isEmpty) {
            throw ResourceNotFoundException("No bin with name $binName exists")
        }
        val bin = binOptional.get()

        val webhookConfig = moduleService.retrieveModuleConfig(bin, Webhook.WEBHOOK_NAME)
        var webhooksettings: WebhookSettings? = null
        if (webhookConfig?.config != null) {
            webhooksettings = om.readValue<WebhookSettings>(webhookConfig.config!!)
        }

        return BinSettingsDto(
                bin.name,
                bin.isPermanent,
                ResponseSettingsDto(bin.response),
                if (webhooksettings == null) null else WebhookSettingsDto(webhooksettings)
        )

        // - update webhook settings
        // - update response settings
        // - WS endpoints in openapi def
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
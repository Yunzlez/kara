package be.zlz.kara.bin.dto.v11

import be.zlz.kara.bin.domain.enums.Interpretation

data class ResponseSettingsDto(
        val code: Int?,
        val contentType: String?,
        val body: String?,
        val headers: Map<String, String>?,
        val responseType: Interpretation?,
        val responseOrigin: ResponseOrigin?
)
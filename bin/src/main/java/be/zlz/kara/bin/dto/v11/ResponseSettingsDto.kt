package be.zlz.kara.bin.dto.v11

import be.zlz.kara.bin.domain.Response
import be.zlz.kara.bin.domain.enums.Interpretation
import java.util.*

data class ResponseSettingsDto(
        var code: Int?,
        var contentType: String?,
        var body: String?,
        var headers: Map<String, String>?,
        var responseType: Interpretation?,
        var responseOrigin: ResponseOrigin?
) {
}
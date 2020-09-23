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
    constructor(response: Response) {
        code = response.code.value()
        contentType = response.contentType
        if (response.body != null) {
            body = if (response.responseType == Interpretation.TEXT) response.body!!.toString(Charsets.UTF_8) else Base64.getEncoder().encodeToString(response.body)
        }
        headers = response.headers
        responseType = response.responseType
        responseOrigin = response.responseOrigin
    }
}
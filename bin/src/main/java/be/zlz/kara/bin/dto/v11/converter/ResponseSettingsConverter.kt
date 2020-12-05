package be.zlz.kara.bin.dto.v11.converter

import be.zlz.kara.bin.domain.Response
import be.zlz.kara.bin.domain.enums.Interpretation
import be.zlz.kara.bin.dto.converter.Converter
import be.zlz.kara.bin.dto.v11.ResponseSettingsDto
import org.springframework.http.HttpStatus
import java.util.*

class ResponseSettingsConverter : Converter<Response, ResponseSettingsDto> {

    override fun toDto(response: Response): ResponseSettingsDto {
        var body: String? = null;
        if (response.body != null) {
            body = if (response.responseType == Interpretation.TEXT) response.body!!.toString(Charsets.UTF_8) else Base64.getEncoder().encodeToString(response.body)
        }
        return ResponseSettingsDto(
                response.code.value(),
                response.contentType,
                body,
                response.headers,
                response.responseType,
                response.responseOrigin
        )
    }

    override fun toDomain(responseSettings: ResponseSettingsDto): Response {
        var body: ByteArray? = null;
        if (responseSettings.body != null) {
            body = if (responseSettings.responseType == Interpretation.TEXT) responseSettings.body!!.toByteArray(Charsets.UTF_8) else Base64.getDecoder().decode(responseSettings.body)
        }
        return Response(
                HttpStatus.valueOf(responseSettings.code!!),
                responseSettings.contentType!!,
                body,
                responseSettings.headers!!,
                Interpretation.BINARY,
                responseSettings.responseOrigin!!
        )
    }

}
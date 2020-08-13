package be.zlz.kara.bin.domain.converter

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.smile.SmileFactory
import java.io.IOException
import java.util.*
import javax.persistence.AttributeConverter
import javax.persistence.Converter


@Converter
class MapToSmileConverter : AttributeConverter<Map<String, String>, ByteArray> {

    override fun convertToDatabaseColumn(map: Map<String, String>?): ByteArray? {
        return try {
            if (map == null || map.isEmpty()) {
                return null
            }
            om.writeValueAsBytes(map)
        } catch (e: JsonProcessingException) {
            throw IllegalStateException("Unable to serialize map to smile")
        }
    }

    override fun convertToEntityAttribute(bytes: ByteArray?): Map<String, String> {
        return try {
            if (bytes == null) {
                return Collections.emptyMap()
            }
            om.readValue(bytes, object : TypeReference<Map<String, String>>() {})
        } catch (e: IOException) {
            throw IllegalStateException("Unable to deserialize map from smile")
        }
    }

    companion object {
        private val om = ObjectMapper(SmileFactory())
    }
}

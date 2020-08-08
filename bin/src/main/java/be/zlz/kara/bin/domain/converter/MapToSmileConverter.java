package be.zlz.kara.bin.domain.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.Map;

@Converter
public class MapToSmileConverter implements AttributeConverter<Map<String, String>, byte[]> {

    private static final ObjectMapper om = new ObjectMapper(new SmileFactory());

    @Override
    public byte[] convertToDatabaseColumn(Map<String, String> map) {
        try {
            return om.writeValueAsBytes(map);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize map to smile");
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(byte[] bytes) {
        try {
            return om.readValue(bytes, new TypeReference<>() {});
        } catch (IOException e) {
            throw new IllegalStateException("Unable to deserialize map from smile");
        }
    }
}

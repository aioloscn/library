package com.aiolos.library.pojo.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author Aiolos
 * @date 2021/4/19 8:09 上午
 */
@Slf4j
public class LongJsonDeserializer extends JsonDeserializer<Long> {
    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String value = jsonParser.getText();
        try {
            return value == null ? null : Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.error("解析长整型错误", e);
            return null;
        }
    }
}

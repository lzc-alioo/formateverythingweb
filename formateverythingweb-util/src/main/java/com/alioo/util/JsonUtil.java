package com.alioo.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: liudi
 * Date: 2018/7/10
 * Time: 下午2:40
 */
public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature
        .FAIL_ON_UNKNOWN_PROPERTIES).disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    /**
     * 将对象序列化
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            LOGGER.error("toJson error. json:", e);
        }
        return null;
    }

    /**
     * 反序列化对象字符串
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("toObject error. json:" + json, e);
        }
        return null;
    }

    /**
     * 反序列化字符串成为对象
     *
     * @param json
     * @param valueTypeRef
     * @return
     */
    public static <T> T toObject(String json, TypeReference<T> valueTypeRef) {
        try {
            if (StringUtil.isEmpty(json)) {
                return null;
            }
            return objectMapper.readValue(json, valueTypeRef);
        } catch (IOException e) {
            LOGGER.error("toObject error. json:" + json, e);
        }
        return null;
    }

    /**
     * 从json中获取某一字段
     *
     * @param json
     * @param field
     * @return
     */
    public static String getField(String json, String field) {
        JsonFactory factory = objectMapper.getFactory();
        try {
            JsonParser jp = factory.createParser(json);
            JsonNode jsonNode = objectMapper.readTree(jp);
            JsonNode fieldNode = jsonNode.get(field);
            if (fieldNode != null) {
                return fieldNode.asText();
            }
        } catch (IOException e) {
            LOGGER.error("toObject error. json:" + json, e);
        }
        return null;
    }

    public static JsonNode toJsonNode(String json) {
        JsonFactory factory = objectMapper.getFactory();
        try {
            JsonParser jp = factory.createParser(json);
            return objectMapper.readTree(jp);
        } catch (IOException e) {
            LOGGER.error("toObject error. json:" + json, e);
        }
        return null;

    }

}

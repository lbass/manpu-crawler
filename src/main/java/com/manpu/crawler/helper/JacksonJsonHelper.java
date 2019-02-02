package com.manpu.crawler.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public final class JacksonJsonHelper {

	protected static final Logger logger = LoggerFactory.getLogger(JacksonJsonHelper.class);
	private static final ObjectMapper _defaultObjectMapper;
	private static final SimpleModule BIG_NUMBER_TYPE_MODULE = new SimpleModule();
	private static final Gson DEFAULT_GSON = new Gson();
	static {
		_defaultObjectMapper = new ObjectMapper();
		_defaultObjectMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
		// _defaultObjectMapper.setDateFormat(DateHelper.SIMPLE_DATE_FORMAT);
		_defaultObjectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		_defaultObjectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		_defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		SimpleModule module = new SimpleModule();
		module.addSerializer(BigInteger.class, new ToStringSerializer());
		module.addSerializer(BigDecimal.class, new ToStringSerializer());
		_defaultObjectMapper.registerModule(BIG_NUMBER_TYPE_MODULE);
	}

	public static final ObjectMapper getDefaultObjectMapper() {
		return _defaultObjectMapper;
	}

	public static ObjectMapper createDefaultObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		// objectMapper.setDateFormat(DateHelper.SIMPLE_DATE_FORMAT);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(BIG_NUMBER_TYPE_MODULE);
		return objectMapper;
	}

	public static JavaType getListJavaType(Class<?> clzz) {
		return _defaultObjectMapper.getTypeFactory().constructCollectionType(List.class, clzz);
	}

	public static String writeValueAsString(Object o) {
		if (o == null) {
			return null;
		}
		try {
			return _defaultObjectMapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			logger.error("object to string convert error [{}]", e);
			throw new RuntimeException(e);
		}
	}

	public static String writeRawValueAsString(Object o) {
		if (o == null) {
			return null;
		}
		try {
			return DEFAULT_GSON.toJson(o);
		} catch (Exception e) {
			logger.error("object to string convert error [{}]", e);
			throw new RuntimeException(e);
		}
	}

	public static byte[] writeValueAsBytes(Object o) {
		if (o == null) {
			return null;
		}
		try {
			return _defaultObjectMapper.writeValueAsBytes(o);
		} catch (JsonProcessingException e) {
			logger.error("object to string convert error [{}]", e);
			throw new RuntimeException(e);
		}
	}

	public static String writeValueAsPrettyString(Object o) {
		if (o == null) {
			return null;
		}
		try {
			return _defaultObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
		} catch (JsonProcessingException e) {
			logger.error("object to string convert error [{}]", e);
			throw new RuntimeException(e);
		}
	}

	public static <T> T convertTo(Object from, Class<T> type) {
		if (from == null) {
			return null;
		}

		if (type == null) {
			return null;
		}
		return _defaultObjectMapper.convertValue(from, type);
	}

	public static <T> T convertTo(Object from, JavaType type) {
		if (from == null) {
			return null;
		}

		if (type == null) {
			return null;
		}
		return _defaultObjectMapper.convertValue(from, type);
	}

	public static <T> T convertToObject(Class<T> type, byte[] content) {
		if (content == null) {
			return null;
		}

		if (type == null) {
			return null;
		}
		try {
			return _defaultObjectMapper.readValue(content, type);
		} catch (Exception e) {
			logger.error("string to object convert error [{}]", e);
			throw new RuntimeException(e);
		}
	}

	public static <T> T convertToObject(JavaType type, String content) {
		if (content == null) {
			return null;
		}

		if (type == null) {
			return null;
		}
		try {
			return _defaultObjectMapper.readValue(content, type);
		} catch (Exception e) {
			logger.error("string to object convert error [{}]", e);
			throw new RuntimeException(e);
		}
	}

	public static <T> T convertToObject(Class<T> type, String content) {
		if (content == null || content.isEmpty()) {
			return null;
		}

		if (type == null) {
			return null;
		}
		try {
			if (List.class.isAssignableFrom(type)) {
				JavaType listJavaType = getListJavaType(type);
				return _defaultObjectMapper.readValue(content, listJavaType);
			} else {
				return _defaultObjectMapper.readValue(content, type);
			}
		} catch (Exception e) {
			logger.error("string to object convert error [{}]", e);
			throw new RuntimeException(e);
		}
	}
}
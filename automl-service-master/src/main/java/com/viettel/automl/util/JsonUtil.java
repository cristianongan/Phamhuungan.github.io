package com.viettel.automl.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.exception.ServerException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@UtilityClass
public class JsonUtil {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final HashMap<String, String> mapUser = new HashMap<>();

	public HashMap<String, String> getListUser() {
		if (mapUser.isEmpty()) {
			Resource resource = new ClassPathResource(Const.USERS_CONFIG_FILE);
			try {
				JsonNode jsonNode = objectMapper.readValue(resource.getFile(), JsonNode.class);
				for (JsonNode json : jsonNode) {
					mapUser.put(json.get("username").asText(), json.get("password").asText());
				}
			} catch (IOException error) {
				log.error("listUser() in jsonUtil :", error);
			}
		}
		return mapUser;
	}

	public Map<String, Object> toMap(Object json) {
		return objectMapper.convertValue(json, Map.class);
	}

	public Map<String, Object> toMap(String json) throws IOException {
		return objectMapper.readValue(json, Map.class);
	}

	public String writeAsString(Map<String, Object> value) {
		try {
			return objectMapper.writeValueAsString(value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServerException(ErrorCode.JSON_WRONG_FORMAT);
		}
	}

	public static List<ObjectNode> _toJson(List<Tuple> results) {

		List<ObjectNode> json = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();

		for (Tuple t : results) {
			List<TupleElement<?>> cols = t.getElements();

			ObjectNode one = mapper.createObjectNode();

			for (TupleElement col : cols) {
				one.put(col.getAlias(), t.get(col.getAlias()).toString());
			}

			json.add(one);
		}

		return json;
	}
}

package org.lab.roomboo.api.query;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

public class BasicQueryDsl {

	private final Map<String, String> map;

	public BasicQueryDsl(String expression) {
		map = new HashMap<>();
		if (StringUtils.isNotBlank(expression)) {
			String[] values = expression.split(";");
			for (String value : values) {
				String[] tmp = value.split("==");
				if (tmp.length > 1) {
					map.put(tmp[0], tmp[1]);
				}
			}
		}
	}

	public String get(String key) {
		return map.get(key);
	}

	public <T> T get(String key, Class<T> type) {
		return get(key, type, null);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> type, T defaultValue) {
		if (!map.containsKey(key)) {
			return defaultValue;
		}
		else if (Boolean.class.equals(type)) {
			return (T) Boolean.valueOf(map.get(key));
		} else if(Integer.class.equals(type)) {
			return (T) Integer.valueOf(map.get(key));
		}
		throw new NotImplementedException("Not implemented conversion");
	}

}

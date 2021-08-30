package com.epam.esm.persistence.util.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@Getter
public enum QueryOrder {
    NO,
    ASC,
    DESC;

    private static final Map<String, QueryOrder> namesMap = new HashMap<String, QueryOrder>(3);

    static {
        namesMap.put("asc", ASC);
        namesMap.put("desc", DESC);
        namesMap.put("no", NO);
    }

    @JsonCreator
    public static QueryOrder forValue(Object value) {
        return namesMap.get(value.toString().toLowerCase());
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, QueryOrder> entry : namesMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null;
    }
}

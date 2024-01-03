package com.healerjean.proj.utils.diff;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * AggregareFieldDifference
 *
 * @author zhangyujin
 * @date 2023/12/20
 */
@Getter
@Setter
public class AggregareFieldDifference extends FieldDifference {

    private Map<String, FieldDifference> fieldDifferences;

    private final Identifier identifier;

    public AggregareFieldDifference(String name, Type type, Object snapshotValue, Object tracValue, DifferenceType differenceType, Identifier identifier) {
        super(name, type, snapshotValue, tracValue, differenceType);
        this.identifier = identifier;
        this.fieldDifferences = new HashMap<>();
    }
}

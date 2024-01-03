package com.healerjean.proj.utils.diff;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * CollectionFieldDifference
 *
 * @author zhangyujin
 * @date 2023/12/20
 */

@Getter
@Setter
public class CollectionFieldDifference extends FieldDifference {

    /**
     * 集合元素差异
     */
    private List<FieldDifference> elementDifference;

    public CollectionFieldDifference(String name, Type type, Object snapshotValue, Object tracValue) {
        super(name, type, snapshotValue, tracValue);
        this.elementDifference = new ArrayList<>();
    }
    public CollectionFieldDifference(String name, Type type, Object snapshotValue, Object tracValue, DifferenceType differenceType) {
        super(name, type, snapshotValue, tracValue, differenceType);
        this.elementDifference = new ArrayList<>();
    }
}
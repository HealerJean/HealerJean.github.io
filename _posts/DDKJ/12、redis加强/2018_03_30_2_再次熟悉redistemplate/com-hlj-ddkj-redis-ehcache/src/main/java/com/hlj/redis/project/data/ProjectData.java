package com.hlj.redis.project.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * Created by j.sh on 28/11/2017.
 */
@Data
@Accessors(chain = true)
public class ProjectData {


    private String name;
    private String Group;

    public ProjectData() {
    }

    public ProjectData(String name, String group) {
        this.name = name;
        Group = group;
    }
}

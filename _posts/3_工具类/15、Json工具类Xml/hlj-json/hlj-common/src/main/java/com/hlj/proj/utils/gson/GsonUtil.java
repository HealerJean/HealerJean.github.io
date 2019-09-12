package com.hlj.proj.utils.gson;

import com.google.gson.*;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author HealerJean
 * @ClassName GsonUtils
 * @date 2019/9/12  11:41.
 * @Description
 */
public class GsonUtil {

    private static Gson gson = null;
    private static JsonParser parser = null;

    static {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(java.util.Date.class, new DateSerializer()).setDateFormat(DateFormat.LONG);
        gb.registerTypeAdapter(java.util.Date.class, new DateDeserializer()).setDateFormat(DateFormat.LONG);
        gson = gb.create();
        parser = new JsonParser();

    }

    /**
     * 转成json
     */
    public static String toJsonString(Object object) {
        return gson.toJson(object);
    }

    /**
     * 转成bean
     */
    public static <T> T toObject(String gsonString, Class<T> cls) {
        return gson.fromJson(gsonString, cls);
    }

    /**
     * 转换成JsonObject
     */
    public static JsonObject toJsonObject(String gsonString) {
        return parser.parse(gsonString).getAsJsonObject();
    }

    /**
     * 转换成数组
     */
    public static JsonArray toJsonArray(String gsonString) {
        return parser.parse(gsonString).getAsJsonArray();
    }


    /**
     * 对象转 JsonObject
     */
    public static JsonObject toJsonObject(Object object) {
        return toJsonElement(object).getAsJsonObject();
    }


    public static JsonElement toJsonElement(Object object) {
        return parser.parse(toJsonString(object));
    }



    /**
     * 转成list
     */
    public static <T> List<T> toList(String json, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        JsonArray array = parser.parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }


    /**
     * 合并多个JsonObejct对象
     */
    public static JsonObject addAll(JsonObject targetJsonObect, JsonObject tempJsonObejct) {
        Set<String> keys = tempJsonObejct.keySet();
        for (String key : keys) {
            targetJsonObect.add(key, tempJsonObejct.get(key));
        }
        return targetJsonObect;
    }


}

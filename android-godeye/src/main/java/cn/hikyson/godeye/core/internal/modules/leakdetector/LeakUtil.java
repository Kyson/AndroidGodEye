package cn.hikyson.godeye.core.internal.modules.leakdetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.annotations.Nullable;

public class LeakUtil {

    @Nullable
    public static String serialize(LeakRefInfo leakRefInfo) {
        if (leakRefInfo == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("excludeRef", leakRefInfo.isExcludeRef());
            if (leakRefInfo.getExtraInfo() != null) {
                JSONObject mapObject = new JSONObject();
                for (Map.Entry<String, String> entry : leakRefInfo.getExtraInfo().entrySet()) {
                    mapObject.put(entry.getKey(), entry.getValue());
                }
                jsonObject.put("extraInfo", mapObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return jsonObject.toString();
    }
    @Nullable
    public static LeakRefInfo deserialize(@Nullable String json) {
        LeakRefInfo leakRefInfo = null;
        if (json == null) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            boolean excludeRef = jsonObject.optBoolean("excludeRef");
            Map<String, String> extraInfo = null;
            JSONObject mapObject = jsonObject.optJSONObject("extraInfo");
            if (mapObject != null) {
                extraInfo = new HashMap<>();
                Iterator iterator = mapObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String value = mapObject.optString(key);
                    extraInfo.put(key, value);
                }
            }
            leakRefInfo = new LeakRefInfo(excludeRef, extraInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return leakRefInfo;
    }
}

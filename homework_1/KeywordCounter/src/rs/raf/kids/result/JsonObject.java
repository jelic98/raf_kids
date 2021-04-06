package rs.raf.kids.result;

import java.util.Map;

public class JsonObject<K, V> {

    private final Map<K, V> map;

    public JsonObject(Map<K, V> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        int i = 0;

        for(Map.Entry<K, V> e : map.entrySet()) {
            if(i++ > 0) {
                sb.append(", ");
            }

            sb.append(e.getKey());
            sb.append("=");
            sb.append(e.getValue());
        }

        sb.append("}");

        return sb.toString();
    }
}

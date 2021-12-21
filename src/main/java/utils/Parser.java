package utils;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<List<Object>> list;
    private String value = null;
    public Parser() {
        list = new ArrayList<>();
    }
    public void addValue(String parseID, Object parseValue) {
        List<Object> valueList = new ArrayList<>();
        valueList.add(parseID);
        valueList.add(parseValue);
        for (var i = 0; i < list.size(); i++) {
            List<Object> oldValueList = list.get(i);
            if (oldValueList.contains(valueList.get(0))) {
                list.set(i, valueList);
                return;
            }
        }
        list.add(valueList);
    }
    public void parseText(String textToParse) {
        value = textToParse;
        for (List<Object> valueList : list)
            value = value.replace(valueList.get(0).toString(), valueList.get(1).toString());
    }
    public String parseAndGet(String textToParse) {
        parseText(textToParse);
        return getValue();
    }
    public String getValue() { return value; }
}

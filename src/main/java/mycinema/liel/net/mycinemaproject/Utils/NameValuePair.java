package mycinema.liel.net.mycinemaproject.Utils;

public class NameValuePair {

    private String mName;
    private String mValue;

    public NameValuePair(String name, String value) {
        mName = name;
        mValue = value;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }
}
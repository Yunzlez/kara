package be.zlz.kara.bin.domain.enums;

public enum BinConfigKey {

    PERMANENT_KEY("isPermanent"),
    BINARY_BODY("hasBinaryBody");

    private String value;

    BinConfigKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

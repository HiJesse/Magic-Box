package cn.jesse.magicbox.data;

/**
 * 魔盒设备信息 app信息数据
 *
 * @author jesse
 */
public class MagicBoxDeviceAppInfoData {
    private String name;
    private String value;

    public MagicBoxDeviceAppInfoData() {
    }

    public MagicBoxDeviceAppInfoData(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

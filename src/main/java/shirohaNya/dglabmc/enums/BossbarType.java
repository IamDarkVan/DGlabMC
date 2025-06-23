package shirohaNya.dglabmc.enums;

public enum BossbarType {
    // 无 只显示A 只显示B 都显示 合并显示
    NONE("NONE"), A("A"), B("B"), BOTH("BOTH");
    private final String text;

    BossbarType(String text) {
        this.text = text;
    }

    public static BossbarType toType(String text) throws IllegalArgumentException{
        for (BossbarType type : BossbarType.values()) if (text.equalsIgnoreCase(type.text)) return type;
        throw new IllegalArgumentException("样式请输入 none A B both 其中一个");
    }
}

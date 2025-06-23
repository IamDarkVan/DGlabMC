package shirohaNya.dglabmc.enums;

import lombok.Getter;

@Getter
public enum AdjustMode {
    ADD(1,"ADD"),DEC(0,"DEC"),SET(2,"SET");

    private final int value;
    private final String text;
    AdjustMode(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public static AdjustMode toMode(String text) throws IllegalArgumentException{
        for (AdjustMode mode : values()) if (text.equalsIgnoreCase(mode.text)) return mode;
        throw new IllegalArgumentException("模式请输入 add dec set 其中一个");
    }
}

package shirohaNya.dglabmc.enums;

import lombok.Getter;

@Getter
public enum Channel {
    A(1, "A"),B(2, "B"),BOTH(3, "BOTH");

    private final int value;
    private final String text;

    Channel(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public static Channel toChannel(String text) throws IllegalArgumentException{
        for (Channel c : values()) if (text.equalsIgnoreCase(c.text)) return c;
        throw new IllegalArgumentException("频道请输入 A B both 其中一个");
    }
}

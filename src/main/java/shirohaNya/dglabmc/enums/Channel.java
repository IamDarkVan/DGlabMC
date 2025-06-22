package shirohaNya.dglabmc.enums;

public enum Channel {
    A,B,BOTH;

    public int toInt(){
        if (this == A) return 1;
        if (this == B) return 2;
        return 0;
    }

    public static Channel toChannel(String c) throws IllegalArgumentException{
        if (c.equalsIgnoreCase("A")) return A;
        if (c.equalsIgnoreCase("B")) return B;
        if (c.equalsIgnoreCase("BOTH")) return BOTH;
        throw new IllegalArgumentException("频道请输入 A B both 其中一个");
    }
}

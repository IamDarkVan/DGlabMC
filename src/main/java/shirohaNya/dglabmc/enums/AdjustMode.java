package shirohaNya.dglabmc.enums;

public enum AdjustMode {
    ADD,DEC,SET;

    public int toInt(){
        if (this == ADD) return 1;
        if (this == DEC) return 0;
        return 2;
    }

    public static AdjustMode toMode(String t) throws IllegalArgumentException{
        if (t.equalsIgnoreCase("ADD")) return ADD;
        if (t.equalsIgnoreCase("DEC")) return DEC;
        if (t.equalsIgnoreCase("SET")) return SET;
        throw new IllegalArgumentException("模式请输入 add dec set 其中一个");
    }
}

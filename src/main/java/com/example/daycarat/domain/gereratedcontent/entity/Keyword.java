package com.example.daycarat.domain.gereratedcontent.entity;

public enum Keyword {
    COMMUNICATION(1, "커뮤니케이션"),
    CONFLICT_RESOLUTION(2, "갈등 해결"),
    PASSION(3, "열정"),
    DILIGENCE(4, "성실"),
    COLLABORATION(5, "협업 능력"),
    LEADERSHIP(6, "리더십"),
    FEEDBACK(7, "피드백");

    private final int id;
    private final String value;

    Keyword(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static Keyword fromId(int id) {
        for (Keyword keyword : values()) {
            if (keyword.getId() == id) {
                return keyword;
            }
        }
        throw new IllegalArgumentException("Invalid Category ID");
    }
}

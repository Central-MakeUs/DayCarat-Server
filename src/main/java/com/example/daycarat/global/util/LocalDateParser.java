package com.example.daycarat.global.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateParser {

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}

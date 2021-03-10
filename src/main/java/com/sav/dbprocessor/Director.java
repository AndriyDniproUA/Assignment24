package com.sav.dbprocessor;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Director {
    Long id;
    String name;
    String surname;
    LocalDateTime birth_date;
}




package br.janioofi.financialcontrol.domain.enums;

import lombok.Getter;

@Getter
public enum Category {

    LEISURE(0, "Lazer"),
    STUDIES(1, "Estudos"),
    FOOD(2, "Alimentação"),
    HOUSING(3, "Moradia"),
    TRANSPORT(4, "Transporte"),
    TRIPS(5, "Viagens"),
    HEALTH(6, "Saúde"),
    PERSONAL_EXPENSES(7, "Despesas Pessoais"),
    CELLPHONE_TV_INTERNET(8, "Celular/TV/Internet");

    private final Integer code;
    private final String description;

    Category(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}

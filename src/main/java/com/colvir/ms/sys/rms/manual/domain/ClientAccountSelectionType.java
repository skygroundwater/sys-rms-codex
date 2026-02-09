package com.colvir.ms.sys.rms.manual.domain;

/** Способ выбора счетов / карт клиента */
public enum ClientAccountSelectionType {

    /** С указанных счетов / карт*/
    SPECIFIED,

    /** С любых счетов / карт */
    ANY,

    /** С любых счетов / карт, кроме указанных */
    EXCLUDING,

    /** Согласно правилу */
    BY_RULE;

}

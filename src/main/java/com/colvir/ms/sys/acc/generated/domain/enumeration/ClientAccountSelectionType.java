package com.colvir.ms.sys.acc.generated.domain.enumeration;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Способ выбора счетов / карт клиента
 */
public enum ClientAccountSelectionType {
    /**
     * С указанных счетов / карт
     */
    SPECIFIED,
    /**
     * С любых счетов / карт
     */
    ANY,
    /**
     * С любых счетов / карт, кроме указанных
     */
    EXCLUDING,
    /**
     * Согласно правилу
     */
    BY_RULE;

    public static String getValueName(ClientAccountSelectionType value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/ClientAccountSelectionType", locale);
        return bundle.getString(value.toString());
    }

    public static String getValueName(ClientAccountSelectionType value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueName(value, locale);
    }

    public static String getValueName(ClientAccountSelectionType value) {
        Locale locale = Locale.getDefault();
        return getValueName(value, locale);
    }


    public static String getValueData(ClientAccountSelectionType value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/ClientAccountSelectionType", locale);
        // к имени константы перечисления добавляем суффикс для получения записи хранящей значение поля 'data' в файле ресурсов ResourceBundle
        return bundle.getString(value.toString()+"$data");
    }

    public static String getValueData(ClientAccountSelectionType value) {
        Locale locale = Locale.getDefault();
        return getValueData(value, locale);
    }

    public static String getValueData(ClientAccountSelectionType value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueData(value, locale);
    }

}

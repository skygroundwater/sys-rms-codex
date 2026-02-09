package com.colvir.ms.sys.rms.generated.domain.enumeration;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Правило выбора счета
 */
public enum AccountSelectionRule {
    /**
     * Приоритет для счета в валюте требования
     */
    CURRENCY,
    /**
     * Строго в заданном порядке
     */
    ORDER,
    /**
     * Не определено
     */
    UNDEFINED;

    public static String getValueName(AccountSelectionRule value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/AccountSelectionRule", locale);
        return bundle.getString(value.toString());
    }

    public static String getValueName(AccountSelectionRule value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueName(value, locale);
    }

    public static String getValueName(AccountSelectionRule value) {
        Locale locale = Locale.getDefault();
        return getValueName(value, locale);
    }


    public static String getValueData(AccountSelectionRule value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/AccountSelectionRule", locale);
        // к имени константы перечисления добавляем суффикс для получения записи хранящей значение поля 'data' в файле ресурсов ResourceBundle
        return bundle.getString(value.toString()+"$data");
    }

    public static String getValueData(AccountSelectionRule value) {
        Locale locale = Locale.getDefault();
        return getValueData(value, locale);
    }

    public static String getValueData(AccountSelectionRule value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueData(value, locale);
    }

}

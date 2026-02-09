package com.colvir.ms.sys.rms.generated.domain.enumeration;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Правило оплаты в группе
 */
public enum GroupPaymentStrategy {
    /**
     * Условная оплата
     */
    CONDITIONAL,
    /**
     * Полная оплата
     */
    FULL,
    /**
     * Не применяется
     */
    INAPPLICABLE,
    /**
     * Пропорциональная оплата
     */
    PROPORTIONAL;

    public static String getValueName(GroupPaymentStrategy value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/GroupPaymentStrategy", locale);
        return bundle.getString(value.toString());
    }

    public static String getValueName(GroupPaymentStrategy value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueName(value, locale);
    }

    public static String getValueName(GroupPaymentStrategy value) {
        Locale locale = Locale.getDefault();
        return getValueName(value, locale);
    }


    public static String getValueData(GroupPaymentStrategy value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/GroupPaymentStrategy", locale);
        // к имени константы перечисления добавляем суффикс для получения записи хранящей значение поля 'data' в файле ресурсов ResourceBundle
        return bundle.getString(value.toString()+"$data");
    }

    public static String getValueData(GroupPaymentStrategy value) {
        Locale locale = Locale.getDefault();
        return getValueData(value, locale);
    }

    public static String getValueData(GroupPaymentStrategy value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueData(value, locale);
    }

}

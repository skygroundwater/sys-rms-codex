package com.colvir.ms.sys.rms.generated.domain.enumeration;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Состояние требования
 */
public enum RequirementStatus {
    /**
     * Аннулировано
     */
    CANCELED,
    /**
     * Не сформировано
     */
    NONEXISTENT,
    /**
     * Оплачено
     */
    PAID,
    /**
     * Приостановлено (блокировано)
     */
    SUSPENDED,
    /**
     * Ожидает оплаты
     */
    WAIT,
    /**
     * Списано
     */
    WRITTEN_OFF;

    public static String getValueName(RequirementStatus value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/RequirementStatus", locale);
        return bundle.getString(value.toString());
    }

    public static String getValueName(RequirementStatus value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueName(value, locale);
    }

    public static String getValueName(RequirementStatus value) {
        Locale locale = Locale.getDefault();
        return getValueName(value, locale);
    }


    public static String getValueData(RequirementStatus value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/RequirementStatus", locale);
        // к имени константы перечисления добавляем суффикс для получения записи хранящей значение поля 'data' в файле ресурсов ResourceBundle
        return bundle.getString(value.toString()+"$data");
    }

    public static String getValueData(RequirementStatus value) {
        Locale locale = Locale.getDefault();
        return getValueData(value, locale);
    }

    public static String getValueData(RequirementStatus value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueData(value, locale);
    }

}

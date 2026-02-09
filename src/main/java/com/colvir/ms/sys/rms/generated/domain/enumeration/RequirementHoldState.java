package com.colvir.ms.sys.rms.generated.domain.enumeration;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Статус холда по требованию
 */
public enum RequirementHoldState {
    /**
     * Создан
     */
    CREATED,
    /**
     * Изменен
     */
    UPDATED,
    /**
     * Удален
     */
    DELETED;

    public static String getValueName(RequirementHoldState value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/RequirementHoldState", locale);
        return bundle.getString(value.toString());
    }

    public static String getValueName(RequirementHoldState value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueName(value, locale);
    }

    public static String getValueName(RequirementHoldState value) {
        Locale locale = Locale.getDefault();
        return getValueName(value, locale);
    }


    public static String getValueData(RequirementHoldState value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/RequirementHoldState", locale);
        // к имени константы перечисления добавляем суффикс для получения записи хранящей значение поля 'data' в файле ресурсов ResourceBundle
        return bundle.getString(value.toString()+"$data");
    }

    public static String getValueData(RequirementHoldState value) {
        Locale locale = Locale.getDefault();
        return getValueData(value, locale);
    }

    public static String getValueData(RequirementHoldState value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueData(value, locale);
    }

}

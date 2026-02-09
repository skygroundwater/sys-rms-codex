package com.colvir.ms.sys.rms.generated.domain.enumeration;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Действие с требованием
 */
public enum RequirementAction {
    /**
     * Создать
     */
    CREATE,
    /**
     * Сохранить
     */
    SAVE,
    /**
     * Обновить
     */
    UPDATE,
    /**
     * Удалить
     */
    DELETE;

    public static String getValueName(RequirementAction value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/RequirementAction", locale);
        return bundle.getString(value.toString());
    }

    public static String getValueName(RequirementAction value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueName(value, locale);
    }

    public static String getValueName(RequirementAction value) {
        Locale locale = Locale.getDefault();
        return getValueName(value, locale);
    }


    public static String getValueData(RequirementAction value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/RequirementAction", locale);
        // к имени константы перечисления добавляем суффикс для получения записи хранящей значение поля 'data' в файле ресурсов ResourceBundle
        return bundle.getString(value.toString()+"$data");
    }

    public static String getValueData(RequirementAction value) {
        Locale locale = Locale.getDefault();
        return getValueData(value, locale);
    }

    public static String getValueData(RequirementAction value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueData(value, locale);
    }

}

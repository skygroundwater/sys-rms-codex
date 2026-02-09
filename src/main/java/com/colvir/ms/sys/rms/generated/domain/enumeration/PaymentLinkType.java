package com.colvir.ms.sys.rms.generated.domain.enumeration;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Вид связи с платежом
 */
public enum PaymentLinkType {
    /**
     * Cвязан автоматически
     */
    AUTO,
    /**
     * Cвязан вручную
     */
    MANUAL,
    /**
     * Cоздан при оплате требования
     */
    REQUIREMENT;

    public static String getValueName(PaymentLinkType value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/PaymentLinkType", locale);
        return bundle.getString(value.toString());
    }

    public static String getValueName(PaymentLinkType value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueName(value, locale);
    }

    public static String getValueName(PaymentLinkType value) {
        Locale locale = Locale.getDefault();
        return getValueName(value, locale);
    }


    public static String getValueData(PaymentLinkType value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/PaymentLinkType", locale);
        // к имени константы перечисления добавляем суффикс для получения записи хранящей значение поля 'data' в файле ресурсов ResourceBundle
        return bundle.getString(value.toString()+"$data");
    }

    public static String getValueData(PaymentLinkType value) {
        Locale locale = Locale.getDefault();
        return getValueData(value, locale);
    }

    public static String getValueData(PaymentLinkType value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueData(value, locale);
    }

}

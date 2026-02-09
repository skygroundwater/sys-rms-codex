package com.colvir.ms.sys.rms.generated.domain.enumeration;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Результат оплаты
 */
public enum PaymentResult {
    /**
     * Аннулирован
     */
    CANCELED,
    /**
     * Не оплачен
     */
    NOT_PAID,
    /**
     * Оплачен
     */
    PAID;

    public static String getValueName(PaymentResult value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/PaymentResult", locale);
        return bundle.getString(value.toString());
    }

    public static String getValueName(PaymentResult value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueName(value, locale);
    }

    public static String getValueName(PaymentResult value) {
        Locale locale = Locale.getDefault();
        return getValueName(value, locale);
    }


    public static String getValueData(PaymentResult value, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/enumeration/PaymentResult", locale);
        // к имени константы перечисления добавляем суффикс для получения записи хранящей значение поля 'data' в файле ресурсов ResourceBundle
        return bundle.getString(value.toString()+"$data");
    }

    public static String getValueData(PaymentResult value) {
        Locale locale = Locale.getDefault();
        return getValueData(value, locale);
    }

    public static String getValueData(PaymentResult value, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return getValueData(value, locale);
    }

}

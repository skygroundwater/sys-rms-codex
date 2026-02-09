package com.colvir.ms.sys.rms.manual.util;

public class RmsConstants {

    public static final String WAIT_PAY_STATUS = "wait_pay";
    public static final String PART_PAID_STATUS = "part_paid";
    public static final String PAID_STATUS = "paid";
    public static final String SUSPENDED_STATE = "suspended";
    public static final String WRITE_OFF_STATE = "revoked";
    public static final String CANCELLED_STATE = "cancelled";
    public static final String SYSTEM_LOCALE_PARAM = "SystemLocale";
    // системный параметр "Вид холда для требований"
    public static final String DEFAULT_HOLD_TYPE = "SYS.ACC.HoldTypeForRequirement";

    public static final String SYS_RMS_NAMESPACE = "/SYS/RMS";

    public static final String SYS_RMS_REQUIREMENT_NAMESPACE = "/SYS/RMS/Requirement";

    // события базового бизнес процесса
    // оплатить
    public static final String BBP_RUN_PAYMENT_EVENT = "/SYS/RMS/Paid";
    // отменить
    public static final String BBP_CANCEL_EVENT = "/SYS/RMS/Cancelled";
    // блокировать
    public static final String BBP_SUSPEND_EVENT = "/SYS/RMS/Suspended";
    // списать
    public static final String BBP_WRITEOFF_EVENT = "/SYS/RMS/WrittenOff";
    // возобновить
    public static final String BBP_RENEW_EVENT = "/SYS/RMS/Resumed";
    // вернуть
    public static final String BBP_RUN_REFUND_EVENT = "/SYS/RMS/Refunded";

    public static final String UPDATE_BASE_BUSINESS_PROCESS_RESULT_PATH = "__step_sysBbpPack_update_";
    public static final String START_BASE_BUSINESS_PROCESS_STATE_FIELD_PREFIX = "__step_initialBbpState_";
    public static final String MAIN_CONTEXT_NODE = "main";
    public static final String HOLD_RESULT_PATH = "__step_holdResult_";
    public static final String SYS_ACC_WITHDRAWAL_TYPE = "/SYS/ACC/WithdrawalType";


}

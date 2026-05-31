package com.colvir.ms.sys.rms.algorithm.std;

import com.colvir.ms.common.alg.extension.api.RemoteFunction;
import com.colvir.ms.common.alg.extension.api.StandardFunction;
import com.colvir.ms.common.alg.extension.api.dto.FunctionDTO;
import com.colvir.ms.common.alg.extension.api.dto.enumeration.FunctionGroup;
import com.colvir.ms.common.alg.extension.api.dto.enumeration.ParameterType;
import com.colvir.ms.common.alg.extension.api.runtime.FunctionCallContext;
import com.colvir.ms.common.alg.extension.api.runtime.function.parameter.ParameterSingle;
import com.colvir.ms.common.alg.extension.runtime.exceptions.AlgorithmCalculationProcessException;
import com.colvir.ms.common.sys.prod.runtime.algorithm.AlgorithmUtils;
import com.colvir.ms.sys.rms.manual.service.PaymentOwMassReportService;
import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.arc.impl.LazyValue;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.inject.spi.CDI;

import java.time.LocalDate;
import java.util.Objects;

import static com.colvir.ms.common.sys.prod.runtime.util.Constants.DATATYPE_LOCAL_DATE;

@RemoteFunction
public class PaymentOwMassReport extends StandardFunction {

    private static final String DATE = "date";
    private static final String PAYMENT_OW_MASS_REPORT_CODE = "payment_ow_mass_report";
    private static final String PAYMENT_OW_MASS_REPORT_DESCRIPTION = "Payment Ow Mass Report";

    private final LazyValue<PaymentOwMassReportService> paymentOwMassReportService = new LazyValue<>(() -> CDI.current().select(PaymentOwMassReportService.class).get());

    public PaymentOwMassReport() {
        stdFunctionInfo = new FunctionDTO()
            .setCode(PAYMENT_OW_MASS_REPORT_CODE)
            .setDescription(PAYMENT_OW_MASS_REPORT_DESCRIPTION)
            .addFunctionGroup(FunctionGroup.PRODUCTS)
            .addParameter(DATE, "The report date", ParameterType.STRING, false, null, DATATYPE_LOCAL_DATE);
    }

    @Override
    public Uni<Object> calculate(FunctionCallContext ctx) {
        ParameterSingle<JsonNode> pDate = ctx.getParameter(DATE).asSingle().unwrap();
        return ctx.calculateParameters(pDate)
            .flatMap(data -> {
                LocalDate date;
                try {
                    date = AlgorithmUtils.convertParameterAsLocalDate(pDate.getValue().textValue());
                } catch (AlgorithmCalculationProcessException e) {
                    throw new RuntimeException(e);
                }
                if (Objects.isNull(data)) {
                    throw new IllegalArgumentException(String.format("Date for Payment Ow Mass is required. %s", pDate));
                }
                return Uni.createFrom().item(() -> paymentOwMassReportService.get().getPaymentOwMassData(date))
                    .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
            });
    }

}

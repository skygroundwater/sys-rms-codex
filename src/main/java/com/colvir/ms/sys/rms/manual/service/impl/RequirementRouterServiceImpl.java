package com.colvir.ms.sys.rms.manual.service.impl;

import com.colvir.ms.common.router.DDCRouterUtil;
import com.colvir.ms.common.router.dto.DDCModifyRequest;
import com.colvir.ms.common.router.dto.DDCReadRequest;
import com.colvir.ms.sys.rms.dto.CreateRequirementDto;
import com.colvir.ms.sys.rms.dto.RequirementIndicatorDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementTypeDTO;
import com.colvir.ms.sys.rms.manual.service.RequirementRouterService;
import com.colvir.ms.sys.rms.manual.service.RequirementTypeService;
import com.colvir.ms.sys.rms.manual.service.RouterService;
import com.colvir.ms.sys.rms.manual.util.CacheInvalidateServiceImpl;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.colvir.ms.sys.rms.manual.util.RmsConstants;
import com.colvir.ms.sys.rms.manual.util.SystemParameterService;
import com.colvir.ms.sys.rms.manual.web.dto.BaseProcessResultDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.colvir.ms.sys.rms.manual.service.impl.RequirementServiceImpl.LOCAL_DATE_FORMATTER;

@ApplicationScoped
public class RequirementRouterServiceImpl implements RequirementRouterService {
    private static final String REQUIREMENT = "Requirement";
    @ConfigProperty(name = "application.domain", defaultValue = "/SYS/RMS")
    String applicationDomain;

    private final RequirementTypeService requirementTypeService;
    private final RouterService routerService;
    private final ObjectMapper objectMapper;
    private final SystemParameterService systemParameterService;
    private final Logger log;

    public RequirementRouterServiceImpl(RequirementTypeService requirementTypeService, RouterService routerService,
                                        ObjectMapper objectMapper, SystemParameterService systemParameterService,
                                        Logger log) {
        this.requirementTypeService = requirementTypeService;
        this.routerService = routerService;
        this.objectMapper = objectMapper;
        this.systemParameterService = systemParameterService;
        this.log = log;
    }

    @Override
    public Long createRequirement(CreateRequirementDto createRequirementDto) {
        return this.createRequirements(List.of(createRequirementDto))
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("The router response does not contain correct result"));
    }

    @Override
    public List<Long> createRequirements(Collection<CreateRequirementDto> createRequirementDtos) {
        List<DDCModifyRequest> requests = createRequirementDtos
            .stream()
            .map(this::createRequestBody)
            .map(body -> DDCRouterUtil.createDDCCreateRequest(applicationDomain, REQUIREMENT, body))
            .toList();
        log.debugf("createRequirements request: %s", requests);
        return routerService.modify(requests)
            .stream()
            .map(routerService::extractIdFromResult)
            .toList();
    }

    @Override
    @CacheResult(cacheName = CacheInvalidateServiceImpl.REQUIREMENT_INDICATOR_CACHE)
    public RequirementIndicatorDto getRequirementIndicator(Long indicatorId) {
        if (indicatorId == null) {
            throw new RuntimeException("Indicator id is null");
        }
        Map<String, Object> readRequestQuery = new HashMap<>();
        readRequestQuery.put("id", Map.of("equals", indicatorId.toString()));
        DDCReadRequest readRequest = DDCRouterUtil.createDDCReadRequest(
            "/SYS/PROD",
            "Indicator",
            new ArrayList<>(),
            readRequestQuery
        );
        List<ObjectNode> indicatorsList = routerService.read(readRequest);
        if (indicatorsList.isEmpty()) {
            throw new RuntimeException(String.format("Indicator with id=%s not found", indicatorId));
        }
        return objectMapper.convertValue(indicatorsList.getFirst(), RequirementIndicatorDto.class);
    }

    @Override
    public ObjectNode deleteRequirement(Long id) {
        return Optional.ofNullable(id)
            .map(i -> deleteRequirements(List.of(i)))
            .flatMap(l -> l.stream().findFirst())
            .orElse(null);
    }

    @Override
    public List<ObjectNode> deleteRequirements(Collection<Long> ids) {
        List<DDCModifyRequest> requests = Optional.ofNullable(ids)
            .stream()
            .flatMap(Collection::stream)
            .map(id -> Requirement.<Requirement>findById(id))
            .filter(Objects::nonNull)
            .filter(requirement -> BooleanUtils.isNotTrue(requirement.isDeleted))
            .map(requirement -> {
                final ObjectNode attributes = objectMapper.createObjectNode();
                attributes.put("id", requirement.id);
                attributes.put("version", requirement.version);
                return DDCRouterUtil.createDDCDeleteRequest(applicationDomain, REQUIREMENT, attributes);
            }).toList();
        log.debugf("deleteRequirements request: %s", requests);
        return routerService.modify(requests);
    }

    private ObjectNode createRequestBody(CreateRequirementDto createRequirementDto) {
        Long systemLocale = systemParameterService.getSystemLocale(RmsConstants.SYSTEM_LOCALE_PARAM);
        RequirementStateInfoDto paymentData = createRequirementDto.getPaymentData();
        if (systemLocale == null) {
            throw new RuntimeException("SystemLocale parameter is not define");
        }

        String initialBbpState = createRequirementDto.getInitialBbpState();

        log.infof("initialBbpState : " + initialBbpState);
        // вид требования вычисляется с помощью настроенного алгоритма
        // который определяет его в зависимости от "расчетной категории" из записи массива
        RequirementTypeDTO requirementType = requirementTypeService.getRequirementType(paymentData.indicator.indicatorDescr, systemLocale);

        // приоритет - целая часть = приоритету из вида требования, дробная часть = "приоритет оплаты" из записи массива
        BigDecimal integerPriority = new BigDecimal(requirementType.priority);
        BigDecimal decimalPriority = new BigDecimal(paymentData.priority).divide(new BigDecimal("100"), 2, RoundingMode.UP);
        BigDecimal priority = integerPriority.add(decimalPriority);


        ObjectNode body = objectMapper.createObjectNode();
        // идентификатор требования сформировали заранее и передали как готовый, но это не обязательно
        if (paymentData.requirementId != null) {
            body.put("id", paymentData.requirementId);
        }

        BaseProcessResultDto bbpState;
        try {
            bbpState = ContextObjectMapper.get().readValue(initialBbpState, BaseProcessResultDto.class);

            if (bbpState.stateCode == null || bbpState.stateCode.isBlank()) {
                JsonNode root = ContextObjectMapper.get().readTree(initialBbpState);
                String state = root.path("state").asText("wait_pay");
                body.put("bbpState000StateCode", state);
            } else {
                body.put("bbpState000StateCode", bbpState.stateCode);
            }
            body.put("bbpState000ProcessId", bbpState.processId);
            body.put("bbpState000JournalId", String.valueOf(bbpState.journalId));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Не удалось прочесть состояние бизнес процесса. bbState=%s для класса ", initialBbpState));
        }

        // состояние = "ожидает оплаты"
        body.put("state", RequirementStatus.WAIT.toString());
        // неоплаченная сумма = сумма
        body.put("unpaidAmount", paymentData.amount);
        // оплаченная сумма = 0
        body.put("paidAmount", BigDecimal.ZERO);
        // списанная сумма = 0
        body.put("writeOffAmount", BigDecimal.ZERO);
        // сумма = "сумма к оплате" из записи массива
        body.put("amount", paymentData.amount);
        // валюта = валюта договора
        body.set("currency", objectMapper.createObjectNode()
            .put("id", createRequirementDto.getCurrency().id)
            .put("__objectType", "/SYS/CUR/Currency"));
        // расчетная категория = "расчетная категория" из записи массива
        body.set("indicator", objectMapper.createObjectNode().put("id", paymentData.indicator.id));
        // дата = параметр "дата операционного дня"
        body.put("date", createRequirementDto.getBusinessDate().format(LOCAL_DATE_FORMATTER));
        // дата начала оплаты= параметр "дата операционного дня"
        body.put("startPaymentDate", createRequirementDto.getBusinessDate().format(LOCAL_DATE_FORMATTER));
        // дата окончания оплаты= параметр "дата операционного дня"
        body.put("paymentEndDate", createRequirementDto.getBusinessDate().format(LOCAL_DATE_FORMATTER));
        // связано с договором = true
        body.put("isContractBound", true);
        // документ-основание = параметр "ссылка на договор"
        body.set("baseDocument", objectMapper.createObjectNode()
            .put("id", createRequirementDto.getContract().id)
            .put("__objectType", createRequirementDto.getContract().__objectType));
        // плательщик = клиент
        body.set("client", objectMapper.createObjectNode()
            .put("id", createRequirementDto.getClient().id)
            .put("__objectType", createRequirementDto.getClient().__objectType));
        // вид требования
        body.set("requirementType", objectMapper.createObjectNode().put("id", requirementType.id));
        // приоритет
        body.put("priority", priority);

        ObjectNode paymentStrategy = objectMapper.createObjectNode();
        // разрешена оплата по частям
        paymentStrategy.put("allowAutoPay", requirementType.isPartialPayable);
        // TODO ! у вида требования нет атрибута: разрешена оплата группой
        // paymentStrategy.put("allowGroupPay", );
        // разрешено использование заемных средств
        paymentStrategy.put("allowOverdraft", requirementType.useLoanFunds);
        body.set("paymentStrategy", paymentStrategy);
        return body;
    }
}

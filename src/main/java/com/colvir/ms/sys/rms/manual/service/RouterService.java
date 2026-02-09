package com.colvir.ms.sys.rms.manual.service;

import com.colvir.ms.common.router.dto.DDCModifyRequest;
import com.colvir.ms.common.router.dto.DDCReadRequest;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public interface RouterService {

    ObjectNode modify(String namespace, String entityName, ObjectNode body);

    List<ObjectNode> modify(DDCModifyRequest modifyRequest);
    List<ObjectNode> modify(List<DDCModifyRequest> modifyRequest);

    List<ObjectNode> read(DDCReadRequest readRequest);

    Long extractIdFromResult(ObjectNode result);
}

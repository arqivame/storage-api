package com.arqivame.storage.application.policy;

import com.arqivame.storage.domain.file.Session;

@FunctionalInterface
public interface ChunkUploadConcurrencyPolicy {

    Boolean isAllowed(Session session);

}

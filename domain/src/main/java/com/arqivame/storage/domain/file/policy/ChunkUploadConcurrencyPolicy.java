package com.arqivame.storage.domain.file.policy;

import com.arqivame.storage.domain.file.Session;

@FunctionalInterface
public interface ChunkUploadConcurrencyPolicy {

    Boolean isAllowed(Session session);

}

package com.arqivame.storage.domain.file;

import java.util.Optional;

public interface FileGateway {

    Optional<File> findById(FileID id);

    File create(File file);

    File update(File file);

}

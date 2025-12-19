package com.arqivame.storage.application.file.session.create;

import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;

public class DefaultCreateUploadSessionUseCase extends CreateUploadSessionUseCase {

    // private final FileGateway fileGateway;

    @Override
    public CreateUploadSessionOutput execute(final CreateUploadSessionInput input) {

        final FileID fileId = FileID.of(input.fileId());
        // fileGateway.findById(fileId)
        //         .orElseGet(null);

        return null;

    }

}

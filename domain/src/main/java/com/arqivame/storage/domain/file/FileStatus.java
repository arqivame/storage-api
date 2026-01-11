package com.arqivame.storage.domain.file;

public enum FileStatus {
    NEW,
    UPLOADING,
    UPLOAD_COMPLETED,
    UPLOAD_ABORTED,
    PROCESSING,
    AVAILABLE,
    FAILED;
}

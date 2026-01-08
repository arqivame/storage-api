package com.arqivame.storage.domain.exception;

import java.util.Set;

import com.arqivame.storage.domain.file.Chunk;

public class MergeChunksException extends SilentDomainException {

    private static final String MESSAGE = "An error occurred while merging file chunks";

    private MergeChunksException() {
        super(MESSAGE);
    }

    public static MergeChunksException create() {
        return new MergeChunksException();
    }

    public static MergeChunksException create(Set<Chunk> nonMergedChunks) {
        return new MergeChunksException();
    }

}

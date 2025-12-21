package com.arqivame.storage.domain.file.service;

import java.io.InputStream;
import java.util.Objects;

import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.ChunkID;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSessionID;

@FunctionalInterface
public interface StorageService {

    Checksum write(
            StorageKey key,
            InputStream inputStream,
            Long sizeInBytes,
            Long bytesPerSecondsWrittenRate,
            Checksum.Algorithm checksumAlgorithm);

    public static final class StorageKey {

        private final String[] segments;

        private StorageKey(final String... segments) {

            if (Objects.isNull(segments) || segments.length == 0)
                throw new IllegalArgumentException("Segments cannot be null or empty");

            this.segments = segments;
        }

        public static StorageKey from(
                final FileID file,
                final UploadSessionID session,
                final ChunkID chunk,
                final Long chunkIndex) {

            return new StorageKey(
                    new String[] {
                            "files",
                            file.getStringValue(),
                            "uploads",
                            session.getStringValue(),
                            "chunks",
                            chunkIndex.toString()
                    });
        }

        public String getFullKey() {
            return String.join("/", segments);
        }

        public String lastSegment() {
            return segments[segments.length - 1];
        }

    }

}

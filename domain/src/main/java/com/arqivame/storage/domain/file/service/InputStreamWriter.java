package com.arqivame.storage.domain.file.service;

import java.io.InputStream;

@FunctionalInterface
public interface InputStreamWriter {

    void write(InputStream inputStream, Long bitsPerSecondsWrittenRate);

}

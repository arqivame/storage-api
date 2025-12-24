package com.arqivame.storage.domain.file.service;

public interface StorageService extends StorageWriter {

    void delete(StorageKey key);

}

package com.arqivame.storage.domain.file.service;

@FunctionalInterface
public interface StorageDeleter {

    void delete(StorageKey key);

}

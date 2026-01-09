package com.arqivame.storage.application.service.storage;

@FunctionalInterface
public interface StorageDeleter {

    void delete(StorageKey key);

}

package com.arqivame.storage.infrastructure.storage.service;

@FunctionalInterface
public interface StorageDeleter {

    void delete(StorageKey key);

}

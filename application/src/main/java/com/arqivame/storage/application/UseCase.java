package com.arqivame.storage.application;

public abstract class UseCase<I, O> {

    public abstract O execute(I input);

}

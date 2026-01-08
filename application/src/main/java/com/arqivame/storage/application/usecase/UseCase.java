package com.arqivame.storage.application.usecase;

public abstract class UseCase<I, O> {

    public abstract O execute(I input);

}

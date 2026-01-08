package com.arqivame.storage.infrastructure.file.model;

import java.io.Serializable;
import java.util.UUID;

public record FileAssembledMessage(UUID fileId) implements Serializable {

}

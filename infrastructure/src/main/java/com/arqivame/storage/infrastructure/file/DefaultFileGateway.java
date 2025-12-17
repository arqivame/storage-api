package com.arqivame.storage.infrastructure.file;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.arqivame.storage.domain.file.File;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;

@Component
public class DefaultFileGateway implements FileGateway {

    @Override
    public Optional<File> findById(FileID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public File save(File file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

}

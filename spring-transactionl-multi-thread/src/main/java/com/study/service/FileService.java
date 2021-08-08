package com.study.service;

import com.study.domain.File;
import com.study.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Transactional
    public void increaseInQuery(long idx, int repetitionCount) {
        for (int i = 0; i < repetitionCount; i++) {
            fileRepository.increaseCountInQuery(idx);
        }
    }

    @Transactional
    public void increaseCountInCode(long idx, int repetitionCount) {
        for (int i = 0; i < repetitionCount; i++) {
            File file = fileRepository.findById(idx);
            file.increaseCount();
            fileRepository.update(file);
        }
    }

    @Transactional
    public void increaseInCodeWithSelectForUpdate(long idx, int repetitionCount) {
        for (int i = 0; i < repetitionCount; i++) {
            File file = fileRepository.findByIdWithSelectForUpdate(idx);
            file.increaseCount();
            int row = fileRepository.update(file);
        }
    }

    @Transactional
    public synchronized void increaseInCodeSync(long idx, int repetitionCount) {
        for (int i = 0; i < repetitionCount; i++) {
            File file = fileRepository.findById(idx);
            file.increaseCount();
            int row = fileRepository.update(file);
        }
    }
}

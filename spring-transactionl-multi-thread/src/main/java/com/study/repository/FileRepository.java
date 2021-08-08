package com.study.repository;

import com.study.domain.File;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository {

    File findById(long idx);

    int update(File file);

    void increaseCountInQuery(long idx);

    File findByIdWithSelectForUpdate(long idx);

    void init();
}

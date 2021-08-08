package com.study.service;

import com.study.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback(value = false)
//@Transactional(isolation = Isolation.READ_COMMITTED)
@SpringBootTest
class FileServiceTest {

    @Autowired
    FileService fileService;

    @Autowired
    FileRepository fileRepository;

    private static final long IDX = 0;
    private static final int NUMBER_OF_THREADS = 100;
    private static final int REPETITION_COUNT = 100;

    private ExecutorService service = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private CountDownLatch latch = null;

    @BeforeEach
    public void initDb() {
        fileRepository.init();
        service = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        latch = new CountDownLatch(NUMBER_OF_THREADS);
    }

    @Test
    public void increaseCountInQuery() throws Exception {
        executorServiceIteration(NUMBER_OF_THREADS, () -> {
            fileService.increaseInQuery(IDX, REPETITION_COUNT);
            latch.countDown();
        });
        latch.await();
        assertThat(fileRepository.findById(IDX).getCount())
                .isEqualTo(NUMBER_OF_THREADS * REPETITION_COUNT);
    }

    @Test
    public void increaseInCode() throws Exception {
        executorServiceIteration(NUMBER_OF_THREADS , () -> {
            latch.countDown();
            fileService.increaseCountInCode(IDX, REPETITION_COUNT);
        });
        latch.await();
        assertThat(fileRepository.findById(IDX).getCount())
                .isEqualTo(NUMBER_OF_THREADS * REPETITION_COUNT);
    }

    @Test
    public void increaseInCodeWithSelectForUpdate() throws Exception {
        executorServiceIteration(NUMBER_OF_THREADS , () -> {
            fileService.increaseInCodeWithSelectForUpdate(IDX, REPETITION_COUNT);
            latch.countDown();
        });
        latch.await();
        assertThat(fileRepository.findById(IDX).getCount())
                .isEqualTo(NUMBER_OF_THREADS * REPETITION_COUNT);
    }

    @Test
    public void increaseInCodeSync() throws Exception {
        executorServiceIteration(NUMBER_OF_THREADS , () -> {
            fileService.increaseInCodeSync(IDX, REPETITION_COUNT);
            latch.countDown();
        });
        latch.await();
        assertThat(fileRepository.findById(IDX).getCount()).isEqualTo(NUMBER_OF_THREADS * REPETITION_COUNT);
    }


    private void executorServiceIteration(int iterCount, Runnable runnable) {
        for (int i = 0; i < iterCount; i++) {
            service.execute(runnable);
        }
    }
}
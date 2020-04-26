package demo.test.Async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
@Slf4j
public class AsyncTask {

    @Async
    public Future<String> task1() throws InterruptedException {
        long currentTimeMillis1 = System.currentTimeMillis();
        Thread.sleep(1000);
        long currentTimeMillis2 = System.currentTimeMillis();
        log.info("task1任务耗时:{} ms",currentTimeMillis2-currentTimeMillis1);
        return new AsyncResult<String>("task1执行完毕");
    }

    @Async
    public Future<String> task2() throws InterruptedException {
        long currentTimeMillis1 = System.currentTimeMillis();
        Thread.sleep(2000);
        long currentTimeMillis2 = System.currentTimeMillis();
        log.info("task2任务耗时:{} ms",currentTimeMillis2-currentTimeMillis1);
        return new AsyncResult<String>("task2执行完毕");
    }

    @Async
    public Future<String> task3() throws InterruptedException {
        long currentTimeMillis1 = System.currentTimeMillis();
        Thread.sleep(3000);
        long currentTimeMillis2 = System.currentTimeMillis();
        log.info("task3任务耗时:{} ms",currentTimeMillis2-currentTimeMillis1);
        return new AsyncResult<String>("task4执行完毕");
    }


}

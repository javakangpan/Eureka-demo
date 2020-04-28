package demo.controller;

import demo.test.Async.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@RequestMapping("taskTest")
@RestController
public class AsyncTaskController {
    @Autowired
    private AsyncTask asyncTask;

    @RequestMapping("")
    public String doTask() throws InterruptedException {
        long time1 = System.currentTimeMillis();
        Future<String> task1 = asyncTask.task1();
        Future<String> task2= asyncTask.task2();
        Future<String> task3 = asyncTask.task3();
        for(;;) {
            if(task1.isDone() && task2.isDone() && task3.isDone()) {
                break;
            }
            Thread.sleep(1000);
        }
        return "task任务总耗时:" + (System.currentTimeMillis() - time1) + "ms";
    }
}

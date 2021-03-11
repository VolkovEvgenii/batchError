package ru.volkov.batch.common.processors;

import org.springframework.batch.item.ItemProcessor;
import ru.volkov.batch.common.exceptions.CustomRetryableException;

public class RetryItemProcessor implements ItemProcessor<String, String> {

    private boolean retry = false;
    private int attemptCount = 0;

    @Override
    public String process(String s) throws Exception {
        System.out.println("Processing of item - '{" + s + "}'");
        if(retry && "42".equals(s)) {
            attemptCount++;

            if(attemptCount >= 5) {
                System.out.println("Success!");
                retry = false;
                return String.valueOf(Integer.valueOf(s) * -1);
            } else {
                System.out.println("Processing of item - '{" + s + "}'" + " failed");
                throw new CustomRetryableException("Process failed. Attempt - '{" + attemptCount + "}'");
            }
        } else {
            return String.valueOf(Integer.valueOf(s) * -1);
        }
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }
}

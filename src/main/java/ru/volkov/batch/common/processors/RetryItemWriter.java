package ru.volkov.batch.common.processors;

import org.springframework.batch.item.ItemWriter;
import ru.volkov.batch.common.exceptions.CustomRetryableException;

import java.util.List;

public class RetryItemWriter implements ItemWriter<String> {

    private boolean retry = false;
    private int attemptCount = 0;

    @Override
    public void write(List<? extends String> list) throws Exception {

        for (String s : list) {
            System.out.println("Writing item - '{" + s + "}'");
            if(retry && "-84".equals(s)) {
                attemptCount++;

                if(attemptCount >= 5) {
                    System.out.println("Success!");
                    retry = false;
                    System.out.println(s);
                } else {
                    System.out.println("Writing of item - '{" + s + "}'" + " failed");
                    throw new CustomRetryableException("Write failed. Attempt - '{" + attemptCount + "}'");
                }
            } else {
                System.out.println(s);
            }
        }
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }
}

package ru.volkov.batch.retry;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import ru.volkov.batch.common.exceptions.CustomRetryableException;
import ru.volkov.batch.common.processors.RetryItemProcessor;
import ru.volkov.batch.common.processors.RetryItemWriter;

@Configuration
public class RetryJobBuilder {

    private StepBuilderFactory stepBuilderFactory;
    private JobBuilderFactory jobBuilderFactory;
    private ItemReader incrementedItemReader;

    public RetryJobBuilder(
            StepBuilderFactory stepBuilderFactory,
            JobBuilderFactory jobBuilderFactory,
            @Qualifier ("incrementedItemReader") ItemReader incrementedItemReader) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobBuilderFactory = jobBuilderFactory;
        this.incrementedItemReader = incrementedItemReader;
    }

    @Bean
    @StepScope
    public RetryItemProcessor processor(@Value("#{jobParameters['retry']}") String retry) {
        RetryItemProcessor processor = new RetryItemProcessor();
        processor.setRetry(StringUtils.hasText(retry) && "processor".equals(retry));
        return processor;
    }

    @Bean
    @StepScope
    public RetryItemWriter writer(@Value("#{jobParameters['retry']}") String retry) {
        RetryItemWriter writer = new RetryItemWriter();
        writer.setRetry(StringUtils.hasText(retry) && "writer".equals(retry));
        return writer;
    }

    @Bean
    public Step retryStep() {
        return stepBuilderFactory.get("retryStep")
                .<String, String>chunk(10)
                .reader(incrementedItemReader)
                .processor(processor(null))
                .writer(writer(null))
                .faultTolerant()
                .retry(CustomRetryableException.class)
                .retryLimit(15)
                .build();
    }

    @Bean
    public Job retryJob(){
        return jobBuilderFactory.get("retryJob")
                .start(retryStep())
                .build();
    }
}

package ru.volkov.batch.common.readers;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class IncrementedItemReaderConfiguration {

    @Bean
    @StepScope
    @Qualifier("incrementedItemReader")
    public ListItemReader incrementedItemReader() {
        List<String> stringList = new ArrayList();
        for (int i = 0; i <= 100; i++) {
            stringList.add(String.valueOf(i));
        }

        ListItemReader<String> reader = new ListItemReader<>(stringList);

        return reader;
    }
}

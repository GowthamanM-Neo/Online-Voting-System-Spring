package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.example.demo.dao.UserDao;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

 

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, 
            StepBuilderFactory stepBuilderFactory, 
            ItemReader<UserDao> itemReader, ItemProcessor<UserDao, UserDao> itemProcessor,
            ItemWriter<UserDao> itemWriter) {
        
        Step step =stepBuilderFactory.get("ETL-file-Load")
                .<UserDao, UserDao>chunk(500)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
 
        Job job =jobBuilderFactory.get("ETL-Load")
        .incrementer(new RunIdIncrementer())
//        .flow(step)
//        .next(step)
//        .next(step)
//        .build();
        .start(step)
        .build();
        
        
        return job;
    }
    
    @Bean
    public FlatFileItemReader<UserDao> itemReader() {
        FlatFileItemReader<UserDao> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/data.csv"));
        flatFileItemReader.setName("CSV-reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }
    
    
    private LineMapper<UserDao> lineMapper() {
        DefaultLineMapper<UserDao> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        
        lineTokenizer.setNames(new String[] {"email", "password", "roles", "active", "name", "phone", "department", "salary"});
        
        BeanWrapperFieldSetMapper<UserDao> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(UserDao.class);
        
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        
        return defaultLineMapper;
    }
}
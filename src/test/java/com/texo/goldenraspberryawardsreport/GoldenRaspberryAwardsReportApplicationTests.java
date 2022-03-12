package com.texo.goldenraspberryawardsreport;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.texo.goldenraspberryawardsreport.dto.MovieDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

@SpringBootTest
class GoldenRaspberryAwardsReportApplicationTests {

    @Autowired
    private Environment env;

    @Test
    void testReadingAndConversationCsv() throws IOException{
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + env.getProperty("path.file.csv"));
        Reader reader = new FileReader(file);

        CsvToBean<MovieDto> csvToBean = new CsvToBeanBuilder(reader)
                .withSeparator(';')
                .withType(MovieDto.class)
                .build();

        Assertions.assertNotNull(csvToBean.parse());
    }

}

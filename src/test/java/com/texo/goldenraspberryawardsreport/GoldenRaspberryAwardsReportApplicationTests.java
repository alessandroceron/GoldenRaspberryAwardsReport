package com.texo.goldenraspberryawardsreport;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.texo.goldenraspberryawardsreport.dto.MovieDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

@SpringBootTest
class GoldenRaspberryAwardsReportApplicationTests {

    @Test
    void testeLeituraCsv() throws IOException{
        File file = ResourceUtils.getFile("classpath:data/movielist.csv");
        Reader reader = new FileReader(file);

        CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                .withSeparator(';')
                .withType(MovieDto.class)
                .build();

        List movies = csvToBean.parse();
        Assertions.assertNotNull(movies);
    }

}

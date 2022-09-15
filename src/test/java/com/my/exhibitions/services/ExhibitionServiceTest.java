package com.my.exhibitions.services;

import com.my.exhibitions.entities.Exhibition;
import com.my.exhibitions.repositories.ExhibitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ExhibitionServiceTest {
    @MockBean
    ExhibitionRepository exhibitionRepository;
    @InjectMocks
    ExhibitionService exhibitionService;

    Exhibition exhibition;

    @Autowired
    public ExhibitionServiceTest(ExhibitionService exhibitionService) {
        this.exhibitionService = exhibitionService;
    }

    @BeforeEach
    public void setUp() {
        exhibition = new Exhibition();
        exhibition.setId(1000L);
        exhibition.setTheme("themE");
    }


}

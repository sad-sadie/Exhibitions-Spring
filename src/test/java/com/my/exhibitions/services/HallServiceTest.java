package com.my.exhibitions.services;

import com.my.exhibitions.entities.Hall;
import com.my.exhibitions.repositories.HallRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class HallServiceTest {

    @MockBean
    HallRepository hallRepository;
    @InjectMocks
    HallService hallService;

    Hall hall;

    @Autowired
    public HallServiceTest(HallService hallService) {
        this.hallService = hallService;
    }

    @BeforeEach
    public void setUp() {
        hall = new Hall();
        hall.setId(10000L);
        hall.setName("hallN");
        hall.setDescription("hallDes");
    }


    @Test
    public void testSave() {
        hallService.save(hall);

        verify(hallRepository).save(hall);
    }

    @Test
    public void testFindById() {
        when(hallService.findById(10000L)).thenReturn(hall);

        Hall hallFound = hallService.findById(10000L);

        verify(hallRepository).findById(anyLong());
        assertThat(hallFound).isEqualTo(hall);
    }

    @Test
    public void testFindByIdNull() {
        when(hallService.findById(10000L)).thenReturn(null);

        Hall hallFound = hallService.findById(10000L);

        verify(hallRepository).findById(anyLong());
        assertThat(hallFound).isNull();
    }

    @Test
    public void testFindAll() {
        Hall hall1 = new Hall();
        when(hallRepository.findAll()).thenReturn(List.of(hall, hall1));

        List<Hall> halls = hallService.findAll();

        verify(hallRepository).findAll();
        assertThat(halls).isEqualTo(List.of(hall, hall1));

    }

    @Test
    public void testFindAllNull() {
        when(hallRepository.findAll()).thenReturn(Collections.emptyList());

        List<Hall> halls = hallService.findAll();

        verify(hallRepository).findAll();
        assertThat(halls).isEqualTo(Collections.emptyList());

    }

    @Test
    public void testExistsByName() {
        when(hallRepository.existsByName("hallN")).thenReturn(true);

        boolean exists = hallService.existsByName("hallN");

        verify(hallRepository).existsByName("hallN");
        assertThat(exists).isTrue();
    }

    @Test
    public void testGetPage() {
        Pageable paging = PageRequest.of(1, 5);

        hallService.getPage(1);

        verify(hallRepository).findAll(paging);
    }
}

package com.my.exhibitions.services;

import com.my.exhibitions.entities.Hall;
import com.my.exhibitions.repositories.HallRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallService {

    private final static Logger LOGGER = Logger.getLogger(HallService.class);
    private static final int DEFAULT_PAGE_SIZE = 5;
    private final HallRepository hallRepository;

    @Autowired
    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public void save(Hall hall) {
        LOGGER.info("Saving hall " + hall);
        hallRepository.save(hall);
    }

    public List<Hall> findAll() {
        LOGGER.info("Getting all halls");
        return hallRepository.findAll();
    }

    public Hall findById(long id) {
        LOGGER.info("Getting hall by id " + id);
        return hallRepository.findById(id);
    }

    public Page<Hall> getPage(int pageNum) {
        LOGGER.info("Getting halls on page at number: " + pageNum);
        Pageable paging = PageRequest.of(pageNum, DEFAULT_PAGE_SIZE);
        return hallRepository.findAll(paging);
    }

    public boolean existsByName(String name) {
        LOGGER.info("Checking if hall " + name + " exists");
        return hallRepository.existsByName(name);
    }
}

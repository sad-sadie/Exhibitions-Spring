package com.my.exhibitions.services;

import com.my.exhibitions.entities.Exhibition;
import com.my.exhibitions.entities.User;
import com.my.exhibitions.repositories.ExhibitionRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExhibitionService {

    private final static Logger LOGGER = Logger.getLogger(ExhibitionService.class);
    private static final int DEFAULT_PAGE_SIZE = 5;

    private final ExhibitionRepository exhibitionRepository;
    private final HallService hallService;
    private final UserService userService;

    @Autowired
    public ExhibitionService(ExhibitionRepository exhibitionRepository, HallService hallService, UserService userService) {
        this.exhibitionRepository = exhibitionRepository;
        this.hallService = hallService;
        this.userService = userService;
    }

    public void save(Exhibition exhibition, List<String> halls) {
        LOGGER.info("Saving exhibition " + exhibition.getTheme());
        exhibition.setHalls(halls.stream().map(Long::parseLong).map(hallService::findById).collect(Collectors.toSet()));
        save(exhibition);
    }

    public void save(Exhibition exhibition) {
        LOGGER.info("Saving exhibition " + exhibition.getTheme());
        exhibitionRepository.save(exhibition);
    }


    public Exhibition findById(long id) {
        LOGGER.info("Getting exhibition with id " + id);
        return exhibitionRepository.findById(id);
    }

    public Page<Exhibition> getPage(int pageNum, String sortType) {
        LOGGER.info("Getting exhibitions on page with number " + pageNum + ", sorted by " + sortType);
        Pageable paging = PageRequest.of(pageNum, DEFAULT_PAGE_SIZE, Sort.by(sortType));
        return exhibitionRepository.findAll(paging);
    }

    public Page<Exhibition> getPage(int pageNum) {
        LOGGER.info("Getting exhibitions on page with number " + pageNum);
        Pageable paging = PageRequest.of(pageNum, DEFAULT_PAGE_SIZE);
        return exhibitionRepository.findAll(paging);
    }

    public void cancelExhibition(long id) {
        LOGGER.info("Cancelling exhibition with id " + id);
        Exhibition exhibition = exhibitionRepository.findById(id);
        exhibition.removeHalls();
        exhibition.removeUsers();
        exhibitionRepository.deleteById(id);
    }

    public void addCustomer(long exhibitionId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();

        LOGGER.info("Confirming ticket purchase for " + username + " for exhibition with id " + exhibitionId);

        Exhibition exhibition = exhibitionRepository.findById(exhibitionId);
        User currentUser = userService.findByUsername(username);

        List<Exhibition> exhibitions = currentUser.getExhibitions();
        exhibitions.add(exhibition);

        List<User> users = exhibition.getUsers();
        users.add(currentUser);

        save(exhibition);
        userService.update(currentUser);
    }

    public Map<Exhibition, Integer> getStats() {
        LOGGER.info("Getting exhibitions' stats on page");
        Map<Exhibition, Integer> stats = new LinkedHashMap<>();
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        exhibitions.forEach(exhibition -> {
            int ticketsSold = exhibitionRepository.getNumberOfTicketsSoldByID(exhibition.getId());
            stats.put(exhibition, ticketsSold);
        });
        return stats;
    }

    public int getNumberOfTicketsBoughtForUserAtExhibition(User user, Exhibition exhibition) {
        LOGGER.info("Getting number of tickets by " + user.getUsername() + " for " + exhibition.getTheme());
        return exhibitionRepository.getNumberOfTicketsBoughtBySingleUser(exhibition.getId(), user.getId());
    }

    public Map<String, Integer> getDetailedStats(long exhibitionId) {
        LOGGER.info("Getting detailed stats for exhibition " + exhibitionId);
        Map<String, Integer> stats = new LinkedHashMap<>();
        List<User> users = userService.findAll();
        users.forEach(user -> stats.put(
                user.getUsername(),
                exhibitionRepository.getNumberOfTicketsBoughtBySingleUser(exhibitionId, user.getId())
        ));
        return stats.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public boolean existsByTheme(String theme) {
        LOGGER.info("Checking if the exhibition " + theme + " exists");
        return exhibitionRepository.existsByTheme(theme);
    }

    public Exhibition findByTheme(String theme) {
        LOGGER.info("Getting the exhibition " + theme + " exists");
        return exhibitionRepository.findByTheme(theme);
    }
}
package com.my.exhibitions.controllers;

import com.my.exhibitions.entities.Exhibition;
import com.my.exhibitions.services.ExhibitionService;
import com.my.exhibitions.services.HallService;
import com.my.exhibitions.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ExhibitionController {


    private static final Logger LOGGER = Logger.getLogger(ExhibitionController.class);
    private final ExhibitionService exhibitionService;
    private final HallService hallService;
    private final UserService userService;

    @Autowired
    public ExhibitionController(ExhibitionService exhibitionService,
                                HallService hallService,
                                UserService userService) {
        this.exhibitionService = exhibitionService;
        this.hallService = hallService;
        this.userService = userService;
    }


    @GetMapping("/addExhibition")
    public String getAddExhibition(Model model) {
        LOGGER.info("Get -> /addExhibition");
        model.addAttribute("exhibition", new Exhibition());
        model.addAttribute("halls", hallService.findAll());
        return "addExhibition";
    }

    @PostMapping("/addExhibition")
    public String addNewExhibition(@Valid @ModelAttribute("exhibition") Exhibition exhibition,
                                   BindingResult bindingResult,
                                   @RequestParam(value = "chosenHalls", required = false) List<String> halls,
                                   Model model) {
        LOGGER.info("Post -> /addExhibition");
        boolean alreadyExists = exhibitionService.existsByTheme(exhibition.getTheme());
        if(alreadyExists) {
            bindingResult.rejectValue(
                    "theme",
                    "",
                    "Exhibition with such theme already exists"
            );
        }
        if(bindingResult.hasErrors()
                || exhibition.getStartDate().after(exhibition.getEndDate())
                || halls == null) {
            model.addAttribute("halls", hallService.findAll());
            LOGGER.error("Error while adding exhibitions");
            return "addExhibition";
        }
        exhibitionService.save(exhibition, halls);
        return "redirect:/home";
    }

    @GetMapping("/getExhibitions")
    public String getExhibitions(Model model,
                                 @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                 @RequestParam(value = "sortType", required = false, defaultValue = "default") String sortType,
                                 @RequestParam(value = "exhibitionId", required = false) Optional<Long> exhibitionId,
                                 @RequestParam(value = "canceledExhibitionId", required = false) Optional<Long> canceledExhibitionId) {
        LOGGER.info("Get -> /getExhibition");

        exhibitionId.ifPresent(exhibitionService::addCustomer);
        canceledExhibitionId.ifPresent(exhibitionService::cancelExhibition);

        Page<Exhibition> page;
        if(sortType.equals("default")) {
            page = exhibitionService.getPage(pageNum - 1);
        } else {
            page = exhibitionService.getPage(pageNum - 1, sortType);
        }
        model.addAttribute("exhibitions", page.toList());
        model.addAttribute("sortType", sortType);
        model.addAttribute("currentPage", pageNum);
        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        model.addAttribute("user", userService.findByUsername(username));
        model.addAttribute("exhService", exhibitionService);

        return "getExhibitions";
    }

    @GetMapping("/getStats")
    public String getStats(Model model) {
        LOGGER.info("Get -> /getStats");
        Map<Exhibition, Integer> stats = exhibitionService.getStats();
        model.addAttribute("stats", stats);
        return "getStats";
    }

    @GetMapping("/getStats/{theme}")
    public String getDetailedStats(Model model, @PathVariable String theme) {
        LOGGER.info("Get -> /getStats{" + theme + "}");
        Exhibition exhibition = exhibitionService.findByTheme(theme);
        Map<String, Integer> detailedStats = exhibitionService.getDetailedStats(exhibition.getId());
        model.addAttribute("stats", detailedStats);
        model.addAttribute("exhibition", exhibitionService.findById(exhibition.getId()));
        return "getDetailedStats";
    }
}
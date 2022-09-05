package com.my.exhibitions.controllers;

import com.my.exhibitions.entities.Hall;
import com.my.exhibitions.services.HallService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class HallController {


    private final static Logger LOGGER = Logger.getLogger(HallController.class);
    private final HallService hallService;

    @Autowired
    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping("/addHall")
    public String getAddHall(Model model) {
        LOGGER.info("Get -> /addHall");
        model.addAttribute("hall", new Hall());
        return "addHall";
    }

    @PostMapping("/addHall")
    public String addNewHall(@Valid @ModelAttribute("hall") Hall hall, BindingResult bindingResult) {
        LOGGER.info("Post -> /addHall");
        boolean alreadyExists = hallService.existsByName(hall.getName());
        if(alreadyExists) {
            bindingResult.rejectValue(
                    "name",
                    "",
                    "Hall with such name already exists"
            );
        }
        if(bindingResult.hasErrors()) {
            LOGGER.error("Error while adding new hall");
            return "addHall";
        }
        hallService.save(hall);
        return "redirect:/home";
    }

    @GetMapping("/getHalls")
    public String getHalls(Model model, @RequestParam("pageNum") int pageNum) {
        LOGGER.info("Get -> /getHalls");
        Page<Hall> page = hallService.getPage(pageNum - 1);
        model.addAttribute("halls", page.toList());
        model.addAttribute("currentPage", pageNum);
        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "getHalls";
    }
}
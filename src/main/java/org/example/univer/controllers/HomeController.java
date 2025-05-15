package org.example.univer.controllers;

import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.services.CathedraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private CathedraService cathedraService;

    public HomeController(CathedraService cathedraService) {
        this.cathedraService = cathedraService;
    }

    @GetMapping("/")
    public String index(Model model) {
        logger.debug("Show index page");
        model.addAttribute("title", "Welcome");

        cathedraService.findById(1L).ifPresentOrElse(cathedra -> {
                     model.addAttribute("cathedra", cathedra);
                     logger.debug("Found and edited audience with id: {}", cathedra.getId());
                }, () -> {
                     throw new ResourceNotFoundException("Cathedra not found");
                }
        );
        return "index";
    }
}

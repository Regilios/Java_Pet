package org.example.univer.controllers;

import org.example.univer.services.CathedraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        model.addAttribute("cathedraName", cathedraService.findById(1L).getName());
        model.addAttribute("title", "Welcome");
        return "index";
    }
}

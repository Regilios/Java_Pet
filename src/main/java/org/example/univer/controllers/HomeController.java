package org.example.univer.controllers;

import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.models.Cathedra;
import org.example.univer.services.CathedraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

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
        Optional<Cathedra> optionalCathedra = cathedraService.findById(1L);
        if (optionalCathedra.isPresent()) {
            Cathedra cathedra = optionalCathedra.get();
            model.addAttribute("audience", cathedra);
            logger.debug("Found and edited audience with id: {}", cathedra.getId());
        } else {
            logger.warn("Cathedra with id 1 not found");
            throw new ResourceNotFoundException("Cathedra not found");
        }
        return "index";
    }
}

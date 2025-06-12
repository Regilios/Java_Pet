package org.example.univer.controllers.web;

import org.example.univer.dto.CathedraDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.mappers.CathedraMapper;
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
    private final CathedraMapper cathedraMapper;

    public HomeController(CathedraService cathedraService,
                          CathedraMapper cathedraMapper) {
        this.cathedraService = cathedraService;
        this.cathedraMapper = cathedraMapper;
    }

    @GetMapping("/")
    public String index(Model model) {
        logger.debug("Show index page");
        model.addAttribute("title", "Welcome");

        CathedraDto dto = cathedraService.findById(1L)
                .map(cathedraMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Cathedra not found"));
        model.addAttribute("cathedraDto", dto);

        return "index";
    }
}

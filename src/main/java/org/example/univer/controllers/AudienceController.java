package org.example.univer.controllers;

import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.models.Audience;
import org.example.univer.services.AudienceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/audiences")
@Transactional
public class AudienceController {
    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);
    private AudienceService audienceService;

    public AudienceController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model, Pageable pageable) {
        model.addAttribute("title", "All Audiences");
        Page<Audience> page = audienceService.findAll(pageable);
        model.addAttribute("audiences", page);
        logger.debug("Show all audience");
        return "audiences/index";
    }

    /* Обарботка добавления */
    @GetMapping("/new")
    public String create(Audience audience, Model model) {
        model.addAttribute("title", "All Audiences");
        model.addAttribute(audience);
        logger.debug("Show create page");
        return "audiences/new";
    }

    @PostMapping
    public String newAudience(@ModelAttribute Audience audience, Model model, RedirectAttributes redirectAttributes) {
        try {
            audienceService.create(audience);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/audiences/new";
        }

        logger.debug("Create new audience. Id {}", audience.getId());
        return "redirect:/audiences";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        audienceService.findById(id).ifPresentOrElse(audience -> {
                    model.addAttribute("audience", audience);
                    logger.debug("Found and edited audience with id: {}", id);
                }, () -> {
                    throw new ResourceNotFoundException("Audience not found");
                }
        );

        logger.debug("Edit audience");
        return "audiences/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("audience") Audience audience, @PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            audienceService.update(audience);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/audiences/edit";
        }

        logger.debug("Show edit page");
        return "redirect:/audiences";
    }

    /* Обарботка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        audienceService.findById(id).ifPresentOrElse(audience -> {
                    model.addAttribute("audience", audience);
                    logger.debug("Found and edited audience with id: {}", id);
                }, () -> {
                    throw new ResourceNotFoundException("Audience not found");
                }
        );
        return "audiences/show";
    }

    /* Обарботка удаления */
    @DeleteMapping("{id}")
    public String delete(@ModelAttribute Audience audience) {
        audienceService.deleteEntity(audience);
        logger.debug("Deleted audience");
        return "redirect:/audiences";
    }
}

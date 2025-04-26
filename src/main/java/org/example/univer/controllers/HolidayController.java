package org.example.univer.controllers;

import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.models.Holiday;
import org.example.univer.services.HolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/holidays")
public class HolidayController {
    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);
    private HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("title", "All Holidays");
        model.addAttribute("holidays", holidayService.findAll());
        logger.debug("Show all holidays");
        return "holidays/index";
    }

    /* Обарботка добавления */
    @GetMapping("/new")
    public String create(Holiday holiday, Model model) {
        model.addAttribute("title", "All Holidays");
        model.addAttribute(holiday);
        logger.debug("Show create page");
        return "holidays/new";
    }

    @PostMapping
    public String newHoliday(@ModelAttribute Holiday holiday, Model model, RedirectAttributes redirectAttributes) {
        try {
            holidayService.create(holiday);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/holidays/new";
        }

        logger.debug("Create new holidays. Id {}", holiday.getId());
        return "redirect:/holidays";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        Optional<Holiday> holidayOptional = holidayService.findById(id);
        if (holidayOptional.isPresent()) {
            Holiday holiday = holidayOptional.get();
            model.addAttribute("holiday", holiday);
            logger.debug("Found and edited holiday with id: {}", id);
        } else {
            logger.warn("Holiday with id {} not found", id);
            throw new ResourceNotFoundException("Holiday not found");
        }
        logger.debug("Edit holiday");
        return "holidays/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("holiday") Holiday holiday, @PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            holidayService.update(holiday);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/holidays/edit";
        }

        logger.debug("Show edit page");
        return "redirect:/holidays";
    }

    /* Обарботка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Optional<Holiday> holidayOptional = holidayService.findById(id);
        if (holidayOptional.isPresent()) {
            Holiday holiday = holidayOptional.get();
            model.addAttribute("holiday", holiday);
            logger.debug("Found and edited holiday with id: {}", id);
        } else {
            logger.warn("Holiday with id {} not found", id);
            throw new ResourceNotFoundException("Holiday not found");
        }
        logger.debug("Edited holiday");
        return "holidays/show";
    }

    /* Обарботка удаления */
    @DeleteMapping("{id}")
    public String delete(@ModelAttribute Holiday holiday) {
        holidayService.deleteById(holiday);
        logger.debug("Deleted holiday");
        return "redirect:/holidays";
    }
}

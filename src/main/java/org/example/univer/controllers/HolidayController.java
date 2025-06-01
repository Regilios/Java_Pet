package org.example.univer.controllers;

import jakarta.validation.Valid;
import org.example.univer.dto.HolidayDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.HolidayMapper;
import org.example.univer.services.HolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/holidays")
public class HolidayController {
    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);
    private HolidayService holidayService;
    private final HolidayMapper holidayMapper;

    public HolidayController(HolidayService holidayService,
                             HolidayMapper holidayMapper) {
        this.holidayMapper = holidayMapper;
        this.holidayService = holidayService;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("holidaysDto", holidayService.findAll()
                .stream()
                .map(holidayMapper::toDto)
                .collect(Collectors.toList()));
        model.addAttribute("title", "All Holidays");
        logger.debug("Show all holidays");
        return "holidays/index";
    }

    /* Обработка добавления */
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("title", "All Holidays");
        model.addAttribute("holidayDto", new HolidayDto());
        logger.debug("Show create page");
        return "holidays/new";
    }

    @PostMapping
    public String newHoliday(@ModelAttribute("holidayDto") @Valid HolidayDto holidayDto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("holidayDto", holidayDto);
            return "holidays/new";
        }

        try {
            holidayService.create(holidayMapper.toEntity(holidayDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("holidayDto", holidayDto);
            model.addAttribute("errorMessage", e.getMessage());
            return "holidays/new";
        }

        logger.debug("Create new holidays. Id {}", holidayDto.getId());
        return "redirect:/holidays";
    }

    /* Обработка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        HolidayDto dto = holidayService.findById(id)
                .map(holidayMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found"));
        model.addAttribute("holidayDto", dto);
        logger.debug("Edit holiday");
        return "holidays/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("holidayDto") @Valid HolidayDto holidayDto,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("holidayDto", holidayDto);
            return "holidays/edit";
        }

        try {
            holidayDto.setId(id);
            holidayService.update(holidayMapper.toEntity(holidayDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("holidayDto", holidayDto);
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/holidays/edit";
        }

        logger.debug("Show edit page");
        return "redirect:/holidays";
    }

    /* Обработка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        HolidayDto dto = holidayService.findById(id)
                .map(holidayMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found"));
        model.addAttribute("holidayDto", dto);
        return "holidays/show";
    }

    /* Обработка удаления */
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        holidayService.deleteById(id);
        logger.debug("Deleted holiday");
        return "redirect:/holidays";
    }
}

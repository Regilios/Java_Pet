package org.example.univer.controllers;

import org.example.univer.dto.HolidayDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.HolidayMapper;
import org.example.univer.services.HolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    /* Обарботка добавления */
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("title", "All Holidays");
        model.addAttribute("holidaysDto", new HolidayDto());
        logger.debug("Show create page");
        return "holidays/new";
    }

    @PostMapping
    public String newHoliday(@ModelAttribute HolidayDto holidayDto,
                             RedirectAttributes redirectAttributes) {
        try {
            holidayService.create(holidayMapper.toEntity(holidayDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/holidays/new";
        }

        logger.debug("Create new holidays. Id {}", holidayDto.getId());
        return "redirect:/holidays";
    }

    /* Обарботка изменения */
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
    public String update(@ModelAttribute("holiday") HolidayDto holidayDto,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            holidayDto.setId(id);
            holidayService.update(holidayMapper.toEntity(holidayDto));
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
        HolidayDto dto = holidayService.findById(id)
                .map(holidayMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found"));
        model.addAttribute("holidayDto", dto);
        return "holidays/show";
    }

    /* Обарботка удаления */
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        holidayService.deleteById(id);
        logger.debug("Deleted holiday");
        return "redirect:/holidays";
    }
}

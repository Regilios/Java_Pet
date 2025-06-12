package org.example.univer.controllers.web;

import jakarta.validation.Valid;
import org.example.univer.dto.SubjectDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.SubjectMapper;
import org.example.univer.services.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);
    private SubjectService subjectService;
    private SubjectMapper subjectMapper;

    public SubjectController(SubjectService subjectService,
                             SubjectMapper subjectMapper) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("subjectsDto", subjectService.findAll()
                .stream()
                .map(subjectMapper::toDto)
                .collect(Collectors.toList()));
        model.addAttribute("title", "All Subjects");
        logger.debug("Show all subjects");
        return "subjects/index";
    }

    /* Обработка добавления */
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("subjectDto", new SubjectDto());
        logger.debug("Show create page");
        return "subjects/new";
    }

    @PostMapping
    public String newSubject(@ModelAttribute("subjectDto") @Valid SubjectDto subjectDto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("subjectDto", subjectDto);
            return "subjects/edit";
        }

        try {
            subjectService.create(subjectMapper.toEntity(subjectDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("subjectDto", subjectDto);
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/subjects/new";
        }

        logger.debug("Create new subject. Id {}", subjectDto.getId());
        return "redirect:/subjects";
    }

    /* Обработка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        SubjectDto dto = subjectService.findById(id)
                .map(subjectMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        model.addAttribute("subjectDto", dto);

        logger.debug("Edit subject");
        return "subjects/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("subjectDto") @Valid SubjectDto subjectDto,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("subjectDto", subjectDto);
            return "subjects/edit";
        }

        try {
            subjectDto.setId(id);
            subjectService.update(subjectMapper.toEntity(subjectDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("subjectDto", subjectDto);
            model.addAttribute("errorMessage", e.getMessage());
            return "subjects/edit";
        }

        logger.debug("Show edit page");
        return "redirect:/subjects";
    }

    /* Обработка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        SubjectDto dto = subjectService.findById(id)
                .map(subjectMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        model.addAttribute("subjectDto", dto);

        logger.debug("Edited student");
        return "subjects/show";
    }

    /* Обработка удаления */
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        subjectService.deleteById(id);
        logger.debug("Deleted student");
        return "redirect:/subjects";
    }
}

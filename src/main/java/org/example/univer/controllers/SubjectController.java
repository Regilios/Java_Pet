package org.example.univer.controllers;

import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.models.Subject;
import org.example.univer.services.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);
    private SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("title", "All Subjects");
        model.addAttribute("subjects", subjectService.findAll());
        logger.debug("Show all subjects");
        return "subjects/index";
    }

    /* Обарботка добавления */
    @GetMapping("/new")
    public String create(Subject subject, Model model) {
        model.addAttribute(subject);
        logger.debug("Show create page");
        return "subjects/new";
    }

    @PostMapping
    public String newSubject(@ModelAttribute("subject") Subject subject, Model model, RedirectAttributes redirectAttributes) {
        try {
            subjectService.create(subject);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/subjects/new";
        }

        logger.debug("Create new subject. Id {}", subject.getId());
        return "redirect:/subjects";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        subjectService.findById(id).ifPresentOrElse(subject -> {
                    model.addAttribute("subject", subject);
                    logger.debug("Found and edited subject with id: {}", id);
                }, () -> {
                    throw new ResourceNotFoundException("Subject not found");
                }
        );

        logger.debug("Edit subject");
        return "subjects/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("subject") Subject subject, @PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        String errorMessage = null;
        try {
            subjectService.update(subject);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/subjects/edit";
        }

        logger.debug("Show edit page");
        return "redirect:/subjects";
    }

    /* Обарботка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        subjectService.findById(id).ifPresentOrElse(subject -> {
                    model.addAttribute("subject", subject);
                    logger.debug("Found and edited subject with id: {}", id);
                }, () -> {
                    throw new ResourceNotFoundException("Subject not found");
                }
        );

        logger.debug("Edited student");
        return "subjects/show";
    }

    /* Обарботка удаления */
    @DeleteMapping("{id}")
    public String delete(@ModelAttribute Subject subject) {
        subjectService.deleteEntity(subject);
        logger.debug("Deleted student");
        return "redirect:/subjects";
    }
}

package org.example.univer.controllers;

import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.models.Subject;
import org.example.univer.models.Teacher;
import org.example.univer.services.CathedraService;
import org.example.univer.services.SubjectService;
import org.example.univer.services.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);
    private TeacherService teacherService;
    private CathedraService cathedraService;
    private SubjectService subjectService;

    public TeacherController(TeacherService teacherService, CathedraService cathedraService, SubjectService subjectService) {
        this.teacherService = teacherService;
        this.cathedraService = cathedraService;
        this.subjectService = subjectService;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("title", "All Teachers");
        model.addAttribute("teachers", teacherService.findAll());
        logger.debug("Show all teachers");
        return "teachers/index";
    }

    /* Обарботка добавления */
    @GetMapping("/new")
    public String create(Teacher teacher, Model model) {
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute(teacher);
        logger.debug("Show create page");
        return "teachers/new";
    }

    @PostMapping
    public String newTeacher(@ModelAttribute Teacher teacher,
                             @RequestParam("subjectIds") List<Long> subjectIds,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            logger.debug("Received subject IDs: {}", subjectIds);
            for (Long subjectId : subjectIds) {
                logger.debug("Test findById and get name: {}", subjectService.findById(subjectId).get().getName());
            }
            List<Subject> subjects = subjectIds.stream()
                    .map(subjectId -> subjectService.findById(subjectId).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());;
            logger.debug("Subjects finded: {}", subjects);
            teacher.setSubjects(subjects);
            logger.debug("Subjects added: {}",teacher.getSubjects());
            teacherService.create(teacher);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/teachers/new";
        }

        logger.debug("Create new teacher. Id {}", teacher.getId());
        return "redirect:/teachers";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        teacherService.findById(id).ifPresentOrElse(teacher -> {
                    model.addAttribute("teacher", teacher);
                    logger.debug("Found and edited teacher with id: {}", id);
                }, () -> {
                    throw new ResourceNotFoundException("Teacher not found");
                }
        );

        logger.debug("Edit teacher");
        return "teachers/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("teacher") Teacher teacher,
                         @RequestParam("subjectIds") List<Long> subjectIds,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            List<Subject> subjects = subjectIds.stream()
                    .map(subjectId -> subjectService.findById(subjectId).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            teacher.setSubjects(subjects);
            teacherService.update(teacher);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/teachers/{id}/edit";
        }

        logger.debug("Show edit page");
        return "redirect:/teachers";
    }

    /* Обарботка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("subjects", subjectService.getSubjectById(id));
        teacherService.findById(id).ifPresentOrElse(teacher -> {
                    model.addAttribute("teacher", teacher);
                    logger.debug("Found and edited teacher with id: {}", id);
                }, () -> {
                    throw new ResourceNotFoundException("Teacher not found");
                }
        );

        logger.debug("Show teacher");
        return "teachers/show";
    }

    /* Обарботка удаления */
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        teacherService.deleteById(id);
        logger.debug("Deleted teacher");
        return "redirect:/teachers";
    }
}

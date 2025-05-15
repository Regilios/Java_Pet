package org.example.univer.controllers;

import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.models.Student;
import org.example.univer.services.GroupService;
import org.example.univer.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentsController {
    private static final Logger logger = LoggerFactory.getLogger(StudentsController.class);
    private StudentService studentService;
    private GroupService groupService;

    public StudentsController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model, Pageable pageable) {
        Page<Student> page = studentService.findAll(pageable);
        model.addAttribute("students", page);
        logger.debug("Show all students");
        return "students/index";
    }

    /* Обарботка добавления */
    @GetMapping("/new")
    public String create(Student student, Model model) {
        model.addAttribute(student);
        model.addAttribute("groups", groupService.findAll());
        logger.debug("Show create page");
        return "students/new";
    }

    @PostMapping
    public String newStudent(@ModelAttribute("student") Student student, Model model, RedirectAttributes redirectAttributes) {
        try {
            studentService.create(student);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/students/new";
        }

        logger.debug("Create new student. Id {}", student.getId());
        return "redirect:/students";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("groups", groupService.findAll());
        studentService.findById(id).ifPresentOrElse(student -> {
                    model.addAttribute("student", student);
                    logger.debug("Found and edited student with id: {}", id);
                }, () -> {
                    throw new ResourceNotFoundException("Student not found");
                }
        );

        logger.debug("Show edit page");
        return "students/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("student") Student student, @PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        String errorMessage = null;
        try {
            studentService.update(student);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/students/edit";
        }

        logger.debug("Edited student");
        return "redirect:/students";
    }

    /* Обарботка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        studentService.findById(id).ifPresentOrElse(student -> {
                    model.addAttribute("student", student);
                    logger.debug("Found and edited student with id: {}", id);
                }, () -> {
                    throw new ResourceNotFoundException("Student not found");
                }
        );

        logger.debug("Show student");
        return "students/show";
    }

    /* Обарботка удаления */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        studentService.deleteById(id);
        logger.debug("Deleted student with id {}", id);
        return "redirect:/students";
    }
}

package org.example.univer.controllers;

import org.example.univer.dto.StudentDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.StudentMapper;
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
    private final StudentMapper studentMapper;

    public StudentsController(StudentService studentService, GroupService groupService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.studentMapper = studentMapper;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model, Pageable pageable) {
        Page<StudentDto> page = studentService.findAll(pageable).map(studentMapper::toDto);
        model.addAttribute("title", "All Students");
        model.addAttribute("studentsDto", page);
        logger.debug("Show all students");
        return "students/index";
    }

    /* Обарботка добавления */
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("studentDto", new StudentDto());
        model.addAttribute("groups", groupService.findAll());
        logger.debug("Show create page");
        return "students/new";
    }

    @PostMapping
    public String newStudent(@ModelAttribute("student") StudentDto studentDto,
                             RedirectAttributes redirectAttributes) {
        try {
            studentService.create(studentMapper.toEntity(studentDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/students/new";
        }

        logger.debug("Create new student. Id {}", studentDto.getId());
        return "redirect:/students";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        StudentDto dto = studentService.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        model.addAttribute("studentDto", dto);
        model.addAttribute("groups", groupService.findAll());
        logger.debug("Show edit page");
        return "students/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("student") StudentDto studentDto,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            studentDto.setId(id);
            studentService.update(studentMapper.toEntity(studentDto));
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
        StudentDto dto = studentService.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        model.addAttribute("studentDto", dto);
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

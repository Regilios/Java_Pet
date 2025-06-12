package org.example.univer.controllers.web;

import jakarta.validation.Valid;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private StudentService studentService;
    private GroupService groupService;
    private final StudentMapper studentMapper;

    public StudentController(StudentService studentService, GroupService groupService, StudentMapper studentMapper) {
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

    /* Обработка добавления */
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("studentDto", new StudentDto());
        model.addAttribute("groups", groupService.findAll());
        logger.debug("Show create page");
        return "students/new";
    }

    @PostMapping
    public String newStudent(@ModelAttribute("studentDto") @Valid StudentDto studentDto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("studentDto", studentDto);
            model.addAttribute("groups", groupService.findAll());
            return "students/new";
        }

        try {
            studentService.create(studentMapper.toEntity(studentDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("studentDto", studentDto);
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/students/new";
        }

        logger.debug("Create new student. Id {}", studentDto.getId());
        return "redirect:/students";
    }

    /* Обработка изменения */
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
    public String update(@ModelAttribute("studentDto") @Valid StudentDto studentDto,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("studentDto", studentDto);
            model.addAttribute("groups", groupService.findAll());
            return "students/edit";
        }

        try {
            studentDto.setId(id);
            studentService.update(studentMapper.toEntity(studentDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("studentDto", studentDto);
            model.addAttribute("groups", groupService.findAll());
            model.addAttribute("errorMessage", e.getMessage());
            return "students/edit";
        }

        logger.debug("Edited student");
        return "redirect:/students";
    }

    /* Обработка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        StudentDto dto = studentService.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        model.addAttribute("studentDto", dto);
        logger.debug("Show student");
        return "students/show";
    }

    /* Обработка удаления */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        studentService.deleteById(id);
        logger.debug("Deleted student with id {}", id);
        return "redirect:/students";
    }
}

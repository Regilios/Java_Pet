package org.example.univer.controllers;

import org.example.univer.dto.TeacherDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.TeacherMapper;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);
    private TeacherService teacherService;
    private CathedraService cathedraService;
    private SubjectService subjectService;
    private final TeacherMapper teacherMapper;

    public TeacherController(TeacherService teacherService,
                             CathedraService cathedraService,
                             SubjectService subjectService,
                             TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.cathedraService = cathedraService;
        this.subjectService = subjectService;
        this.teacherMapper = teacherMapper;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("teachersDto", teacherService.findAll()
                .stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList()));
        model.addAttribute("title", "All Teachers");
        logger.debug("Show all teachers");
        return "teachers/index";
    }

    /* Обарботка добавления */
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute("teacherDto", new TeacherDto());
        logger.debug("Show create page");
        return "teachers/new";
    }

    @PostMapping
    public String newLecture(@ModelAttribute TeacherDto teacherDto,
                             @RequestParam(value = "subjectIds", required = false) List<Long> subjectIds,
                             RedirectAttributes redirectAttributes) {
        try {
            teacherDto.setSubjectIds(subjectIds);
            teacherService.create(teacherMapper.toEntity(teacherDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/teachers/new";
        }
        return "redirect:/teachers";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        TeacherDto dto = teacherService.findById(id)
                .map(teacherMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        model.addAttribute("teacherDto", dto);
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute("subjects", subjectService.findAll());

        logger.debug("Edit teacher");
        return "teachers/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("teacher") TeacherDto teacherDto,
                         @RequestParam("subjectIds") List<Long> subjectIds,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            teacherDto.setSubjectIds(subjectIds);
            teacherService.update(teacherMapper.toEntity(teacherDto));
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
        TeacherDto dto = teacherService.findById(id)
                .map(teacherMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        model.addAttribute("teacherDto", dto);

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

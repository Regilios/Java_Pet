package org.example.univer.controllers.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.univer.dto.LectureDto;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.LectureMapper;
import org.example.univer.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class LectureController {
    private static final Logger logger = LoggerFactory.getLogger(LectureController.class);
    private final LectureService lectureService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final AudienceService audienceService;
    private final SubjectService subjectService;
    private final CathedraService cathedraService;
    private final LectureTimeService lectureTimeService;
    private final LectureMapper lectureMapper;

    @GetMapping
    public String index(Model model, Pageable pageable) {
        model.addAttribute("title", "All Lectures");
        model.addAttribute("lecturesDto", lectureService.findAllWithGroups(pageable));
        return "lectures/index";
    }

    /* Общая страница */
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("lectureDto", new LectureDto());
        addCommonAttributes(model);
        return "lectures/new";
    }

    /* Новая лекция */
    @PostMapping
    public String newLecture(@ModelAttribute("lectureDto") @Valid LectureDto lectureDto,
                             BindingResult bindingResult,
                             Model model,
                             @RequestParam(value = "groupIds", required = false) List<Long> groupIds,
                             RedirectAttributes redirectAttributes) {

        lectureDto.setGroupIds(groupIds);

        if (bindingResult.hasErrors()) {
            model.addAttribute("lectureDto", lectureDto);
            addCommonAttributes(model);
            return "lectures/new";
        }

        try {
            lectureService.create(lectureMapper.toEntity(lectureDto));
            return "redirect:/lectures";
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("lectureDto", lectureDto);
            addCommonAttributes(model);
            return "redirect:/lectures/new";
        }
    }

    /* Обработка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        LectureDto dto = lectureService.findById(id);
        model.addAttribute("lectureDto", dto);
        addCommonAttributes(model);
        return "lectures/edit";
}

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("lectureDto") @Valid LectureDto lectureDto,
                         BindingResult bindingResult,
                         @RequestParam(value = "groupIds", required = false) List<Long> groupIds,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {

            lectureDto.setId(id);
            lectureDto.setGroupIds(groupIds);

            if (bindingResult.hasErrors()) {
                model.addAttribute("lectureDto", lectureDto);
            addCommonAttributes(model);
            return "lectures/edit";
        }

        try {
            lectureService.update(lectureMapper.toEntity(lectureDto));
            return "redirect:/lectures";
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("lectureDto", lectureDto);
            addCommonAttributes(model);
            return "lectures/edit";
        }
    }

    /* Обработка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("lectureDto", lectureService.findById(id));
        return "lectures/show";
    }

    /* Обработка удаления */
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        lectureService.deleteById(id);
        return "redirect:/lectures";
    }

    private void addCommonAttributes(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("times", lectureTimeService.findAll());
        model.addAttribute("audiences", audienceService.findAll());
        model.addAttribute("groups", groupService.findAll());
    }
}
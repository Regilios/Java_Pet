package org.example.univer.controllers;

import org.example.univer.dto.LectureDto;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.LectureMapper;
import org.example.univer.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/lectures")
public class LectureController {
    private static final Logger logger = LoggerFactory.getLogger(LectureController.class);
    private CathedraService cathedraService;
    private TeacherService teacherService;
    private SubjectService subjectService;
    private LectureTimeService lectureTimeService;
    private AudienceService audienceService;
    private LectureService lectureService;
    private GroupService groupService;
    private final LectureMapper lectureMapper;

    public LectureController(TeacherService teacherService,
                             CathedraService cathedraService,
                             SubjectService subjectService,
                             LectureTimeService lectureTimeService,
                             AudienceService audienceService,
                             LectureService lectureService,
                             GroupService groupService,
                             LectureMapper lectureMapper) {
        this.teacherService = teacherService;
        this.cathedraService = cathedraService;
        this.subjectService = subjectService;
        this.lectureTimeService = lectureTimeService;
        this.audienceService = audienceService;
        this.lectureService = lectureService;
        this.groupService = groupService;
        this.lectureMapper = lectureMapper;
    }
    @GetMapping()
    public String index(Model model, Pageable pageable) {
        Page<LectureDto> page = lectureService.findAllWithGroups(pageable);
        model.addAttribute("title", "All Lectures");
        model.addAttribute("lecturesDto", page);
        return "lectures/index";
    }

    /* Общая страница */
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("lectureDto", new LectureDto());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("times", lectureTimeService.findAll());
        model.addAttribute("audiences", audienceService.findAll());
        model.addAttribute("groups", groupService.findAll());
        logger.debug("Show all lectures");
        return "lectures/new";
    }

    /* Новая лекция */
    @PostMapping
    public String newLecture(@ModelAttribute LectureDto lectureDto,
                             @RequestParam(value = "groupsId", required = false) List<Long> groupIds,
                             RedirectAttributes redirectAttributes) {
        try {
            lectureDto.setGroupIds(groupIds);
            lectureService.create(lectureMapper.toEntity(lectureDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/lectures/new";
        }
        logger.debug("Create new lecture. Id {}", lectureDto.getId());
        return "redirect:/lectures";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        LectureDto dto = lectureService.findById(id);
        model.addAttribute("lectureDto", dto);
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("times", lectureTimeService.findAll());
        model.addAttribute("audiences", audienceService.findAll());
        model.addAttribute("groups", groupService.findAll());

        logger.debug("Edit lecture");
        return "lectures/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute LectureDto lectureDto,
                         @RequestParam(value = "groupIds", required = false) List<Long> groupIds,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            lectureDto.setId(id);
            lectureDto.setGroupIds(groupIds);
            lectureService.update(lectureMapper.toEntity(lectureDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/lectures/" + id + "/edit";
        }
        return "redirect:/lectures";
    }

    /* Обарботка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        LectureDto dto = lectureService.findById(id);
        model.addAttribute("lectureDto", dto);
        return "lectures/show";
    }

    /* Обарботка удаления */
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        lectureService.deleteById(id);
        logger.debug("Deleted lecture");
        return "redirect:/lectures";
    }
}

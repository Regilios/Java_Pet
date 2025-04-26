package org.example.univer.controllers;

import org.example.univer.exeption.ServiceException;
import org.example.univer.models.Group;
import org.example.univer.models.Lecture;
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
import java.util.stream.Collectors;

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

    public LectureController(TeacherService teacherService,
                             CathedraService cathedraService,
                             SubjectService subjectService,
                             LectureTimeService lectureTimeService,
                             AudienceService audienceService,
                             LectureService lectureService,
                             GroupService groupService) {
        this.teacherService = teacherService;
        this.cathedraService = cathedraService;
        this.subjectService = subjectService;
        this.lectureTimeService = lectureTimeService;
        this.audienceService = audienceService;
        this.lectureService = lectureService;
        this.groupService = groupService;
    }

    @GetMapping()
    public String index(Model model, Pageable pageable) {
        model.addAttribute("title", "All Lectures");
        Page<Lecture> page = lectureService.findAllWithGroup(pageable);
        model.addAttribute("lectures", page);
        logger.debug("Show all lectures");
        return "lectures/index";
    }

    /* Общая страница */
    @GetMapping("/new")
    public String index(Lecture lecture, Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("times", lectureTimeService.findAll());
        model.addAttribute("audiences", audienceService.findAll());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute(lecture);
        logger.debug("Show all lectures");
        return "lectures/new";
    }

    @PostMapping
    public String newLecture(@ModelAttribute Lecture lecture,
                             @RequestParam(value = "groups", required = false) List<Long> groupIds,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {

            lecture.setCathedra(cathedraService.findById(lecture.getCathedra().getId()));
            lecture.setTeacher(teacherService.findById(lecture.getTeacher().getId()));
            lecture.setSubject(subjectService.findById(lecture.getSubject().getId()));
            lecture.setAudience(audienceService.findById(lecture.getAudience().getId()));
            lecture.setTime(lectureTimeService.findById(lecture.getTime().getId()));

            List<Group> groups = groupIds.stream()
                    .map(groupService::findById)
                    .collect(Collectors.toList());
            lecture.setGroup(groups);
            lectureService.create(lecture);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/lectures/new";
        }

        logger.debug("Create new lecture. Id {}", lecture.getId());
        return "redirect:/lectures";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("times", lectureTimeService.findAll());
        model.addAttribute("audiences", audienceService.findAll());
        model.addAttribute("lecture", lectureService.findById(id));
        logger.debug("Edit teacher");
        return "lectures/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Lecture lecture,
                         @RequestParam(value = "groups", required = false) List<Long> groupIds,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            lecture.setCathedra(cathedraService.findById(lecture.getCathedra().getId()));
            lecture.setTeacher(teacherService.findById(lecture.getTeacher().getId()));
            lecture.setSubject(subjectService.findById(lecture.getSubject().getId()));
            lecture.setAudience(audienceService.findById(lecture.getAudience().getId()));
            lecture.setTime(lectureTimeService.findById(lecture.getTime().getId()));

            List<Group> groups = groupIds.stream()
                    .map(groupService::findById)
                    .collect(Collectors.toList());
            lecture.setGroup(groups);
            lectureService.update(lecture);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/lectures/{id}/edit";
        }

        logger.debug("Show edit page");
        return "redirect:/lectures";
    }

    /* Обарботка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        List<Long> listGroups = lectureService.getListGroupForLecture(id);
        model.addAttribute("groups", groupService.getGroupById(listGroups));
        model.addAttribute("lecture", lectureService.findById(id));
        logger.debug("Show lecture");
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

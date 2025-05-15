package org.example.univer.controllers;

import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.models.Lecture;
import org.example.univer.models.Teacher;
import org.example.univer.models.Vacation;
import org.example.univer.services.LectureService;
import org.example.univer.services.TeacherService;
import org.example.univer.services.VacationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/teachers/{teacherId}/vacations")
public class VacationController {
    private static final Logger logger = LoggerFactory.getLogger(VacationController.class);
    private TeacherService teacherService;
    private VacationService vacationService;
    private LectureService lectureService;

    public VacationController(TeacherService teacherService, VacationService vacationService,
                              LectureService lectureService) {
        this.teacherService = teacherService;
        this.vacationService = vacationService;
        this.lectureService = lectureService;
    }

    /* Общая страница */
    @GetMapping()
    public String index(@PathVariable("teacherId") Long teacherId, Model model) {
        model.addAttribute("title", "All Vacations");
        model.addAttribute("vacations", vacationService.findByTeacherId(teacherId));
        Teacher teacher = teacherService.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        model.addAttribute("teacher", teacher);
        logger.debug("Show all vacations for teacher");
        return "teachers/vacations/index";
    }

    /* Обарботка добавления */
    @GetMapping("/new")
    public String create(@PathVariable("teacherId") Long teacherId, Vacation vacation, Model model) {
        Teacher teacher = teacherService.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        model.addAttribute("teacher", teacher);
        model.addAttribute(vacation);
        logger.debug("Show create page");
        return "teachers/vacations/new";
    }

    @PostMapping
    public String newVacation(@ModelAttribute Vacation vacation,
                              @PathVariable("teacherId") Long teacherId,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {

            teacherService.findById(teacherId).ifPresent(vacation::setTeacher);
            logger.debug("Create new vacation. Id {}", vacation.getId());
            List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(
                    vacation.getTeacher(),
                    vacation.getStartJob(),
                    vacation.getEndJob()
            );

            if (lectures.isEmpty()) {
                vacationService.create(vacation);
                logger.debug("Create new vacation. Id {}", vacation.getId());
            } else {
                logger.debug("Vacation wasn`t created! Need to change teacher in some lectures");
                return "redirect:/teachers/" + teacherId + "/vacations/lectures?start=" + vacation.getStartJob() + "&end="
                        + vacation.getEndJob();
            }
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/teachers/" + teacherId + "/vacations/new";
        }
        return "redirect:/teachers/" + teacherId + "/vacations";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("teacherId") Long teacherId, @PathVariable("id") Long vacationId, Model model) {
        Teacher teacher = teacherService.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        model.addAttribute("teacher", teacher);
        vacationService.findById(vacationId).ifPresentOrElse(vacation -> {
                    model.addAttribute("vacation", vacation);
                    logger.debug("Found and edited vacation with id: {}", vacationId);
                }, () -> {
                    throw new ResourceNotFoundException("Vacation not found");
                }
        );

        logger.debug("Edit vacation");
        return "teachers/vacations/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Vacation vacation,
                         @PathVariable("teacherId") Long teacherId,
                         @PathVariable("id") Long vacationId,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            teacherService.findById(teacherId).ifPresent(vacation::setTeacher);
            List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(
                    vacation.getTeacher(),
                    vacation.getStartJob(),
                    vacation.getEndJob()
            );

            if (lectures.isEmpty()) {
                vacationService.update(vacation);
                logger.debug("Create new vacation. Id {}", vacation.getId());
            } else {
                logger.debug("Vacation wasn`t created! Need to change teacher in some lectures");
                return "redirect:/teachers/" + teacherId + "/vacations/lectures?start=" + vacation.getStartJob() + "&end="
                        + vacation.getEndJob();
            }
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/teachers/{id}/edit";
        }

        logger.debug("Show edit page");
        return "redirect:/teachers/" + teacherId + "/vacations";
    }

    /* Показать лекции учителя попавшие на период отпуска */
    @GetMapping("/lectures")
    public String changeTeacherOnLectures(@PathVariable("teacherId") Long teacherId,
                                          Model model,
                                          @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                          @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherService.findById(teacherId).orElse(null), start, end);
        Teacher teacher = teacherService.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        model.addAttribute("teacher", teacher);
        model.addAttribute("lectures", lectures);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "teachers/vacations/lectures";
    }

    /* Обарботка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        vacationService.findById(id).ifPresentOrElse(vacation -> {
                    model.addAttribute("vacation", vacation);
                    logger.debug("Found and edited vacation with id: {}", id);
                }, () -> {
                    throw new ResourceNotFoundException("Vacation not found");
                }
        );

        logger.debug("Edited vacation");
        return "teachers/vacations/show";
    }

    /* Обарботка удаления */
    @DeleteMapping("{id}")
    public String delete(@PathVariable Long id, @PathVariable("teacherId") Long teacherId) {
        try {
            vacationService.deleteById(id);
            logger.debug("Vacation with id {} was deleted", id);
        } catch (ResourceNotFoundException ex) {
            logger.warn("Attempted to delete non-existing vacation with id {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
        logger.debug("Deleted teacher");
        return "redirect:/teachers/{teacherId}/vacations";
    }
}

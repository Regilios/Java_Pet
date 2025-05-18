package org.example.univer.controllers;

import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.models.LectureTime;
import org.example.univer.services.LectureTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RequestMapping("/lecturetimes")
public class LectureTimeController {
    private static final Logger logger = LoggerFactory.getLogger(LectureTimeController.class);
    private LectureTimeService lectureTimeService;

    public LectureTimeController(LectureTimeService lectureTimeService) {
        this.lectureTimeService = lectureTimeService;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("title", "All lecturetimes");
        model.addAttribute("lectureTimes", lectureTimeService.findAll());
        logger.debug("Show all lecturetimes");
        return "lecturetimes/index";
    }

    /* Обарботка добавления */
    @GetMapping("/new")
    public String create(LectureTime lectureTime, Model model) {
        model.addAttribute("title", "All lecturetimes");
        model.addAttribute(lectureTime);
        logger.debug("Show create page");
        return "lecturetimes/new";
    }

    @PostMapping
    public String newLetureTime(@RequestParam("start_date") String startDate,
                           @RequestParam("start_time") String startTime,
                           @RequestParam("end_date") String endDate,
                           @RequestParam("end_time") String endTime,
                           Model model, RedirectAttributes redirectAttributes) {
        try {
            LocalDateTime startLecture = LocalDateTime.of(LocalDate.parse(startDate), LocalTime.parse(startTime));
            LocalDateTime endLecture = LocalDateTime.of(LocalDate.parse(endDate), LocalTime.parse(endTime));

            LectureTime lectureTime = new LectureTime();
            lectureTime.setStartLecture(startLecture);
            lectureTime.setEndLecture(endLecture);
            lectureTimeService.create(lectureTime);
            logger.debug("Create new group. Id {}", lectureTime.getId());
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/lecturetimes/new";
        }

        return "redirect:/lecturetimes";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        lectureTimeService.findById(id).ifPresentOrElse(lectureTime -> {
                    model.addAttribute("lectureTime", lectureTime);
                    logger.debug("Found and edited lectureTime with id: {}", id);
                }, () -> {
                    throw new ResourceNotFoundException("LectureTime not found");
                }
        );

        logger.debug("Edit lectureTime");
        return "lecturetimes/edit";
    }

    @PatchMapping("/{id}")
    public String update(@RequestParam("start_date") String startDate,
                         @RequestParam("start_time") String startTime,
                         @RequestParam("end_date") String endDate,
                         @RequestParam("end_time") String endTime,
                         @PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            LocalDateTime startLecture = LocalDateTime.of(LocalDate.parse(startDate), LocalTime.parse(startTime));
            LocalDateTime endLecture = LocalDateTime.of(LocalDate.parse(endDate), LocalTime.parse(endTime));

            LectureTime lectureTime = new LectureTime();
            lectureTime.setId(id);
            lectureTime.setStartLecture(startLecture);
            lectureTime.setEndLecture(endLecture);
            lectureTimeService.update(lectureTime);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/lecturetimes/edit";
        }

        logger.debug("Show edit page");
        return "redirect:/lecturetimes";
    }



    /* Обарботка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("lectureTime", lectureTimeService.findById(id));
        lectureTimeService.findById(id).ifPresentOrElse(lectureTime -> {
                    model.addAttribute("lectureTime", lectureTime);
                    logger.debug("Found and edited lectureTime with id: {}", id);
                }, () -> {
                    throw new ResourceNotFoundException("LectureTime not found");
                }
        );

        logger.debug("Edited lectureTime");
        return "lecturetimes/show";
    }

    /* Обарботка удаления */
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        lectureTimeService.deleteById(id);
        logger.debug("Deleted lectureTime");
        return "redirect:/lecturetimes";
    }
}

package org.example.univer.controllers;

import org.example.univer.dto.LectureTimeDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.LectureTimeMapper;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/lecturetimes")
public class LectureTimeController {
    private static final Logger logger = LoggerFactory.getLogger(LectureTimeController.class);
    private LectureTimeService lectureTimeService;
    private final LectureTimeMapper lectureTimeMapper;

    public LectureTimeController(LectureTimeService lectureTimeService,
                                 LectureTimeMapper lectureTimeMapper) {
        this.lectureTimeService = lectureTimeService;
        this.lectureTimeMapper = lectureTimeMapper;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("lectureTimesDto", lectureTimeService.findAll()
                .stream()
                .map(lectureTimeMapper::toDto)
                .collect(Collectors.toList()));
        model.addAttribute("title", "All lecturetimes");
        logger.debug("Show all lecturetimes");
        return "lecturetimes/index";
    }

    /* Обарботка добавления */
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("title", "All lecturetimes");
        model.addAttribute("lectureTimeDto", new LectureTimeDto());
        logger.debug("Show create page");
        return "lecturetimes/new";
    }

    @PostMapping
    public String newLectureTime(
                           @RequestParam("start_date") String startDate,
                           @RequestParam("start_time") String startTime,
                           @RequestParam("end_date") String endDate,
                           @RequestParam("end_time") String endTime,
                           RedirectAttributes redirectAttributes) {
        try {
            LocalDateTime startLecture = LocalDateTime.of(LocalDate.parse(startDate), LocalTime.parse(startTime));
            LocalDateTime endLecture = LocalDateTime.of(LocalDate.parse(endDate), LocalTime.parse(endTime));

            LectureTimeDto lectureTimeDto = new LectureTimeDto();
            lectureTimeDto.setStartLecture(startLecture);
            lectureTimeDto.setEndLecture(endLecture);
            lectureTimeService.create(lectureTimeMapper.toEntity(lectureTimeDto));
            logger.debug("Create new group. Id {}", lectureTimeDto.getId());
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/lecturetimes/new";
        }

        return "redirect:/lecturetimes";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        LectureTimeDto dto = lectureTimeService.findById(id)
                .map(lectureTimeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("LectureTime not found"));
        model.addAttribute("lectureTimeDto", dto);
        logger.debug("Edit lectureTime");
        return "lecturetimes/edit";
    }

    @PatchMapping("/{id}")
    public String update(@RequestParam("start_date") String startDate,
                         @RequestParam("start_time") String startTime,
                         @RequestParam("end_date") String endDate,
                         @RequestParam("end_time") String endTime,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            LocalDateTime startLecture = LocalDateTime.of(LocalDate.parse(startDate), LocalTime.parse(startTime));
            LocalDateTime endLecture = LocalDateTime.of(LocalDate.parse(endDate), LocalTime.parse(endTime));

            LectureTimeDto lectureTimeDto = new LectureTimeDto();
            lectureTimeDto.setId(id);
            lectureTimeDto.setStartLecture(startLecture);
            lectureTimeDto.setEndLecture(endLecture);
            lectureTimeService.update(lectureTimeMapper.toEntity(lectureTimeDto));
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
        LectureTimeDto dto = lectureTimeService.findById(id)
                .map(lectureTimeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("LectureTime not found"));
        model.addAttribute("lectureTimeDto", dto);

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

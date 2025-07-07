package org.example.univer.controllers.web;

import jakarta.validation.Valid;
import org.example.univer.dto.LectureTimeDto;
import org.example.univer.dto.LectureTimeFormDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.LectureTimeMapper;
import org.example.univer.services.LectureTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
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

    /* Обработка добавления */
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("title", "All lecturetimes");
        model.addAttribute("lectureTimeFormDto", new LectureTimeFormDto());
        logger.debug("Show create page");
        return "lecturetimes/new";
    }

    @PostMapping
    public String newLectureTime(@ModelAttribute("lectureTimeFormDto") @Valid LectureTimeFormDto formDto,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("lectureTimeFormDto", formDto);
            return "lecturetimes/new";
        }

        try {
            LocalDateTime startLecture = LocalDateTime.of(
                    LocalDate.parse(formDto.getStartDate()),
                    LocalTime.parse(formDto.getStartTime())
            );
            LocalDateTime endLecture = LocalDateTime.of(
                    LocalDate.parse(formDto.getEndDate()),
                    LocalTime.parse(formDto.getEndTime())
            );

            LectureTimeDto dto = new LectureTimeDto();
            dto.setStartLecture(startLecture);
            dto.setEndLecture(endLecture);

            lectureTimeService.create(lectureTimeMapper.toEntity(dto));
        } catch (DateTimeParseException e) {
            model.addAttribute("lectureTimeFormDto", formDto);
            model.addAttribute("errorMessage", "Неверный формат даты или времени");
            return "redirect:/lecturetimes/new";
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/lecturetimes/new";
        }

        return "redirect:/lecturetimes";
    }

    /* Обработка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        if (!model.containsAttribute("lectureTimeDto")) {
            LectureTimeDto dto = lectureTimeService.findById(id)
                    .map(lectureTimeMapper::toDto)
                    .orElseThrow(() -> new ResourceNotFoundException("LectureTime not found"));
            model.addAttribute("lectureTimeDto", dto);
            model.addAttribute("id", id);
        }

        if (!model.containsAttribute("lectureTimeFormDto")) {
            LectureTimeDto dto = (LectureTimeDto) model.getAttribute("lectureTimeDto");

            LectureTimeFormDto formDto = new LectureTimeFormDto();
            formDto.setStartDate(dto.getStartLecture().toLocalDate().toString());
            formDto.setStartTime(dto.getStartLecture().toLocalTime().toString());
            formDto.setEndDate(dto.getEndLecture().toLocalDate().toString());
            formDto.setEndTime(dto.getEndLecture().toLocalTime().toString());

            model.addAttribute("lectureTimeFormDto", formDto);
            model.addAttribute("id", id);
        }

        logger.debug("Edit lectureTime");
        return "lecturetimes/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("lectureTimeFormDto") @Valid LectureTimeFormDto formDto,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("lectureTimeFormDto", formDto);
            return "lecturetimes/edit";
        }

        try {
            LocalDateTime startLecture = LocalDateTime.of(
                    LocalDate.parse(formDto.getStartDate()),
                    LocalTime.parse(formDto.getStartTime())
            );
            LocalDateTime endLecture = LocalDateTime.of(
                    LocalDate.parse(formDto.getEndDate()),
                    LocalTime.parse(formDto.getEndTime())
            );

            LectureTimeDto dto = new LectureTimeDto();
            dto.setStartLecture(startLecture);
            dto.setEndLecture(endLecture);

            lectureTimeService.update(lectureTimeMapper.toEntity(dto));
        } catch (DateTimeParseException e) {
            model.addAttribute("lectureTimeFormDto", formDto);
            model.addAttribute("errorMessage", "Неверный формат даты или времени");
            return "lecturetimes/edit";
        } catch (ServiceException e) {
            LectureTimeDto dto = new LectureTimeDto();
            dto.setStartLecture(LocalDateTime.of(LocalDate.parse(formDto.getStartDate()), LocalTime.parse(formDto.getStartTime())));
            dto.setEndLecture(LocalDateTime.of(LocalDate.parse(formDto.getEndDate()), LocalTime.parse(formDto.getEndTime())));
            dto.setId(id); // если нужно

            redirectAttributes.addFlashAttribute("lectureTimeDto", dto);
            redirectAttributes.addFlashAttribute("lectureTimeFormDto", formDto);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/lecturetimes/" + id + "/edit";
        }

        logger.debug("Show edit page");
        return "redirect:/lecturetimes";
    }

    /* Обработка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        LectureTimeDto dto = lectureTimeService.findById(id)
                .map(lectureTimeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("LectureTime not found"));
        model.addAttribute("lectureTimeDto", dto);

        logger.debug("Edited lectureTime");
        return "lecturetimes/show";
    }

    /* Обработка удаления */
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        lectureTimeService.deleteById(id);
        logger.debug("Deleted lectureTime");
        return "redirect:/lecturetimes";
    }
}

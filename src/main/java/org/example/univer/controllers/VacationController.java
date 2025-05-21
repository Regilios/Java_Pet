package org.example.univer.controllers;

import org.example.univer.dto.VacationDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.TeacherMapper;
import org.example.univer.mappers.VacationMapper;
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
    private final VacationMapper vacationMapper;
    private final TeacherMapper teacherMapper;

    public VacationController(TeacherService teacherService,
                              VacationService vacationService,
                              LectureService lectureService,
                              VacationMapper vacationMapper,
                              TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.vacationService = vacationService;
        this.lectureService = lectureService;
        this.vacationMapper = vacationMapper;
        this.teacherMapper = teacherMapper;
    }

    @GetMapping
    public String index(@PathVariable("teacherId") Long teacherId, Model model) {
        Teacher teacher = teacherService.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        List<VacationDto> vacations = vacationService.findByTeacherId(teacherId).stream()
                .map(vacationMapper::toDto)
                .toList();

        model.addAttribute("title", "All Vacations");
        model.addAttribute("teacher", teacherMapper.toDto(teacher));
        model.addAttribute("vacations", vacations);

        return "teachers/vacations/index";
    }

    @GetMapping("/new")
    public String create(@PathVariable("teacherId") Long teacherId, Model model) {
        Teacher teacher = teacherService.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        VacationDto dto = new VacationDto();
        dto.setTeacher(teacherMapper.toDto(teacher));

        model.addAttribute("teacher", teacherMapper.toDto(teacher));
        model.addAttribute("vacationDto", dto);

        return "teachers/vacations/new";
    }

    @PostMapping
    public String newVacation(@ModelAttribute VacationDto vacationDto,
                              @PathVariable("teacherId") Long teacherId,
                              RedirectAttributes redirectAttributes) {
        try {
            Teacher teacher = teacherService.findById(teacherId)
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
            vacationDto.setTeacher(teacherMapper.toDto(teacher));

            Vacation vacation = vacationMapper.toEntity(vacationDto);

            List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(
                    teacher, vacation.getStartJob(), vacation.getEndJob()
            );

            if (lectures.isEmpty()) {
                vacationService.create(vacation);
            } else {
                return "redirect:/teachers/" + teacherId + "/vacations/lectures?start=" +
                        vacation.getStartJob() + "&end=" + vacation.getEndJob();
            }

        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/teachers/" + teacherId + "/vacations/new";
        }

        return "redirect:/teachers/" + teacherId + "/vacations";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("teacherId") Long teacherId,
                       @PathVariable("id") Long vacationId,
                       Model model) {
        Teacher teacher = teacherService.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        VacationDto dto = vacationService.findById(vacationId)
                .map(vacationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation not found"));

        model.addAttribute("teacher", teacherMapper.toDto(teacher));
        model.addAttribute("vacationDto", dto);

        return "teachers/vacations/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute VacationDto vacationDto,
                         @PathVariable("teacherId") Long teacherId,
                         @PathVariable("id") Long vacationId,
                         RedirectAttributes redirectAttributes) {
        try {
            Teacher teacher = teacherService.findById(teacherId)
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
            vacationDto.setId(vacationId);
            vacationDto.setTeacher(teacherMapper.toDto(teacher));

            Vacation vacation = vacationMapper.toEntity(vacationDto);

            List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(
                    teacher, vacation.getStartJob(), vacation.getEndJob()
            );

            if (lectures.isEmpty()) {
                vacationService.update(vacation);
            } else {
                return "redirect:/teachers/" + teacherId + "/vacations/lectures?start=" +
                        vacation.getStartJob() + "&end=" + vacation.getEndJob();
            }

        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/teachers/" + teacherId + "/vacations/" + vacationId + "/edit";
        }

        return "redirect:/teachers/" + teacherId + "/vacations";
    }

    @GetMapping("/lectures")
    public String changeTeacherOnLectures(@PathVariable("teacherId") Long teacherId,
                                          @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                          @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                          Model model) {
        Teacher teacher = teacherService.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacher, start, end);

        model.addAttribute("teacher", teacherMapper.toDto(teacher));
        model.addAttribute("lectures", lectures);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "teachers/vacations/lectures";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("teacherId") Long teacherId,
                       @PathVariable("id") Long id,
                       Model model) {
        Teacher teacher = teacherService.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        VacationDto dto = vacationService.findById(id)
                .map(vacationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation not found"));

        model.addAttribute("teacher", teacherMapper.toDto(teacher));
        model.addAttribute("vacationDto", dto);

        return "teachers/vacations/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, @PathVariable("teacherId") Long teacherId) {
        try {
            vacationService.deleteById(id);
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.GONE, ex.getMessage(), ex);
        }
        return "redirect:/teachers/" + teacherId + "/vacations";
    }
}

package org.example.univer.controllers;

import org.example.univer.dto.AudienceDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.AudienceMapper;
import org.example.univer.services.AudienceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/audiences")
@Transactional
public class AudienceController {
    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);
    private AudienceService audienceService;
    private final AudienceMapper audienceMapper;

    public AudienceController(AudienceService audienceService,
                              AudienceMapper audienceMapper) {
        this.audienceService = audienceService;
        this.audienceMapper = audienceMapper;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model, Pageable pageable) {
        Page<AudienceDto> page = audienceService.findAll(pageable).map(audienceMapper::toDto);
        model.addAttribute("title", "All Audiences");
        model.addAttribute("audiences", page);
        logger.debug("Show all audience");
        return "audiences/index";
    }

    /* Обарботка добавления */
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("audienceDto", new AudienceDto());
        model.addAttribute("title", "All Audiences");
        logger.debug("Show create page");
        return "audiences/new";
    }

    @PostMapping
    public String newAudience(@ModelAttribute AudienceDto audienceDto,
                              RedirectAttributes redirectAttributes) {
        try {
            audienceService.create(audienceMapper.toEntity(audienceDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/audiences/new";
        }

        logger.debug("Create new audience. Id {}", audienceDto.getId());
        return "redirect:/audiences";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        AudienceDto dto = audienceService.findById(id)
                .map(audienceMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Audience not found"));
        model.addAttribute("audienceDto", dto);

        logger.debug("Edit audience");
        return "audiences/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("audience") AudienceDto audienceDto,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            audienceDto.setId(id);
            audienceService.update(audienceMapper.toEntity(audienceDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/audiences/edit";
        }

        logger.debug("Show edit page");
        return "redirect:/audiences";
    }

    /* Обарботка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        AudienceDto dto = audienceService.findById(id)
                .map(audienceMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Audience not found"));
        model.addAttribute("audienceDto", dto);
        return "audiences/show";
    }

    /* Обарботка удаления */
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        audienceService.deleteById(id);
        logger.debug("Deleted audience");
        return "redirect:/audiences";
    }
}

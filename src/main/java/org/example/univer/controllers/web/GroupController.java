package org.example.univer.controllers.web;

import jakarta.validation.Valid;
import org.example.univer.dto.GroupDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.GroupMapper;
import org.example.univer.services.CathedraService;
import org.example.univer.services.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
    private GroupService groupService;
    private CathedraService cathedraService;
    private final GroupMapper groupMapper;

    public GroupController(GroupService groupService, CathedraService cathedraService,GroupMapper groupMapper) {
        this.groupService = groupService;
        this.cathedraService = cathedraService;
        this.groupMapper = groupMapper;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model) {
        logger.debug("Show all groups");
        model.addAttribute("title", "All Groups");
        model.addAttribute("groupsDto", groupService.findAll()
                .stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList()));
        return "groups/index";
    }

    /* Обработка добавления */
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("groupDto", new GroupDto());
        model.addAttribute("title", "All Groups");
        model.addAttribute("cathedras", cathedraService.findAll());
        logger.debug("Show create page");
        return "groups/new";
    }

    @PostMapping
    public String newGroup(@ModelAttribute("groupDto") @Valid GroupDto groupDto,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("groupDto", groupDto);
            model.addAttribute("cathedras", cathedraService.findAll());
            return "groups/new";
        }

        try {
            groupService.create(groupMapper.toEntity(groupDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("groupDto", groupDto);
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/groups/new";
        }

        logger.debug("Create new group. Id {}", groupDto.getId());
        return "redirect:/groups";
    }

    /* Обработка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        GroupDto dto = groupService.findById(id)
                .map(groupMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute("groupDto", dto);

        logger.debug("Edit group");
        return "groups/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("groupDto") @Valid GroupDto groupDto,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("groupDto", groupDto);
            model.addAttribute("cathedras", cathedraService.findAll());
            return "groups/edit";
        }

        try {
            groupDto.setId(id);
            groupService.update(groupMapper.toEntity(groupDto));
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("groupDto", groupDto);
            model.addAttribute("errorMessage", e.getMessage());
            return "groups/edit";
        }

        logger.debug("Show edit page");
        return "redirect:/groups";
    }

    /* Обработка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        GroupDto dto = groupService.findById(id)
                .map(groupMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        model.addAttribute("groupDto", dto);
        logger.debug("Edited group");
        return "groups/show";
    }


    /* Обработка удаления */
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        groupService.deleteById(id);
        logger.debug("Deleted group");
        return "redirect:/groups";
    }
}

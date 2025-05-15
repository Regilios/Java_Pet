package org.example.univer.controllers;

import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.models.Group;
import org.example.univer.services.CathedraService;
import org.example.univer.services.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
    private GroupService groupService;
    private CathedraService cathedraService;

    public GroupController(GroupService groupService, CathedraService cathedraService) {
        this.groupService = groupService;
        this.cathedraService = cathedraService;
    }

    /* Общая страница */
    @GetMapping()
    public String index(Model model) {
        logger.debug("Show all groups");
        model.addAttribute("title", "All Groups");
        model.addAttribute("groups", groupService.findAll());
        return "groups/index";
    }

    /* Обарботка добавления */
    @GetMapping("/new")
    public String create(Group group, Model model) {
        model.addAttribute("title", "All Groups");
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute(group);
        logger.debug("Show create page");
        return "groups/new";
    }

    @PostMapping
    public String newGroup(@ModelAttribute("group") Group group, Model model, RedirectAttributes redirectAttributes) {
        try {
            groupService.create(group);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/groups/new";
        }

        logger.debug("Create new group. Id {}", group.getId());
        return "redirect:/groups";
    }

    /* Обарботка изменения */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("cathedras", cathedraService.findAll());
        groupService.findById(id).ifPresentOrElse(group -> {
                    model.addAttribute("group", group);
                    logger.debug("Found and edited group with id: {}", id);
                }, () -> {
                    throw new ResourceNotFoundException("Group not found");
                }
        );

        logger.debug("Edit group");
        return "groups/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("group") Group group, @PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            groupService.update(group);
            } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/groups/edit";
        }

        logger.debug("Show edit page");
        return "redirect:/groups";
    }

    /* Обарботка показа по id */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        groupService.findById(id).ifPresentOrElse(group -> {
                    model.addAttribute("group", group);
                    logger.debug("Found and edited group with id: {}", id);
                }, () -> {
                    throw new ResourceNotFoundException("Group not found");
                }
        );

        logger.debug("Edited group");
        return "groups/show";
    }


    /* Обарботка удаления */
    @DeleteMapping("{id}")
    public String delete(@ModelAttribute Group group) {
        groupService.deleteEntity(group);
        logger.debug("Deleted group");
        return "redirect:/groups";
    }
}

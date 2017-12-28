package org.launchcode.controllers;

import com.sun.javafx.sg.prism.NGShape;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("title", "Menus");
        model.addAttribute("menus", menuDao.findAll());

        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model) {
        model.addAttribute("title", "Add New Menu");
        model.addAttribute(new Menu());
        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddMenuForm(@Valid @ModelAttribute Menu newMenu,
                                     Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add New Menu");
            return "menu/add";
        }

        menuDao.save(newMenu);

        return "redirect:view/" + newMenu.getId();
    }

    @RequestMapping(value = "add-item/{id}", method = RequestMethod.GET)
    public String addItem(@PathVariable(value = "id") int id,
                          Model model) {

        Menu menu = menuDao.findOne(id);
        String titleMessage = "Add item to menu: " + menu.getName();
        AddMenuItemForm menuForm = new AddMenuItemForm(cheeseDao.findAll(), menu);

        model.addAttribute("title", titleMessage);
        model.addAttribute("menuform", menuForm);

        return "menu/add-item";
    }

    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String processAddItemForm(@Valid @ModelAttribute AddMenuItemForm menuForm,
                                     @RequestParam int menuId, @RequestParam int cheeseId,
                                     Errors errors, Model model) {

        Menu menu = menuDao.findOne(menuId);
        String titleMessage = "Add item to menu: " + menu.getName();

        if (errors.hasErrors()) {
            model.addAttribute("title", titleMessage);
            return "menu/add-item";
        }

        Cheese cheeseToAdd = cheeseDao.findOne(cheeseId);
        menu.addItem(cheeseToAdd);

        menuDao.save(menu);

        return "redirect:view/" + menuId;
    }

    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String viewMenu(@PathVariable(value = "id") int id, Model model) {
        Menu menuToDisplay = menuDao.findOne(id);
        List<Cheese> menuCheeses = menuToDisplay.getItems();

        model.addAttribute("title", "Menu Detail");
        model.addAttribute("menu", menuToDisplay);
        model.addAttribute("menuCheeses", menuCheeses);

        return "menu/menu-detail";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Remove Menu");
        return "menu/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] menuIds) {

        for (int menuId : menuIds) {
            menuDao.delete(menuId);
        }

        return "redirect:";
    }
}

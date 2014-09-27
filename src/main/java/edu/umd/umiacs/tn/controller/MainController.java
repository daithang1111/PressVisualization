package edu.umd.umiacs.tn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

	MainController() {

	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView getPages() {

		ModelAndView model = new ModelAndView("index");
		return model;

	}

	@RequestMapping(value = "/learnD3", method = RequestMethod.GET)
	public ModelAndView getLearnD3(ModelMap modelMap) {

		modelMap.addAttribute("title", "A Simple D3 program");

		ModelAndView model = new ModelAndView("learnD3");

		return model;

	}
}
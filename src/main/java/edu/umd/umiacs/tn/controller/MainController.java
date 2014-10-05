package edu.umd.umiacs.tn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping(value = "/visualize", method = RequestMethod.GET)
	public ModelAndView visualize(ModelMap modelMap,
			@RequestParam String senatorName) {

		modelMap.addAttribute("title", "A Simple Visualization for "
				+ senatorName);
		modelMap.addAttribute("senatorName", senatorName);
		ModelAndView model = new ModelAndView("visualize");

		return model;

	}
}
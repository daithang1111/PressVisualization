package edu.umd.umiacs.tn.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.umd.umiacs.tn.model.PressData;
import edu.umd.umiacs.tn.model.Senator;

@Controller
public class PressTopicController {

	List<PressData> data;
	List<Senator> senators;

	PressTopicController() {
		loadSenators();
		loadData();
	}

	@RequestMapping(value = "/press", method = RequestMethod.GET)
	public ModelAndView getPages() {

		ModelAndView model = new ModelAndView("press");
		return model;
	}

	@RequestMapping(value = "/getSenatorName", method = RequestMethod.GET)
	public @ResponseBody
	List<Senator> getSenatorName(@RequestParam String searchTerm) {

		return searchSenatorName(searchTerm);

	}

	@RequestMapping(value = "/getPressData", method = RequestMethod.GET)
	public @ResponseBody
	List<PressData> getPressData(@RequestParam String senatorName) {
		System.out.println("TEST:"+senatorName);
		return searchPressData(senatorName);

	}

	/**
	 * 
	 * @param searchTerm
	 * @return
	 */
	private List<Senator> searchSenatorName(String searchTerm) {

		List<Senator> result = new ArrayList<Senator>();

		// iterate a list and filter by tagName
		for (Senator senator : senators) {
			if (senator.getName().contains(searchTerm)) {
				result.add(senator);
			}
		}

		return result;
	}

	/**
	 * 
	 * @param senatorName
	 * @return
	 */
	private List<PressData> searchPressData(String senatorName) {

		List<PressData> result = new ArrayList<PressData>();

		// iterate a list and filter by tagName
		for (PressData pressData : data) {
			if (pressData.getSenatorName().equalsIgnoreCase(senatorName)) {
				result.add(pressData);
			}
		}

		return result;
	}

	/**
	 * 
	 */
	private void loadSenators() {
		if (senators == null) {
			senators = new ArrayList<Senator>();

		}
		// load actual data from somewhere here
		senators.add(new Senator(1, "akaka"));
		senators.add(new Senator(2, "jimm"));
	}

	/**
	 * 
	 */
	private void loadData() {
		if (data == null) {
			data = new ArrayList<PressData>();
		}

		// load actual data from somewhere here
		data.add(new PressData("July 2014", "akaka", "topic1", 0.5));
		data.add(new PressData("July 2014", "akaka", "topic2", 0.5));
		data.add(new PressData("July 2014", "jimm", "topic1", 0.7));
		data.add(new PressData("July 2014", "jimm", "topic2", 0.3));
	}
}
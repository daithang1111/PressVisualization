package edu.umd.umiacs.tn.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.umd.umiacs.tn.model.Press;
import edu.umd.umiacs.tn.model.PressData;
import edu.umd.umiacs.tn.model.Senator;
import edu.umd.umiacs.tn.model.Topic;
import edu.umd.umiacs.tn.util.DatabaseUtil;

@Controller
public class PressTopicController {

	static List<Senator> senators;

	PressTopicController() {
		loadSenators();
	}

	@RequestMapping(value = "/getSenatorName", method = RequestMethod.GET)
	public @ResponseBody
	List<Senator> getSenatorName(@RequestParam String searchTerm) {

		List<Senator> result = new ArrayList<Senator>();

		// iterate a list and filter by tagName
		for (Senator senator : senators) {
			if (senator.getName().contains(searchTerm)) {
				result.add(senator);
			}
		}

		return result;

	}

	@RequestMapping(value = "/getPressData", method = RequestMethod.GET)
	public @ResponseBody
	List<PressData> getPressData(@RequestParam String senatorName) {
		return DatabaseUtil.getPressData(senatorName);
	}

	@RequestMapping(value = "/getDoc", method = RequestMethod.GET)
	public @ResponseBody
	Press getDoc(@RequestParam String docId) {
		return DatabaseUtil.getDoc(docId);
	}

	@RequestMapping(value = "/getTopic", method = RequestMethod.GET)
	public @ResponseBody
	Topic getTopic(@RequestParam String topicId) {
		return DatabaseUtil.getTopic(topicId);
	}

	/**
	 * 
	 */
	private void loadSenators() {
		if (senators == null) {
			senators = DatabaseUtil.getSenators();
		}
	}
}
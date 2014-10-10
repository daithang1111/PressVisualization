package edu.umd.umiacs.tn.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.umd.umiacs.tn.model.Algorithm;
import edu.umd.umiacs.tn.model.Press;
import edu.umd.umiacs.tn.model.PressData;
import edu.umd.umiacs.tn.model.Senator;
import edu.umd.umiacs.tn.model.SenatorTopic;
import edu.umd.umiacs.tn.model.Topic;
import edu.umd.umiacs.tn.util.DatabaseUtil;

@Controller
public class PressTopicController {

	static List<Senator> senators;
	static List<Algorithm> algorithms;

	PressTopicController() {
		loadSenators();
		loadAlgorithms();
	}

	/**
	 * stat about senator-topic
	 * 
	 * @param algorithmName
	 * @return
	 */
	@RequestMapping(value = "/getSenatorTopic", method = RequestMethod.GET)
	public @ResponseBody
	List<SenatorTopic> getSenatorTopic(@RequestParam String algorithmName) {

		List<SenatorTopic> result = DatabaseUtil.getSenatorTopic(algorithmName);

		return result;

	}

	/**
	 * 
	 * @param modelMap
	 * @param senatorName
	 * @return
	 */
	@RequestMapping(value = "/senatorVis", method = RequestMethod.GET)
	public ModelAndView visualize(ModelMap modelMap,
			@RequestParam String senatorName, @RequestParam String algorithmName) {

		modelMap.addAttribute("senatorName", senatorName);
		modelMap.addAttribute("algorithmName", algorithmName);
		ModelAndView model = new ModelAndView("senatorVis");

		return model;

	}

	/**
	 * Get algorithmName names
	 * 
	 * @param searchTerm
	 * @return
	 */
	@RequestMapping(value = "/getAlgorithmName", method = RequestMethod.GET)
	public @ResponseBody
	List<Algorithm> getSenatorName(@RequestParam String searchTerm) {

		List<Algorithm> result = new ArrayList<Algorithm>();

		// iterate a list and filter by tagName
		for (Algorithm alg : algorithms) {
			if (alg.getName().contains(searchTerm)) {
				result.add(alg);
			}
		}

		return result;

	}

	/**
	 * Get senator/document information
	 * 
	 * @param senatorName
	 * @return
	 */
	@RequestMapping(value = "/getPressData", method = RequestMethod.GET)
	public @ResponseBody
	List<PressData> getPressData(@RequestParam String senatorName,
			@RequestParam String algorithmName) {
		return DatabaseUtil.getPressData(senatorName, algorithmName);
	}

	@RequestMapping(value = "/getDoc", method = RequestMethod.GET)
	public @ResponseBody
	Press getDoc(@RequestParam String docId) {
		return DatabaseUtil.getDoc(docId);
	}

	/**
	 * Get topic distribution TODO: get real distribution from beta with weight
	 * 
	 * @param topicId
	 * @return
	 */
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

	private void loadAlgorithms() {
		if (algorithms == null) {
			algorithms = DatabaseUtil.getAlgorithms();
		}
	}
}
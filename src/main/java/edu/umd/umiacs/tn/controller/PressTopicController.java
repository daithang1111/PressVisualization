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
import edu.umd.umiacs.tn.model.AlgorithmTopic;
import edu.umd.umiacs.tn.model.DocTopic;
import edu.umd.umiacs.tn.model.DocumentId;
import edu.umd.umiacs.tn.model.Press;
import edu.umd.umiacs.tn.model.SDocument;
import edu.umd.umiacs.tn.model.Senator;
import edu.umd.umiacs.tn.model.SenatorTopic;
import edu.umd.umiacs.tn.model.Topic;
import edu.umd.umiacs.tn.util.DatabaseUtil;

@Controller
public class PressTopicController {

	static List<Senator> senators;
	static List<Algorithm> algorithms;
	static List<DocumentId> documentIds;

	PressTopicController() {
		loadSenators();
		loadAlgorithms();
		loadDocumentIds();
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

	@RequestMapping(value = "/getAlgorithmTopic", method = RequestMethod.GET)
	public @ResponseBody
	List<AlgorithmTopic> getAlgorithmTopic(@RequestParam String docId) {

		List<AlgorithmTopic> result = DatabaseUtil.getAlgorithmTopic(docId);

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
	List<Algorithm> getAlgorithmName(@RequestParam String searchTerm) {

		List<Algorithm> result = new ArrayList<Algorithm>();

		// iterate a list and filter by tagName
		for (Algorithm alg : algorithms) {
			if (alg.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
				result.add(alg);
			}
		}

		return result;

	}

	@RequestMapping(value = "/getAlgorithms", method = RequestMethod.GET)
	public @ResponseBody
	List<Algorithm> getAlgorithms() {
		return algorithms;
	}

	@RequestMapping(value = "/getDocumentId", method = RequestMethod.GET)
	public @ResponseBody
	List<DocumentId> getDocumentId(@RequestParam String searchTerm) {

		List<DocumentId> result = new ArrayList<DocumentId>();

		// iterate a list and filter by tagName
		for (DocumentId alg : documentIds) {
			if (alg.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
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
	@RequestMapping(value = "/getDocTopic", method = RequestMethod.GET)
	public @ResponseBody
	List<DocTopic> getPressData(@RequestParam String senatorName,
			@RequestParam String algorithmName) {
		return DatabaseUtil.getDocTopic(senatorName, algorithmName);
	}

	@RequestMapping(value = "/getDocuments", method = RequestMethod.GET)
	public @ResponseBody
	List<SDocument> getDocuments(@RequestParam String senatorId) {
		return DatabaseUtil.getDocuments(senatorId);
	}

	@RequestMapping(value = "/getAllLabels", method = RequestMethod.GET)
	public @ResponseBody
	List<String> getAllLabels() {
		return DatabaseUtil.getUniqLabels();
	}

	@RequestMapping(value = "/updateLabel", method = RequestMethod.GET)
	public @ResponseBody
	List<String> updateLabel(@RequestParam String docId,
			@RequestParam String label) {
		DatabaseUtil.insertLabel(docId, label);
		List<String> output = new ArrayList<String>();
		output.add("success");
		return output;
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
	Topic getTopic(@RequestParam String topicName) {
		return DatabaseUtil.getTopic(topicName);
	}

	@RequestMapping(value = "/removeLabel", method = RequestMethod.GET)
	public @ResponseBody
	List<String> removeLabel() {
		DatabaseUtil.removeLabel();
		List<String> output = new ArrayList<String>();
		output.add("success");
		return output;
	}

	@RequestMapping(value = "/getSenator", method = RequestMethod.GET)
	public @ResponseBody
	List<Senator> getSenator(@RequestParam String searchTerm) {

		List<Senator> result = new ArrayList<Senator>();

		// iterate a list and filter by tagName
		for (Senator alg : senators) {
			if (alg.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
				result.add(alg);
			}
		}

		return result;

	}

	/**
	 * 
	 */
	private void loadSenators() {
		if (senators == null) {
			senators = DatabaseUtil.getSenators();
		}
	}

	/**
	 * 
	 */
	private void loadAlgorithms() {
		if (algorithms == null) {
			algorithms = DatabaseUtil.getAlgorithms();
		}
	}

	private void loadDocumentIds() {
		if (documentIds == null) {
			documentIds = DatabaseUtil.getDocumentIds();
		}

	}
}
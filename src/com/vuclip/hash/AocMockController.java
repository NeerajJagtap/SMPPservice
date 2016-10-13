package com.vuclip.hash;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author SivaLabs
 *
 */

@Controller
public class AocMockController {
	private static final Logger logger = LoggerFactory.getLogger(AocMockController.class);

	@Value("${ip}")
	private String ip;

	@Value("${port}")
	private String port;

	@Autowired
	HashDAO hashDAO;

	@Autowired
	AocMockService aocMockService;

	@Autowired
	private HashDataFormValidator validator;

	// @RequestMapping("/home")
	public String home() {
		return "home";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	// @RequestMapping("/searchCustomer")
	public ModelAndView searchCustomer(@RequestParam(required = false, defaultValue = "") String customerNo) {
		ModelAndView mav = new ModelAndView("showCustomer");
		List<HashData> hashData = hashDAO.searchCustomer(customerNo.trim());
		mav.addObject("SEARCH_HASHDATA_RESULTS_KEY", hashData);
		return mav;
	}

	// @RequestMapping("/viewAllCustomer")
	public ModelAndView getAllCustomer() {
		ModelAndView mav = new ModelAndView("showCustomer");
		List<HashData> hashData = hashDAO.getAllHashData();
		mav.addObject("SEARCH_HASHDATA_RESULTS_KEY", hashData);
		return mav;
	}

	// @RequestMapping(value="/saveCustomer", method=RequestMethod.GET)
	public ModelAndView newuserForm() {
		ModelAndView mav = new ModelAndView("newCustomer");
		HashData hashData = new HashData();
		mav.getModelMap().put("newCustomer", hashData);
		return mav;
	}

	// @RequestMapping(value="/saveCustomer", method=RequestMethod.POST)
	public String create(@ModelAttribute("newCustomer") HashData hashData, BindingResult result, SessionStatus status) {
		validator.validate(hashData, result);
		if (result.hasErrors()) {
			return "newCustomer";
		}
		hashDAO.save(hashData);
		status.setComplete();
		return "redirect:viewAllCustomer.htm";
	}

	// @RequestMapping(value="/updateCustomer", method=RequestMethod.GET)
	public ModelAndView edit(@RequestParam("id") Integer id) {
		ModelAndView mav = new ModelAndView("editCustomer");
		HashData hashData = hashDAO.getById(id);
		mav.addObject("editCustomer", hashData);
		return mav;
	}

	//@RequestMapping(value = "/nds/aoc", method = RequestMethod.GET)
	public ModelAndView getResp(HttpServletRequest request, HttpServletResponse response) {
		// HashData hashData = hashDAO.getByCustomerNo(customerNo);

		String hashString = request.getParameter("hash");
		if (logger.isDebugEnabled()) {
			logger.debug("******************* Welcome AOC Mock ********************");
		}

		System.out.println("Input AOC hash : " + hashString);
		if (logger.isDebugEnabled()) {
			logger.debug("Input AOC hash : " + hashString);
		}

		// AOC flow starts here
		// String endocedAOCString =
		// "4u%2Bz3oOVstniZS7VucmMc9ky0r2iBglcQkxAHudhj5p9jSXjH09mXTnPMLGKDINWYMzLD5r5F95b3VZ5CpKg1RoXI8cEzqDSiXuoqajviQU%3D";
		// String endocedAOCString =
		// "MoO6yxJ2ru4mbjvWVm5IOXxNBbFsMhb2uTCUcTe0N5XYvcxXVi5SUaVzCdZCnG7GDQ81JfZBonN%2B0z2lML6HEIUY6pIVOJxBoPrSfrcfCzQ%3D";
		String finalRespString = aocMockService.aocFlowMock(hashString);
		System.out.println("Encoded Output from AOC : " + finalRespString);
		if (logger.isDebugEnabled()) {
			logger.debug("Encoded Output from AOC : " + finalRespString);
		}

		String redirectionURL = "http://" + ip + ":" + port + "/maxis-viu/billing/handler?resp=" + finalRespString;
		System.out.println("redirectionURL : " + redirectionURL);
		if (logger.isDebugEnabled()) {
			logger.debug("redirectionURL : " + redirectionURL);
		}

		ModelAndView mav = new ModelAndView("redirect:" + redirectionURL);
		return mav;

		// return finalRespString;
	}

	@RequestMapping(value = "/getCustomer", method = RequestMethod.GET)
	public ModelAndView getCustomer(String customerNo) {
		ModelAndView mav = new ModelAndView("editCustomer");
		// HashData hashData = hashDAO.getByCustomerNo(customerNo);

		// AOC flow starts here
		String endocedAOCString = "4u%2Bz3oOVstniZS7VucmMc9ky0r2iBglcQkxAHudhj5p9jSXjH09mXTnPMLGKDINWYMzLD5r5F95b3VZ5CpKg1RoXI8cEzqDSiXuoqajviQU%3D";
		// String endocedAOCString =
		// "MoO6yxJ2ru4mbjvWVm5IOXxNBbFsMhb2uTCUcTe0N5XYvcxXVi5SUaVzCdZCnG7GDQ81JfZBonN%2B0z2lML6HEIUY6pIVOJxBoPrSfrcfCzQ%3D";
		String finalRespString = aocMockService.aocFlowMock(endocedAOCString);
		System.out.println("Encoded Output from AOC : " + finalRespString);
		logger.debug("Encoded Output from AOC : " + finalRespString);

		mav.addObject("editCustomer", finalRespString);
		return mav;
	}

	// @RequestMapping(value="/updateCustomer", method=RequestMethod.POST)
	public String update(@ModelAttribute("editCustomer") HashData hashData, BindingResult result,
			SessionStatus status) {
		validator.validate(hashData, result);
		if (result.hasErrors()) {
			return "editCustomer";
		}
		hashDAO.update(hashData);
		status.setComplete();
		return "redirect:viewAllCustomer.htm";
	}

	// @RequestMapping("deleteCustomer")
	public ModelAndView delete(@RequestParam("id") Integer id) {
		ModelAndView mav = new ModelAndView("redirect:viewAllCustomer.htm");
		hashDAO.delete(id);
		return mav;
	}

}

package com.vuclip.hash;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
public class HashController
{
	@Autowired
	private HashDAO hashDAO;
	
	@Autowired
	private HashDataFormValidator validator;
	
	//@RequestMapping("/home")
	public String home()
	{
		return "home";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) 
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
		
	@RequestMapping("/searchCustomer")
	public ModelAndView searchCustomer(@RequestParam(required= false, defaultValue="") String customerNo)
	{
		ModelAndView mav = new ModelAndView("showCustomer");
		List<HashData> hashData = hashDAO.searchCustomer(customerNo.trim());
		mav.addObject("SEARCH_HASHDATA_RESULTS_KEY", hashData);
		return mav;
	}
	
	@RequestMapping("/viewAllCustomer")
	public ModelAndView getAllCustomer()
	{
		ModelAndView mav = new ModelAndView("showCustomer");
		List<HashData> hashData = hashDAO.getAllHashData();
		mav.addObject("SEARCH_HASHDATA_RESULTS_KEY", hashData);
		return mav;
	}
	
	@RequestMapping(value="/saveCustomer", method=RequestMethod.GET)
	public ModelAndView newuserForm()
	{
		ModelAndView mav = new ModelAndView("newCustomer");
		HashData hashData = new HashData();
		mav.getModelMap().put("newCustomer", hashData);
		return mav;
	}
	
	@RequestMapping(value="/saveCustomer", method=RequestMethod.POST)
	public String create(@ModelAttribute("newCustomer")HashData hashData, BindingResult result, SessionStatus status)
	{
		validator.validate(hashData, result);
		if (result.hasErrors()) 
		{				
			return "newCustomer";
		}
		hashDAO.save(hashData);
		status.setComplete();
		return "redirect:viewAllCustomer.htm";
	}
	
	@RequestMapping(value="/updateCustomer", method=RequestMethod.GET)
	public ModelAndView edit(@RequestParam("id")Integer id)
	{
		ModelAndView mav = new ModelAndView("editCustomer");
		HashData hashData = hashDAO.getById(id);
		mav.addObject("editCustomer", hashData);
		return mav;
	}
	
	@RequestMapping(value="/updateCustomer", method=RequestMethod.POST)
	public String update(@ModelAttribute("editCustomer") HashData hashData, BindingResult result, SessionStatus status)
	{
		validator.validate(hashData, result);
		if (result.hasErrors()) {
			return "editCustomer";
		}
		hashDAO.update(hashData);
		status.setComplete();
		return "redirect:viewAllCustomer.htm";
	}
	
	
	@RequestMapping("deleteCustomer")
	public ModelAndView delete(@RequestParam("id")Integer id)
	{
		ModelAndView mav = new ModelAndView("redirect:viewAllCustomer.htm");
		hashDAO.delete(id);
		return mav;
	}
	
}

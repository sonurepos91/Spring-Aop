package com.example.springaop;

import com.example.springaop.testaop.AfterLog;
import com.example.springaop.testaop.AroundLog;
import com.example.springaop.testaop.BeforeLog;
import com.example.springaop.testaop.ShipmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PocController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PocController.class);

	@Autowired
	private ShipmentService shipmentService;

	//ShipmentService shipmentService = new ShipmentService();

	@AopAspects(allow = true)
	@RequestMapping(method = RequestMethod.POST, value = "/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getJson(@RequestBody OptionHeader optionHeader, HttpServletRequest reqest) {

		shipmentService.doStuff();
		shipmentService.matchJoinPointExecution();

		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(optionHeader);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info(json);
		JSONObject option = new JSONObject(optionHeader);
		LOGGER.info("hii Poc Controller");
		return new ResponseEntity<>("", HttpStatus.OK);

	}

	@BeforeLog(allow = true)
	@RequestMapping(method = RequestMethod.POST,value="/getBeforeAdvice",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getBeforAdvice(@RequestBody OptionHeader optionHeader, HttpServletRequest request){
		System.out.println("Inside Target Method :");
		return new ResponseEntity<>("",HttpStatus.OK);
	}

	@AfterLog(allow = true)
	@RequestMapping(method = RequestMethod.POST,value="/getAfterAdvice",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getAfterAdvice(@RequestBody OptionHeader optionHeader, HttpServletRequest request){
		System.out.println("Inside Target Method :");
		return new ResponseEntity<>("",HttpStatus.OK);
	}

	@AroundLog(allow = true)
	@PostMapping(value = "/getAroundAdvice", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getAroundAdvice(@RequestBody OptionHeader optionHeader, HttpServletRequest request){
		return  new ResponseEntity<>("",HttpStatus.OK);
	}

}

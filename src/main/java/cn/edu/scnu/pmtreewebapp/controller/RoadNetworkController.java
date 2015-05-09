package cn.edu.scnu.pmtreewebapp.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@EnableAutoConfiguration
@Controller
public class RoadNetworkController {
	  @RequestMapping(value = "/rngraph/{graphId}/rn/{rnId}/edit", method = {RequestMethod.POST, RequestMethod.PUT})
	    public String initCreationForm(@PathVariable("graphId") String ownerId, @PathVariable("rnId") String rnId, Model model) { 
	        return "pets/createOrUpdatePetForm";
	    }
}

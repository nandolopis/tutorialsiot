package br.com.anteros.iot.tutorials;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.anteros.iot.app.AnterosIOTService;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.domain.devices.MasterDeviceRPiNode;
import br.com.anteros.iot.domain.plant.PlaceNode;
import br.com.anteros.iot.domain.plant.PlantNode;
import br.com.anteros.iot.domain.things.LampOrBulbNode;
import br.com.anteros.iot.support.Pi4JHelper;
import br.com.anteros.iot.triggers.Trigger;

public class LedRedMqtt implements AnterosIOTServiceListener
{
	public LedRedMqtt() {
		
	}
	
    public static void main( String[] args ) throws JsonProcessingException
    {
        new Thread(new AnterosIOTService("Raspberry", "iot.eclipse.org", "1883", "", "", null, buildConfiguration(), null, null)).start();;
    }

	private static InputStream buildConfiguration() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		PlaceNode placeNode = new PlaceNode("PlaceNode", "Plant Node");
		
		PlantNode plantNode = new PlantNode("PlantNode", "Plant Node");
		
		plantNode.addChildren(placeNode);
		
		/**
		 * MASTER
		 */

		MasterDeviceRPiNode RaspberryMasterNode = new MasterDeviceRPiNode("Raspberry", "Central");
		
		LampOrBulbNode ledRed = new LampOrBulbNode("LedRed", "Led Red", Pi4JHelper.GPIO_01);
		
		placeNode.addChildren(RaspberryMasterNode,ledRed);
		
		
		
		RaspberryMasterNode.addThingsToController(ledRed);
		
		
		System.out.println(mapper.writeValueAsString(plantNode));
		
		return new ByteArrayInputStream(mapper.writeValueAsBytes(plantNode));
	}

	

	public void onFireTrigger(Trigger source, Object value) {
		throw new RuntimeException();	
	}

	public void onAddSubTypeNames(ObjectMapper mapper) {
		// TODO Auto-generated method stub
		
	}

}

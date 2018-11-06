package br.com.anteros.iot.tutorials;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pi4j.temperature.TemperatureScale;

import br.com.anteros.iot.app.AnterosIOTService;
import br.com.anteros.iot.app.listeners.AnterosIOTServiceListener;
import br.com.anteros.iot.domain.devices.MasterDeviceRPiNode;
import br.com.anteros.iot.domain.plant.PlaceNode;
import br.com.anteros.iot.domain.plant.PlantNode;
import br.com.anteros.iot.domain.things.RingStripLED12Node;
import br.com.anteros.iot.domain.things.TemperatureOneWireNode;
import br.com.anteros.iot.support.colors.RGB;
import br.com.anteros.iot.things.test.LEDDisplayType;
import br.com.anteros.iot.triggers.Trigger;

/**
 * Led com Anteros 
 *
 */
public class App implements AnterosIOTServiceListener
{
	public App() {
		
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
		
		RGB color = new RGB(0, 255, 0);
		
		RingStripLED12Node ringStripLED12Node = new RingStripLED12Node("LED", "LED anel", 10, LEDDisplayType.COLOUR_WIPE, 20,color );

		
		placeNode.addChildren(RaspberryMasterNode,ringStripLED12Node);
		
		
		
		RaspberryMasterNode.addThingsToController(ringStripLED12Node);
		
		
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

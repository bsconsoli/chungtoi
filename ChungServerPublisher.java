/**
 * @file ChungServerPublisher.java
 * @author Bernardo Scapini Consoli
 * @date November 2017
 * 
 * @section DESCRIPTION
 * 
 * Publishes the endpoint of the ChungServer web service. 
 * 
 */

package chungtoi;

import javax.xml.ws.Endpoint;

public class ChungServerPublisher {

	public static void main(String[] args){
		Endpoint.publish("http://" + args[0] + ":9876/chungtoi", new ChungServerImpl());
	}
}

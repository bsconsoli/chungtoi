package chungtoi;

import javax.xml.ws.Endpoint;

public class ChungServerPublisher {

	public static void main(String[] args){
		Endpoint.publish("http://127.0.0.1:9876/chungtoi", new ChungServerImpl());
	}
}
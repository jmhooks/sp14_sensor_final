package project.sensors.hooks_gateway;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;

import project.sensors.hooks_gateway.Gateway;

public class Gateway {

	XBee xbee = new XBee();

	public Gateway() throws Exception{
		try {
			xbee.open("COM1", 9600);

			while (true) {

				try {
					// we wait here until a packet is received.
					//stuff happens
					XBeeResponse response = xbee.getResponse(10);

					if (response.getApiId() == ApiId.ZNET_RX_RESPONSE) {
						// we received a packet from ZNetSenderTest.java
						ZNetRxResponse rx = (ZNetRxResponse) response;
						System.out.println("got a packet");
						System.out.println(rx.getRemoteAddress64());
						
						int[] data = rx.getData();
						//System.out.println(data[0] + "," + data[1]);
						int reading = data[0] | (data[1] << 8);
						System.out.println(reading);
						
						String httpResponse = Request
								.Get("http://localhost/addMoteData.php?"+
									  "password=penguin&"+
										"sensor_value="+reading + "&"+
									  "radio_address="+URLEncoder.encode(rx.getRemoteAddress64().toString(), "UTF-8"))
								.execute().returnContent().asString();
						System.out.println(httpResponse);
						
					} else {

					}
				} catch (Exception e) {

				}
				
				//pole server for status of each of the motes
				String httpResponse = Request
						.Get("http://localhost/getMoteData.php?")								
						.execute().returnContent().asString();
				//System.out.println(httpResponse);
				//Thread.sleep(1000);
				JsonParser parser = new JsonParser();
				JsonElement f = parser.parse(httpResponse);
				JsonObject j = f.getAsJsonObject();
				System.out.println(j.get("success").getAsBoolean());
				JsonArray motes = j.get("data").getAsJsonArray();
				for(int i=0; i<motes.size(); i++){
					String values =  motes.get(i).getAsJsonObject().get("sensor_value").getAsString();
				}
			}
		} finally {
			if (xbee.isConnected()) {
				xbee.close();
			}
		}
	}
/*
	public void sendDataToServer() {
		try {
			System.out.println("hello");
			String response = Request
					.Get("http://sensornetworks.engr.uga.edu/sp14/examples/getMoteData.php?mote_id=1")
					.execute().returnContent().asString();
			JsonParser parser = new JsonParser();
			JsonElement f = parser.parse(response);
			JsonObject j = f.getAsJsonObject();
			System.out.println(j.get("success").getAsBoolean());

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("hello2");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("hello3");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("hello4");
		}

	}
	*/

	public static void main(String[] args) throws Exception {

		new Gateway();
	}
}

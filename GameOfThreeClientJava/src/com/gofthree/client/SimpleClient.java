package com.gofthree.client;

import java.util.Scanner;
import java.util.concurrent.Future;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SimpleClient {
	
	public static final String SEED = "SEED";
	public static final String NOSEED = "NOSEED";
	public static final String VOID = "VOID";
	
	public SimpleClient(){
	}
	
	/**
	 * Method register user into game (GET)
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String getSEEDfromClient(String user) throws Exception{
		
		String outputData = "";
		
		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client.target("http://localhost:8080/app/gengine/"+user);
		
		Invocation.Builder reqBuilder = target.request();
		AsyncInvoker asyncInvoker = reqBuilder.async();
		Future<Response> futureResp = asyncInvoker.get(); //--> blocking response
		
		Response response = futureResp.get();
		outputData = response.readEntity(String.class);
		
		return outputData;	
	}
	
	/**
	 * Method generate SEED (POST)
	 * @param user
	 * @return
	 * @throws Exception
	 */
	
	public String postSEEDtoClients(String user) throws Exception{
		
		String outputData = "";
		
		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client.target("http://localhost:8080/app/gengine/"+user);
		
		Invocation.Builder reqBuilder = target.request();
		AsyncInvoker asyncInvoker = reqBuilder.async();
		
		GenericType<Response> generic = new GenericType<Response>(Response.class);
		Entity entity = Entity.entity("SEED", MediaType.TEXT_PLAIN_TYPE);
		
		Response response = asyncInvoker.post(entity,generic).get(); 
		outputData = response.readEntity(String.class);
		
		return outputData;	
		
		
		
	}
	
	
	
	public static void main (String [ ] args) throws Exception {
		
		SimpleClient sC = new SimpleClient();
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("======= GAME OF THREE CONSOLE INI ===============");
		System.out.println("1/->put your user here: ");
		String user = keyboard.nextLine();
		String isFirstPlayer = sC.getSEEDfromClient(user);
		
		if (isFirstPlayer.equalsIgnoreCase(SEED)){
			System.out.println("USER :" + user + " is the first player. Need to launch SEED");
		}else if (isFirstPlayer.equalsIgnoreCase(NOSEED)){
			System.out.println("USER: " + user + "is the second player. System waiting for SEED of player one");
		}else{
			System.out.println("USER: " + user+ " cannot play with this game. Only for two players. Bye!");
			System.exit(0);
		}
		
		boolean DONE = true;
		
		if (isFirstPlayer.equalsIgnoreCase(SEED)){
			System.out.println("2/->is first player. generating SEED");	
			int i=0;
			do{
				isFirstPlayer = sC.postSEEDtoClients(user);
				i++;
			}while((isFirstPlayer.equalsIgnoreCase(NOSEED)) && (i<10));
			
			if (i>=10){
				System.out.println("Cannot generate SEED");
			}else{
				System.out.println("System response (" + isFirstPlayer + ")");
			}
		}
		System.out.println("======= GAME OF THREE CONSOLE END ===============");
		System.exit(0);
	}
	
}

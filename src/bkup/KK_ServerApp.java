package bkup;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * KK_ServerApp is the application logic for the main KnockKnock application.
 * This starts the KnockKnock application, sets up the listening port, the 
 * serverSockets for accepting the client connections and the application GUI
 * object. 
 * 
 * @author notroot
 *
 */


public class KK_ServerApp {

	private KK_ServerAppGUI serverGUI;
	private SimpleDateFormat timeDateStamp;
	private int port = 4444;
	private boolean listenForConnections;
	private ServerSocket serverSocket = null;
	
	/**
	 * KK_ServerApp constructor that receive the port to listen to for connection as parameter in console
	 * 
	 * @param port server side listening TCP port
	 * @param serverGUI setup serverGUI object with the connection logic 
	 * 
	 */
	 
	public KK_ServerApp(int port, KK_ServerAppGUI serverGUI) {	
		
		this.serverGUI = serverGUI;
		this.port = port;
		timeDateStamp = new SimpleDateFormat("HH:mm:ss");
	}
	
	/**
	 * startKnockKnock() starts by setting listening tag to ensure the
	 * continuous while loop maintains a listening state for incoming connections
	 * 
	 */

	public void startKnockKnock() {
			
		listenForConnections = true;
		
		// create socket server and wait for connection requests
		try 
		{
			// the socket used by the server
			serverConnection();
			
			int clientConnectCount = 0;

			// infinite loop to wait for connections
			while(listenForConnections) 
			{
							
				if (clientConnectCount == 0) {
					// format message saying we are waiting
					display("Knock Knock server listening on port " + port + " ");
				}
				else {
					// format message saying we are waiting
					display("Knock Knock client connected on port " + port + " ");
				}
				clientConnectCount++;

				
				new KK_ServerMultiThread(serverSocket.accept()).start();
				
				// if I was asked to stop
				if(!listenForConnections)
					break;
				
			}
			// Stop the server, close the serverSocket
			try {
				serverSocket.close();

			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		// something went wrong
		catch (IOException e) {
            String message = timeDateStamp.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(message);
		}
	}
	/**
	 * Sets up the socket connection for the application server, listens on port 4444
	 * 
	 *
	 * @throws IOException if any input/output connection issuse
	 */
	public void serverConnection() throws IOException {

		try {
			serverSocket = new ServerSocket(port); 	

		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(-1);
		}
	}
	
	/**
	 * For the GUI to stop the server. Creates a port to listen back to itself. I know 
	 * this is not the best way to do this, but for the purposes of the assignment 
	 * deadline served a purpose.
	 *  
	 */
	@SuppressWarnings("resource")
	protected void stop() {
		listenForConnections = false;
		// connect to myself as Client to exit statement 
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Display an event (not a message) to the event window, includes
	 * a timestamp when the even occurred.
	 * @param message - displayed in the event window
	 */
	private void display(String message) {
		String time = timeDateStamp.format(new Date()) + " " + message;
		if(serverGUI == null)
			System.out.println(time);
		else
			serverGUI.appendEvent(time + "\n");
	}

}





import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * KK_MultiServerThread was supplied as part of this assignment.
 * This sets up a threads to run for the BufferedReader and PrintWriter
 * for connecting and sending stream data across a network.
 * @author notroot
 *
 */

public class KK_ServerMultiThread extends Thread {
	private Socket socket = null;
	
	/**
	 * Constructor creates a multi-threaded server application
	 * using buffered stream PrintWriter and BufferedReader to 
	 * accept input from the socket receiving the output stream
	 * 
	 * @param socket creates a server socket connection
	 */
	public KK_ServerMultiThread(Socket socket) {
		super("KKMultiServerThread");
		this.socket = socket;
	}
	
	/**
	 * This thread was constructed using a separate Runnable 
	 * run object, the Runnable object's run method is called
	 * to create the input and output stream buffers
	 * 
	 */

	public void run() {

		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream()));

			String inputLine, outputLine;
			KK_ServerProtocol kkProtocol = new KK_ServerProtocol();
			outputLine = kkProtocol.processInput(null);
			out.println(outputLine);

			while ((inputLine = in.readLine()) != null) {
				outputLine = kkProtocol.processInput(inputLine);
				out.println(outputLine);
				if (outputLine.equals("Bye"))
					break;
			}
			out.close();
			in.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

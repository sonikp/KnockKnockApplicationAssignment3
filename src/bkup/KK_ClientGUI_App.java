package bkup;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * KK_ClientGUI_App creates the clients that connect to the server
 * TCP port and interface with the server streams for playing the KnockKnock
 * game
 * @author notroot
 *
 */


public class KK_ClientGUI_App extends JFrame  {	
    
	private static final long serialVersionUID = 1L; 
	// added: The serializable class KK_ClientGUI_App does not declare a static final serialVersionUID field of type long
	
	private Socket kkSocket = null;
	private PrintWriter writeOutput = null;
	private BufferedReader readInput = null;
	private BufferedReader strInput = null;
	private String fromServer;
	private String fromUser;
	private JTextField responseField;
	private JTextArea displayArea;
	private boolean shutdownClient;
	private String shutdownServer;
	private Thread serverThread;
	
	private final ExecutorService clientProcessingPool;
	
	
	/**
	 * Constructor creates the client thread pool which will 
	 * hold and execute the interactions as threads. The constructor 
	 * created the swing GUI interface for sending and receiving the
	 * required responses to the KnockKnock game.
	 * 
	 * @throws IOException for any input / output issues setting up the client constructor
	 */
    public KK_ClientGUI_App() throws IOException {

    	super("Super Knock-Knock Client");
    	
    	clientProcessingPool = Executors.newCachedThreadPool();
    	
    	// client response field, starts off with the obvious answer:
		responseField = new JTextField("who's there?");
		responseField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
								
				try {
					displayArea.append("Client: " + e.getActionCommand() + "\n"); 
					writeOutput.println(responseField.getText());
					displayArea.append("Server: " + readInput.readLine() + "\n");			
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				responseField.setText("");	// clears response field ready for user input				
			}
		});
		
		add(responseField, BorderLayout.SOUTH);
		
		displayArea = new JTextArea();
		add(new JScrollPane(displayArea));
    
        
        // showing the window on the screen
		setSize(400, 300);
        setVisible(true);
        setLocation(1100, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    	connectToServer();   	
    }
    
    /**
     * Method to connect to the server. These values are hard coded, and it
     * connects to the server running on the local host to the listening
     * TCP port 4444
     * 
     * @throws IOException for any connection issues to the streams
     */
    
    public void connectToServer() throws IOException {
    	
        try {
            kkSocket = new Socket("127.0.0.1", 4444);
            writeOutput = new PrintWriter(kkSocket.getOutputStream(), true);
            readInput = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: localhost.");
            System.exit(1);
        }
    	
    }
    
    /**
     * The getJoke() connects reads from and writes to the stream buffers
     * When a connection is made to the server, the server instantly responds
     * with 'Knock Knock'. The connection reads that string and displays 
     * it to the client applications. The responses are also read from 
     * the keyboard an transmitted back as a response to the server-side application.
     * 
     * @throws IOException for any connection issues communicating to server
     */
    public void getJoke() throws IOException {
    	
    	strInput = new BufferedReader(new InputStreamReader(System.in));

    	while ((fromServer = readInput.readLine()) != null) {
    		System.out.println("Server: " + fromServer);
    		displayArea.append("Server: " + fromServer + "\n");
    		if (fromServer.equals("Bye.")) {
    			closeConnection();
    			break;
    		}
    			
    		fromUser = strInput.readLine();		
    		if (fromUser != null) {
    			System.out.println("Client: " + fromUser);
    			displayArea.append("Client: " + fromUser  + "\n");
    			writeOutput.println(fromUser);
    		}   
    	}
    }
    /**
     * closeConnection() method closes the connection by closing
     * the write output buffer, and the input read buffers. Finally
     * the socket is also closed. 
     * 
     * @throws IOException for any issues closing the buffers, streams, sockets
     */
    public void closeConnection() throws IOException {
        writeOutput.close();
        readInput.close();
        strInput.close();
        kkSocket.close();
        
    }
	
    /**
     * This allows the client to be executed independently of the 
     * main KnockKnock application. The server must be running and
     * listening on the port for this client to be able to connect
     * and access the application.
     * 
     * @param args startup arguments, none required
     * @throws IOException throws and exception if there are any connection issues
     */
	public static void main(String[] args) throws IOException {
		
		KK_ClientGUI_App client = new KK_ClientGUI_App();
		client.setLocation(1500,100);
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.setVisible(true);

    }
	
	
	/**
	 * startClient() implements the Runnable interface which implements 
	 * the clients connecting as a thread.
	 * 
	 * @throws IOException Throws exception if unable to connect a client
	 * 
	 */
	public void startClient() throws IOException {

		Runnable clientTask = null;

		if (!shutdownClient) {

			clientTask = new Runnable() {

				@Override
				public void run() {
					try {
						
						KK_ClientGUI_App client = new KK_ClientGUI_App();
						client.setLocation(1500,100);
						client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						client.setVisible(true);

					} catch (IOException e) {
						System.err.println("Unable to process client request");
						e.printStackTrace();
					}
				}
			};
		}

		serverThread = new Thread(clientTask);
		serverThread.start();
	} 	
}

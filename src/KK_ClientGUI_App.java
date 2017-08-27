
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



public class KK_ClientGUI_App extends JFrame  {	
    
	private static final long serialVersionUID = 1L; // added: The serializable class KK_ClientGUI_App does not declare a static final serialVersionUID field of type long
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
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	connectToServer();
    	getJoke();
    	
    }
    
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
    
    public void closeConnection() throws IOException {
        writeOutput.close();
        readInput.close();
        strInput.close();
        kkSocket.close();
    }
	
	public static void main(String[] args) throws IOException {
		
		KK_ClientGUI_App client = new KK_ClientGUI_App();
		client.setLocation(1500,100);
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.setVisible(true);

    }

	
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
		else {
			System.out.println("!!!enter shutdown mode, shutdownServer = " + shutdownServer);
			
//			clientProcessingPool.shutdown();
			
			try {

				clientProcessingPool.shutdown();
//				closeConnections();
				System.out.println("replaced serverSocket.close()");

//				try {
////					serverSocket.close();
//				}
//				catch (SocketException ex) {
//					System.out.println("enter printstacktrace()");
//					ex.printStackTrace();
//				}

			}
//			catch (SocketException ex) {
//				ex.printStackTrace();
//			}
			finally {
				
				// currently empty
			}
		}

		serverThread = new Thread(clientTask);
		serverThread.start();

	} 
	public void closeConnections() throws IOException {
		
		System.out.println("closeConnections()");
//		writeOutput.close();
//		readInput.close();
//		strInput.close();
//		kkSocket.close();
//		System.exit(0);
	
//		serverSocket.close();
	}
	
}

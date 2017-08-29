package bkup;



import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;



/**
 * UCSD Java IV - Assignment 3
 * 
 * This application is a client/server multi-threaded application
 * that connects clients to the server using sockets. The application
 * starts with an application panel that allows a user to start/stop
 * the server component. The server starts by creating a serverSocket
 * listening on a TCP port for clients to connect. This also creates a 
 * thread pool which allows the clients to connect.
 * 
 */


public class KnockKnock_Application_Main_START extends JFrame {
	

	private static final long serialVersionUID = 1L; 
	// added: The serializable class KK_App_Main_START does not declare a static final serialVersionUID field of type long
	
	// application objects
	private KK_ServerAppGUI serverapp; 
	private KK_ClientGUI_App kkclientapp;
	
	// swing components starting the Knock Knock server application
	private final JPanel startServerJPanel;
	private final JButton startServerJButton;
	private final JButton stopServerJButton;

	// swing components for starting the Knock Knock client application
	private final JPanel startClientThreadJPanel;
	private final JButton startClientJButton;
	private final JButton stopClientJButton;	
	
	private boolean shutdownClient;
	private Thread clientThread;
	private List<KK_ClientGUI_App> connectedClientList;
	private List<Thread> clientThreadList;
	
	
	/**
	 * Constructor KnockKnock_Application_Main_START()
	 * This constructor extends JFrame and sets up the 
	 * application interface used to start and stop all
	 * the application components. The KnockKnock Application
	 * consists of server and client components. This launch
	 * application allows a user to start/stop the server and 
	 * client applications.
	 * 
	 * @throws IOException throws exception if error connecting to stream
	 * 
	 */
	

	public KnockKnock_Application_Main_START() throws IOException {
		
		super("KnockKnock Launch Application");
		
		connectedClientList = new ArrayList<KK_ClientGUI_App>();
		clientThreadList = new ArrayList<Thread>();
		
		startServerJPanel = new JPanel(new GridLayout(2, 2, 5, 5));
		startServerJButton = new JButton("Start Server");
		stopServerJButton = new JButton("Stop Server");
		
		startClientThreadJPanel = new JPanel(new GridLayout(2, 2, 5, 5));
		startClientJButton = new JButton("Start Client");
		stopClientJButton = new JButton("Stop All Clients");	
		
		setLayout(new GridLayout(2, 1, 10, 10));
		
		// disable buttons until server is started
		stopServerJButton.setEnabled(false);
		startClientJButton.setEnabled(false);
		stopClientJButton.setEnabled(false);
		
		// add GUI components to the SwingWorker
		startServerJPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Launch Server"));
		startServerJButton.addActionListener(new ActionListener() {
			
			// start the serverside application
			@Override
			public void actionPerformed(ActionEvent e) {
				
				stopServerJButton.setEnabled(true);
				startClientJButton.setEnabled(true);
				stopClientJButton.setEnabled(true);
				
				serverapp = new KK_ServerAppGUI(4444);
				serverapp.serverStartStop();				
			}
		});	// end anonymous inner class, and end call to addActionListener
		
		stopServerJButton.addActionListener(new ActionListener() {
			
			// stop the serverside application
			@Override
			public void actionPerformed(ActionEvent e) {
			
				startClientJButton.setEnabled(false);
				stopClientJButton.setEnabled(false);
				serverapp.serverStartStop();				
			}
		});	// end anonymous inner class, and end call to addActionListener
		
		startServerJPanel.add(startServerJButton);
		startServerJPanel.add(stopServerJButton);
		
		// add GUI components to the event-dispatch thread panel
		startClientThreadJPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Launch Clients"));
		startClientJButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				
				// starts client application
				try {
					startClient();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
		}); // end anonymous inner class, and end call to addActionListener
		
		stopClientJButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {

				// stops and closes all the client applications
				stopClients();

			}
		}); // end anonymous inner class, and end call to addActionListener
		
		startClientThreadJPanel.add(startClientJButton);
		startClientThreadJPanel.add(stopClientJButton);
		
		add(startServerJPanel);
		add(startClientThreadJPanel);
		setSize(275, 200);
		setVisible(true);
	}	// end constructor
	
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
						
						// creates the client GUI object
						kkclientapp = new KK_ClientGUI_App();

						// adds the client to the object arrayList
						connectedClientList.add(kkclientapp);

						// communicates with the server to get the jokes
						kkclientapp.getJoke();

					} catch (IOException e) {
						System.err.println("Unable to process client request");
						e.printStackTrace();
					}
				}
			};
		}
		
		clientThread = new Thread(clientTask) {
			
			// Anonymous inner class, call to interrupt to close out each of the client objects
			public void interrupt () {
				// step through the list of clients and kill each one, completing the entire list
				for( KK_ClientGUI_App app : connectedClientList ) {
					
					app.dispose(); 
					
				}
			}
		};	// end anonymous inner class
		
		// starts the client thread
		clientThread.start();
		// adds each client thread to the thread arrayList
		clientThreadList.add(clientThread);
	} 
	
	/**
	 * stopClients() stops all the clients by stepping through the arraylist
	 * using the enhanced for loop method and calling interrupt on each thread object
	 */
	public void stopClients() {
		
		for (Thread cThread : clientThreadList) {
			cThread.interrupt();			
		}		
	}
	
	/**
	 * Main Method 'STARTS' the execution of the KnockKnock Application.
	 * The entire application consists of the application component, 
	 * server and client applications. The execution of the main KnockKnock
	 * Application allows a user to start/stop the server and client applications
	 * and to play this fun and exhilarating KnockKnock game!
	 * 
	 * @param args Main Method command line arguments, none implemented
	 * @throws IOException Throws IOException if stream cannot be written to or closed.
	 */
	public static void main(String[] args) throws IOException {
		
		KnockKnock_Application_Main_START application = new KnockKnock_Application_Main_START();
		application.setLocationRelativeTo(null);
		application.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}	// end class 
























import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
//import javax.swing.JLabel;
//import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

import java.io.IOException;
//import java.net.SocketException;
//import java.util.concurrent.ExecutionException;


public class KK_App_Main_START extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	private Thread serverThread;
	
	// constructor
	public KK_App_Main_START() throws IOException {
		
		super("KnockKnock Launch Application");
		
		startServerJPanel = new JPanel(new GridLayout(2, 2, 5, 5));
		startServerJButton = new JButton("Start Server");
		stopServerJButton = new JButton("Stop Server");
		
		startClientThreadJPanel = new JPanel(new GridLayout(2, 2, 5, 5));
		startClientJButton = new JButton("Start Client");
		stopClientJButton = new JButton("Stop Client");	//Logout
		
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}); // end anonymous inner class, and end call to addActionListener
		
		stopClientJButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("Client STOP button pressed");
				System.out.println(event);	
				
				// starts client application
//				try {
//					
//					shutdownClient = true;
//					startClient();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

				

			}
		}); // end anonymous inner class, and end call to addActionListener
		
		startClientThreadJPanel.add(startClientJButton);
		startClientThreadJPanel.add(stopClientJButton);
		
		add(startServerJPanel);
		add(startClientThreadJPanel);
		setSize(275, 200);
		setVisible(true);
	}	// end constructor
	
	
	public void startClient() throws IOException {

		Runnable clientTask = null;

		if (!shutdownClient) {

			clientTask = new Runnable() {

				@Override
				public void run() {
					try {
						
						kkclientapp = new KK_ClientGUI_App();

					} catch (IOException e) {
						System.err.println("Unable to process client request");
						e.printStackTrace();
					}
				}
			};


		}
		else {
			System.out.println("!!!enter shutdown mode, shutdownServer = " );	//+ shutdownServer
			
//			clientProcessingPool.shutdown();
			
			try {
				
				kkclientapp.closeConnection();

//				clientProcessingPool.shutdown();
//				threadServerController.closeConnections();
//				System.out.println("replaced serverSocket.close()");

//				try {
////					serverSocket.close();
//				}
//				catch (SocketException ex) {
//					System.out.println("enter printstacktrace()");
//					ex.printStackTrace();
//				}

			}
			finally {
				
				// currently empty
			}
		}

		serverThread = new Thread(clientTask);
		serverThread.start();

	} 
	
	// main method begins
	public static void main(String[] args) throws IOException {
		
		KK_App_Main_START application = new KK_App_Main_START();
		application.setLocationRelativeTo(null);
		application.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}	// end class 


















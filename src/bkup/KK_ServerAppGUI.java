package bkup;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * KK_ServerAppGUI is the UI interface for the main KnockKnock application
 * used for start/stop of the server and the client application components
 * 
 * 
 * @author notroot
 *
 */


public class KK_ServerAppGUI extends JFrame implements ActionListener {	
	

	private static final long serialVersionUID = 1L; 
	// added: The serializable class KK_ServerAppGUI does not declare a static final serialVersionUID field of type long
	
	// swing components
	private JButton stopStart;
	private JTextArea event;	
	private JPanel north;
	
	// server object
	private KK_ServerApp server;
	private int portKK = 4444;
	
	/**
	 * Constructor for the application sets up the hard-coded TCP port of 4444. 
	 * Also this sets up the GUI layout and the buttons for launching the
	 * server and client components.
	 * 
	 * @param port hard-coded server side TCP port 4444 for connecting clients
	 */
	
	
	// server constructor that receive the port to listen to for connection as parameter
	KK_ServerAppGUI(int port) {
		super("KK Server");
		System.out.println("ServerGUI constructor: " + port);
		knockKnockServer();

	}	
	
	/**
	 * knockKnockServer() sets up all the GUI layouts, and the
	 * buttons for the KnockKnock application.
	 */
	
	public void knockKnockServer() {
		server = null;

		// in the NorthPanel contains the server-side Start and Stop buttons
		north = new JPanel();
		north.add(new JLabel("Start/Stop Server: "));
		
		// to stop or start the server, we start with "Start"
		stopStart = new JButton("Start");
		stopStart.addActionListener(this);
		north.add(stopStart);
		add(north, BorderLayout.NORTH);
		
		// the event panel
		JPanel eventPanel = new JPanel(new GridLayout(1,1));	
		event = new JTextArea();	
		event.setEditable(false);
		appendEvent("Knock Knock Events log.\n");
		eventPanel.add(new JScrollPane(event));	
		add(eventPanel);
		
		setSize(450, 400);	
		setLocation(360, 400);
		setVisible(true);

	}
	
	// append event message to the JTextArea at the end position
	void appendEvent(String str) {
		event.append(str);
		event.setCaretPosition(event.getText().length() - 1);	
		
	}
	/**
	 * Action performed when start or stop where clicked.
	 * If running we have to stop.
	 */
	public void actionPerformed(ActionEvent e) {
		
		serverStartStop();
	}
	
	/**
	 * The mechanism for starting /  stopping the service. 
	 * If starting creates a new server object
	 * If stopping calls the stop() method.
	 */
	public void serverStartStop() {
		
		if(server != null) {
			server.stop();
			server = null;
			stopStart.setText("Start");
			return;
		}

		// create a new Server
		server = new KK_ServerApp(portKK, this);		
		// and start it as a thread
		new ServerRunning().start();
		stopStart.setText("Stop");		
		
	}
	
	/**
	 * Server threads for running the KnockKnock server in a thread
	 * 
	 * @author notroot
	 *
	 */


	class ServerRunning extends Thread {
		public void run() {
			server.startKnockKnock();
			// the server failed
			stopStart.setText("Start");
			appendEvent("Server stopped, no longer listening\n");
			server = null;
		}
	}

}




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




public class KK_ServerAppGUI extends JFrame implements ActionListener {	
	

	private static final long serialVersionUID = 1L; // added: The serializable class KK_ClientGUI_App does not declare a static final serialVersionUID field of type long
	// swing components
	private JButton stopStart;
	private JTextArea event;	
	private JPanel north;
	// server object
	private KK_ServerApp server;
	private int portKK = 4444;
	
	
//	private JTextField serverTextField;	// inputs message from user
//	private JTextArea displayAreaServer;
	
	
	// server constructor that receive the port to listen to for connection as parameter
	KK_ServerAppGUI(int port) {
		super("KK Server");
		System.out.println("ServerGUI constructor: " + port);
		knockKnockServer();

	}	
	
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
		setVisible(true);

	}
	
	// append event message to the JTextArea at the end position
	void appendEvent(String str) {
		event.append(str);
		event.setCaretPosition(event.getText().length() - 1);	
		
	}
	
	// start or stop where clicked
	public void actionPerformed(ActionEvent e) {
		// if running we have to stop
		serverStartStop();
	}
	
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
	

	// server thread
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


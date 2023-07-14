package com.qadri;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client {
	
	JFrame jFrame = new JFrame("Chat Application");
	JTextField sendMessageTextField = new JPlaceholderTextField("Enter Message..");
	JTextArea messageTextArea = new JTextArea(8,40);
	BufferedReader bufferedReader;
	PrintWriter pWriter;

	@SuppressWarnings("serial")
	public class JPlaceholderTextField extends JTextField {

		private String ph;

		public JPlaceholderTextField(String ph) {
			this.ph = ph;
		}
	
		public JPlaceholderTextField() {
			this.ph = null;
		}

		/**
		 * Gets text, returns placeholder if nothing specified
		 */
			@Override
			public String getText() {
				String text = super.getText();

			if (text.trim().length() == 0 && ph != null) {
				text = ph;
			}

			return text;
			}

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				if (super.getText().length() > 0 || ph == null) {
					return;
				}
		
				Graphics2D g2 = (Graphics2D) g;

				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(super.getDisabledTextColor());
				g2.drawString(ph, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
			}
	}
	
	public Client() {
		sendMessageTextField.setEditable(true);
		messageTextArea.setEditable(false);
		jFrame.getContentPane().add(sendMessageTextField, "North");
		jFrame.getContentPane().add(messageTextArea , "Center");
		jFrame.pack();
	
		sendMessageTextField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pWriter.println(sendMessageTextField.getText());
				sendMessageTextField.setText("");
			}
		}
				
		) ;
		
	}
	
	public void connect() {
		String serverAddress = JOptionPane.showInputDialog(jFrame, "Enter the server IP: ", 
				"Connect to the server", JOptionPane.QUESTION_MESSAGE
				);
		try {
			Socket socket = new Socket(serverAddress, 4444);
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pWriter = new PrintWriter(socket.getOutputStream(), true);
			
			while(true) {
				String line = bufferedReader.readLine();
				if(line.startsWith("SUBMITNAME")) {
					String name = JOptionPane.showInputDialog(jFrame, "Enter your name: ", 
							"Name Selection", JOptionPane.PLAIN_MESSAGE);
					pWriter.println(name);
				} else if(line.startsWith("NAMEACCEPTED")) {
					sendMessageTextField.setEditable(true);
				}else if (line.startsWith("MESSAGE")) {
					messageTextArea.append(line.substring(7) + "\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		client.jFrame.setVisible(true);
		client.connect();
	}

}

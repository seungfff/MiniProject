package Test;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MyFrame extends JFrame {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	public MyFrame() {
		setFont(new Font("Dialog", Font.PLAIN, 10));
		setBackground(Color.WHITE);
		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		getContentPane().add(textField, BorderLayout.CENTER);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		getContentPane().add(textField_1, BorderLayout.SOUTH);
		textField_1.setColumns(10);
		
		JTextArea textArea = new JTextArea();
		getContentPane().add(textArea, BorderLayout.WEST);
		
		textField_2 = new JTextField();
		getContentPane().add(textField_2, BorderLayout.EAST);
		textField_2.setColumns(10);
		
		JTextPane txtpnChatting = new JTextPane();
		txtpnChatting.setText("Chatting");
		getContentPane().add(txtpnChatting, BorderLayout.NORTH);
	}

}

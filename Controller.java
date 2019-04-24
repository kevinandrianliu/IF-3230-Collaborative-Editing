import java.util.Scanner;
import java.util.UUID;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.tugasbesar.networking.SenderThread;
import com.tugasbesar.classes.CRDT;
import com.tugasbesar.classes.CharacterData;
import com.tugasbesar.networking.ReceiverThread;

public class Controller {
	public static void main (String[] args){
		if (args.length != 2){
			System.out.println("Usage: Controller <ip> <port>");
			System.exit(1);
    }
    
    // Creating text area
    JTextArea textArea = new JTextArea();

		String ip = args[0];
    int port = Integer.parseInt(args[1]);
    String computerId = UUID.randomUUID().toString().substring(0, 6);
    
    // Broadcasting computer ID until user press enter
    // Creating computer ID JFrame
    JFrame computerIdFrame = new JFrame("Scanning for computers. . .");
    computerIdFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    computerIdFrame.setSize(500, 300);
    
    JButton button = new JButton("Continue");
    JTextArea tArea = new JTextArea();
    tArea.setEditable(false);
    tArea.insert("Your computer ID: " + computerId + "\n\n", 0);
    tArea.insert("Scanned computer IDs: \n", tArea.getText().length());

    ReceiverThread receiverThread = new ReceiverThread(ip, port, textArea, tArea, computerId);
    SenderThread senderThread = new SenderThread(ip,port);
    senderThread.SetComputerId(computerId);
    
    senderThread.start();
    receiverThread.start();
    
    // Creating JFrame
    JFrame frame = new JFrame("CRDT-based Text Editor");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 600);

    // Creating menu bar
    JMenuBar menuBar = new JMenuBar();
    JMenu menuBarFile = new JMenu("File");
    JMenu menuBarHelp = new JMenu("Help");
    menuBar.add(menuBarFile);
    menuBar.add(menuBarHelp);
    JMenuItem menuItemFileOpen = new JMenuItem("Open");
    JMenuItem menuItemFileSaveAs = new JMenuItem("Save as");
    menuBarFile.add(menuItemFileOpen);
    menuBarFile.add(menuItemFileSaveAs);

    textArea.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        handleEvent(e);
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        handleEvent(e);
      }

      private void handleEvent(DocumentEvent e){

      }
    });
    textArea.addKeyListener(new KeyListener(){
    
      @Override
      public void keyTyped(KeyEvent e) {
        
      }
    
      @Override
      public void keyReleased(KeyEvent e) {
        
      }
    
      @Override
      public void keyPressed(KeyEvent e) {
        char value = e.getKeyChar();
        int position = 0;
        String order = null;

        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
          if (textArea.getCaretPosition() > 0){
            position = textArea.getCaretPosition() - 1;
            order = "DELETE";
          }
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE){
          if (textArea.getCaretPosition() < textArea.getText().length()){
          position = textArea.getCaretPosition();
          order = "DELETE";
          }
        } else if (
          (e.getKeyCode() == KeyEvent.VK_UP) ||
          (e.getKeyCode() == KeyEvent.VK_DOWN) ||
          (e.getKeyCode() == KeyEvent.VK_LEFT) ||
          (e.getKeyCode() == KeyEvent.VK_RIGHT)
        ) {
          // DO nothing
        } else if (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED){
          e.consume();
        } else {
          position = textArea.getCaretPosition();
          order = "INSERT";
        }
        
        if (order != null){
          synchronized(senderThread){  
            if (order.equals("INSERT")){
              senderThread.CreateCharacter(computerId, value, position, UUID.randomUUID().toString(), order);
  
              senderThread.notify();
              try {
                senderThread.wait();
              } catch (InterruptedException ex) {
                System.out.println("Sender thread interrupted on maint thread");
              }
            } else if (order.equals("DELETE")){
              CRDT crdt = receiverThread.getCrdt();
              if (crdt.getCharacterDataCRDT().size() > 0){
                CharacterData characterData = crdt.getCharacterData(position);
                senderThread.CreateCharacter(computerId, '0', -1, characterData.getPositionId(), order);
  
                senderThread.notify();
                try {
                  senderThread.wait();
                } catch (InterruptedException ex) {
                  System.out.println("Sender thread interrupted on maint thread");
                }
              }
            }
          }
        }
      }
    });

    // Add text area scroll pane
    JScrollPane textAreaScrollPane = new JScrollPane(textArea);

    // Adding Components to the frame.
    frame.getContentPane().add(BorderLayout.NORTH, menuBar);
    frame.getContentPane().add(BorderLayout.CENTER, textAreaScrollPane);

    button.addActionListener(new ActionListener(){
    
      @Override
      public void actionPerformed(ActionEvent e) {
        computerIdFrame.dispose();
        frame.setVisible(true);

        senderThread.isConnecting = false;
        receiverThread.isConnecting = false;
      }
    });
    computerIdFrame.getContentPane().add(BorderLayout.CENTER, tArea);
    computerIdFrame.getContentPane().add(BorderLayout.SOUTH, button);

    computerIdFrame.setVisible(true);
	}
}
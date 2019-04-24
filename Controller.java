import java.util.Scanner;
import java.util.UUID;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
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
		ReceiverThread receiverThread = new ReceiverThread(ip, port, textArea);
		SenderThread senderThread = new SenderThread(ip,port);
    senderThread.SetComputerId(computerId);

    senderThread.start();
    receiverThread.start();
    
    // Broadcasting computer ID until user press enter
    Scanner scanner = new Scanner(System.in);
    System.out.println("Broadcasting and receiving computer IDs in the area. Founded computer IDs will be listed below.");
    System.out.println("Press Enter to stop and continue . . .");
    scanner.nextLine();
    senderThread.isConnecting = false;
    receiverThread.isConnecting = false;
    
    // Creating JFrame
    JFrame frame = new JFrame("CRDT-based Text Editor");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 400);

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
        //System.out.println(textArea.getCaretPosition());
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
            //System.out.println("BKSP" + (textArea.getCaretPosition() - 1));
            position = textArea.getCaretPosition() - 1;
            order = "DELETE";
          }
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE){
          if (textArea.getCaretPosition() < textArea.getText().length()){
          // System.out.println("DEL" + textArea.getCaretPosition());
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
          // System.out.println(e.getKeyChar());
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
    frame.setVisible(true);
	}
}
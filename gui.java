import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
class gui {
  public static void main(String args[]) {
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

    // Creating text area
    JTextArea textArea = new JTextArea();

    // Add special case for backspace button
    // InputMap inputMap = textArea.getInputMap(JComponent.WHEN_FOCUSED);
    // ActionMap actionMap = textArea.getActionMap();

    // String bkspKey = (String) inputMap.get(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0));
    // Action originalBkspAction = actionMap.get(bkspKey);
    
    // Action newBkspAction = new BkspAction(originalBkspAction);
    // actionMap.put(bkspKey, newBkspAction);

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
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
          if (textArea.getCaretPosition() > 0){
            System.out.println("BKSP" + (textArea.getCaretPosition() - 1));
          }
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE){
          System.out.println("DEL" + textArea.getCaretPosition());
        } else if (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED){
          e.consume();
        } else {
          System.out.println(e.getKeyChar());
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

  // private static class BkspAction extends AbstractAction {
  //   private Action originalBkspAction;
  //   public boolean isBackspacePressed;
  
  //   public BkspAction(Action originalBkspAction) {
  //     this.originalBkspAction = originalBkspAction;
  //     isBackspacePressed = false;
  //   }
  
  //   @Override
  //   public void actionPerformed(ActionEvent e) {
  //     isBackspacePressed = true;
  
  //     if (originalBkspAction != null){
  //       originalBkspAction.actionPerformed(e);
  //     }
  //   }
  // }
}
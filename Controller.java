import java.util.Scanner;
import java.util.UUID;

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

		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		ReceiverThread receiverThread = new ReceiverThread(ip, port);
		SenderThread senderThread = new SenderThread(ip,port);

		senderThread.start();
		receiverThread.start();
		
    Scanner scanner = new Scanner(System.in);
    CRDT crdt = receiverThread.getCrdt();
		while (true){
			synchronized(senderThread){
        System.out.print("Enter text: ");
        String line = scanner.nextLine();
        String[] words = line.split(" ");

        if (words.length != 4){
          crdt = receiverThread.getCrdt();
          crdt.printCrdt();
        } else {
          String computerId = words[0];
          char value = words[1].charAt(0);
          int position = Integer.parseInt(words[2]);
          String order = words[3];
          
          if (order.equals("INSERT")){
            senderThread.CreateCharacter(computerId, value, position, UUID.randomUUID().toString(), order);

            senderThread.notify();
            try {
              senderThread.wait();
            } catch (InterruptedException e) {
              System.out.println("Sender thread interrupted on maint thread");
            }
          } else if (order.equals("DELETE")){
            if (crdt.getCharacterDataCRDT().size() > 0){
              System.out.println("YO");
              CharacterData characterData = crdt.getCharacterData(position);
              senderThread.CreateCharacter(computerId, '0', -1, characterData.getPositionId(), order);

              senderThread.notify();
              try {
                senderThread.wait();
              } catch (InterruptedException e) {
                System.out.println("Sender thread interrupted on maint thread");
              }
            }
          }
        }
      }
		}
	}
}
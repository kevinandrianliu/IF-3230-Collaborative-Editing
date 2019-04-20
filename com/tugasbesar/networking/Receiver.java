package com.tugasbesar.networking;

import com.tugasbesar.classes.Character;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Receiver {
  private final short BUFFER_SIZE = 1024 * 6;
  private MulticastSocket socket;
  private InetAddress ipAddressGroup;
  private int port;

  public Receiver(String ip, int port) {
    this.port = port;
    
    // Try connecting to socket and bind to multicast address
    try {
      socket = new MulticastSocket(port);
      ipAddressGroup = InetAddress.getByName(ip);
      socket.joinGroup(ipAddressGroup);
    } catch (UnknownHostException e) {
      System.out.println("ERROR IP address is unknown or invalid: " + e.getLocalizedMessage());
    } catch (IOException e) {
      System.out.println("ERROR Cannot create and/or bind socket: " + e.getLocalizedMessage());
    }
    
  }

  public void receiveMessage() {
    while (true){
      // Receive object
      System.out.println("[Receiving Object...]");
      byte[] buffer = new byte[BUFFER_SIZE];
      try {
        socket.receive(new DatagramPacket(buffer, BUFFER_SIZE, ipAddressGroup, port));
      } catch (IOException e) {
        System.out.println("ERROR Cannot receive data: " + e.getLocalizedMessage());
      }
      
      // Deserialize
      ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
      try {
        ObjectInputStream ois = new ObjectInputStream(bais);
      
        Character character = (Character) ois.readObject();
        System.out.println(character.toString());
      } catch (IOException e) {
        System.out.println("ERROR Cannot create ObjectInputStream or object is corrupted: " + e.getLocalizedMessage());
      } catch (ClassNotFoundException e){
        System.out.println("ERROR Declared class does not exist.");
      }
    }
  }
}
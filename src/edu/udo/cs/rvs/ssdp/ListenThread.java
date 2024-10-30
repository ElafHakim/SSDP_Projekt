package edu.udo.cs.rvs.ssdp;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;

/**
 * dieser Thread soll ein MulticastSocket auf Port 1900 öffnen, der
 * Multicast-Gruppe „239.255.255.250“ beitreten und bis zum Programmende endlos Datagramme
 * empfangen und dem Worker-Thread zur Verfügung stellen
 * Die Methode run() öffnet ein MulticastSocket, bindet es an Port 1900 und tritt der Multicast-Gruppe bei. Sie empfängt Datagramme
 * und fügt diese einer Liste hinzu.
 * @throws IOException Tritt ein wenn der Port oder die Addresse in Multicast nicht gefunden wurden.
 * @author Hakim, Fistok
 */

public class ListenThread implements Runnable {


	public MulticastSocket multicastSocket;
	

	final int port = 1900; // Standardport für SSDP.
    final String hostIPv4 = "239.255.255.250"; // Die Multicast-Adresse in IPv4 lautet diese IP.

	private DatagramPacket datagramPacket; // DatagramPacket für empfangene Daten
	public  LinkedList<DatagramPacket> dgPktList = new LinkedList<DatagramPacket>();


// Der Konstruktor Listen() initialisiert das MulticastSocket, bindet es an den Port 1900 und tritt der Gruppe bei.

	public ListenThread() throws IOException  {
   
        this.multicastSocket = new MulticastSocket(port);
       this.multicastSocket.joinGroup(InetAddress.getByName(hostIPv4));
    }

	@Override
	public void run() {

		while (!this.multicastSocket.isClosed()) { // Solange der Socket nicht geschlossen ist



			
			try {
				 // Buffer für empfangene Datagramme erstellen
				 byte[] buffer = new byte[this.multicastSocket.getReceiveBufferSize()];// die Größe des MulticastSocketes nehmen, zu wissen wie viel Byte Dategrammpackete haben 
				 datagramPacket = new DatagramPacket(buffer, buffer.length);

				 multicastSocket.receive(datagramPacket);

			
				synchronized (dgPktList) {
				dgPktList.add(datagramPacket);
			}

		} catch (IOException e1) {
			System.out.println("Das Emfangen von Datagramme hat fehlgeschlagen , " + e1);
			e1.printStackTrace();

		}
			
		}
	}

}

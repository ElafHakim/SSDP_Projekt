package edu.udo.cs.rvs.ssdp;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;

/**
 * Dieser Thread soll die Nutzereingaben lesen, verarbeiten und entsprechende Aktionen durchführen.
 * Er führt es die Befehle Scan, der über die Pakete sucht. List-Befehl,
 * der die UUID und Service-Typ ausgibt . Clear-Befehl entfernt die alle bekannten Geräte .
 * Exit-Befehl beendet sich das Programm.
 * @throws IOException . Der Thread soll dieses Exception werfen , insbesonders im Scan-Befehl wenn die Addresse Fehler hat
 * @author Hakim, Fistok
 */
public class UserThread implements Runnable {
    //Attribute, die Informationen über die anderen Klassen liefern

    ListenThread listenThread  = null;
    WorkerThread WorkerThread = null;
    ServiceIdentifier srvId = new ServiceIdentifier();


    public UserThread (ListenThread listenThread, WorkerThread workerThread) {

        this.listenThread = listenThread;
        //this.WorkerThread = WorkerThread;
        
    }

    @Override
    public void run() {
        //User can enter anything with scanner to get the commands.

        try (Scanner input = new Scanner(System.in)) {
            String input2;

            while (true) {
                System.out.println("\nWelchen Befehl wollen Sie ausführen:\n SCAN\n LIST\n CLEAR\n EXIT\n");
                input2 = input.nextLine();
                


                 if(input2.equals("EXIT")){
                     System.out.println("Programm  endet");

                     System.out.println("++++++++++++++++++++");

                     System.exit(0);
                 }

                 else if(input2.equals("SCAN")){
                     try {
                         scan();
                         System.out.println("++++++++++++++++++++");
                     }catch (Exception e){
                         e.printStackTrace();
                     }
                 }

                 else if(input2.equals("LIST")){
                     list_Befehl();

                     System.out.println("++++++++++++++++++++");
                 }

                 else if(input2.equals("CLEAR")){
                     clear();
                     System.out.println("Alle Geräte gelöscht.");
                     System.out.println("++++++++++++++++++++");
                 }

                 else {
                    
                     System.out.println("Von Ihnen wurde nichts eingegeben. ");
                     System.out.println("++++++++++++++++++++");

                 }
                }
        }
    }


 /**
 * Diese Methode leert die Liste deviceList und gibt eine Bestätigung aus.
 */
    public void clear() {
    // Synchronisierung auf die deviceList, um Thread-Sicherheit zu gewährleisten
    synchronized (WorkerThread.deviceList) {
        
       if (!WorkerThread.deviceList.isEmpty()) {
            // Leeren der Liste
            WorkerThread.deviceList.clear();
            // Ausgabe zur Bestätigung
            System.out.println("Die Liste ist leer!");
        } else {
            System.out.println("Die Liste war schon leer.");

    }
    }
    }

   // Überprüfung und Ausgabe der in der zweiten Liste gespeicherten Geräte:
    public void list_Befehl() {

        try {

            if (!WorkerThread.deviceList.isEmpty()){
                System.out.println("Die verfügbare Geräte ");

            synchronized (WorkerThread.deviceList) {

                for (int index = 0; index < WorkerThread.deviceList.size(); index++) {

                    System.out.println("UUID: " + WorkerThread.deviceList.get(index).uuid + " , Type: " + WorkerThread.deviceList.get(index).serviceType);

                }
            }
        }else{
                System.out.println("Keine Geraete gefunden!");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void scan() throws IOException {

        String Suchanfrage
                = "M-SEARCH * HTTP/1.1\r\n"
                + "S: uuid:" + UUID.randomUUID() + "\r\n"
                + "HOST: 239.255.255.250:1900\r\n"
                + "MAN: \"ssdp:discover\"\r\n"
                + "ST: ssdp:all\r\n"
                + "\r\n"; // leere Zeile schließt die Such-Anfrage ab.

        byte[] data = Suchanfrage.getBytes(StandardCharsets.UTF_8);
        DatagramPacket datagramPacketNeu = new DatagramPacket(data, data.length, InetAddress.getByName("239.255.255.250"), 1900);

        try {
            listenThread.multicastSocket.send(datagramPacketNeu);
            System.out.println("SSDP empfängt ... \n");

        } catch (Exception ex) {
            System.out.println(" Empfangsfehler aufgetreten ");
            ex.printStackTrace();
        }

    }
}

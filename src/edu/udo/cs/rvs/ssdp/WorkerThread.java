package edu.udo.cs.rvs.ssdp;

//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;

import java.io.IOException;
//import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.UUID;

/**
 * dieser Thread soll die empfangenen Datagramme aus dem Listen-Thread
 * verarbeiten und dem User-Thread mitteilen, welche Geräte gerade Dienste im Netzwerk anbieten.
 * Er soll zuerst das einzelne Pakett mit den Schemataen vergleichen, und dann nimmt sowohl die UUID als auch den Service-type
 * Dies funktioniert, indem die Methode split teilt
 * Und dann speichert es in einer neue Liste, die muss in Klasse SpeID speichern .
 *
 * @author Asakrah, Majadly, Qeadan
 * @throws IOException Tritt ein wenn ein Fehler in Objekt von Klasse Listen vorkommt.
 */

public class WorkerThread implements Runnable {

    ListenThread listenThread; // Objekt von SSDPPEER(LIST-THREAD);
    UserThread userThread = new UserThread(listenThread, this);

    LinkedList<ServiceIdentifier> deviceList = new LinkedList<ServiceIdentifier>();

    //UUID  uuid;
    String type;

    public WorkerThread(ListenThread listenThread) throws IOException {
        //listenThread = listenThread;
    }


    @Override
    public void run() {

        while (true) {

            try {

                DatagramPacket dp; // der zu überprüfende DatagrammPacket

                if (!listenThread.dgPktList.isEmpty()) {

                    synchronized (listenThread.dgPktList) {

                        dp = listenThread.dgPktList.pop();
                    }

                    String str = new String(dp.getData(), StandardCharsets.UTF_8);
                    String[] parts = str.split("\r\n");
                    String[] arr = parts;
                    ServiceIdentifier srvId = new ServiceIdentifier();


                    try {

                        if (arr[0].equals("HTTP/1.1 200 OK")) {
                            for (int index = 0; index < arr.length; index++) {
                                if (arr[index].startsWith("ST: ")) {
                                    type = arr[index].substring(4);
                                } else if (arr[index].startsWith("USN: ")) {

                                    try {
                                        // Deklaration der UUID außerhalb des try-Blocks
                                        UUID uuid = UUID.fromString(arr[index].split("uuid:", 2)[1].split(":", 2)[0]);

                                        // Setze die UUID auf srvId, nachdem sie erfolgreich erstellt wurde
                                        srvId.uuid = uuid; // Hier setzen wir die UUID direkt
                                    } catch (Exception e) {
                                        System.out.println("Es gibt kein UUID");
                                        e.printStackTrace();
                                    }
                                }
                            }

                            // Verschieben der Zuweisung von serviceType außerhalb des Blocks
                            srvId.serviceType = type;
                            deviceList.add(srvId);
                        } else if (arr[0].startsWith("NOTIFY * HTTP/1.1")) {// Wenn die Zeile mit "USN: " angefangen hat, ist es (Multicast)

                            for (int index = 0; index < arr.length; index++) {

                                if (arr[index].startsWith("NT: ")) {
                                    type = arr[index].substring(4);

                                } else if (arr[index].startsWith("USN: ")) {
                                    try {
                                        // Deklaration der UUID außerhalb des try-Blocks
                                        UUID uuid = UUID.fromString(arr[index].split("uuid:", 2)[1].split(":", 2)[0]);


                                        // Setze die UUID auf srvId, nachdem sie erfolgreich erstellt wurde
                                        srvId.uuid = uuid; // Hier setzen wir die UUID direkt
                                    } catch (Exception e) {
                                        System.out.println("Es gibt kein UUID");
                                        e.printStackTrace();
                                    }


                                }
                            }

                            srvId.serviceType = type;

                            deviceList.add(srvId); // nimmt den uuid und srvTy; von Klasse SpeID und speichert es in der neuen Liste neuList.


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Thread.sleep(10);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }

}
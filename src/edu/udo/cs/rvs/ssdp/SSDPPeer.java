package edu.udo.cs.rvs.ssdp;

//import java.io.*;
//import java.net.*;

/**
 * das Simple Service Discovery Protocol (SSDP) wird häufig in UPnP-Anwendungen verwendet.
 * Diese Klasse wird zunächst beim Programmstart instanziiert und nur wenn sie die Runnable implementiert,
 * wird ein Thread erstellt und gestartet.
 */
public class SSDPPeer implements Runnable {
	
	ListenThread listentThread;
    WorkerThread workerThread;
	UserThread userThread;


    public SSDPPeer() {

        try {
			listentThread  = new ListenThread();
            workerThread = new WorkerThread(listentThread);
            userThread = new UserThread(listentThread, workerThread);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

	@Override
	public void run() {

		

		Runnable peerRunnable = (Runnable) listentThread;
		Runnable peerRunnable1 = (Runnable) workerThread;
		Runnable peerRunnable2 = (Runnable) userThread;

		Thread listenThread = new Thread(peerRunnable);
		Thread workerThread = new Thread(peerRunnable1);
		Thread userThread = new Thread(peerRunnable2);

		listenThread.setName("Listen Thread");
		listenThread.start();

		workerThread.setName("Worker Thread");
		workerThread.start();

		userThread.setName("User Thread");
		userThread.start();
	}

	

}


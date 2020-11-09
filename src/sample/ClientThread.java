package sample;

import javafx.application.Application;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientThread extends Thread {

    private Controller controller;

    public ClientThread() {
    }

    public ClientThread( Controller controller) {
        super("ClientThread");

        this.controller = controller;
    }

    public void run() {

        Platform.runLater(new Runnable () {
            @Override
            public void run() {
                try {
                    controller.sendMessageToLabel("GroupChat","", controller.getClient().getName());
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
package sample;

import javafx.application.Platform;

import java.io.IOException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class ChatClient  extends UnicastRemoteObject implements ChatClientInt{

    private String name;
    private Controller ui;
    public ChatClient (String n) throws RemoteException {
        name=n;
    }

    public void tell(String st) throws RemoteException{
        System.out.println(st);
        Platform.runLater( () -> {
            try {
                ui.sendMessageToLabel(st);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public String getName() throws RemoteException{
        return name;
    }

    public void setGUI(Controller t){
        ui=t ;
    }
}
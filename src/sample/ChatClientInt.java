package sample;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClientInt extends Remote {
    public void tell (String groupchat, String message, String sender) throws RemoteException;
    public String getName()throws RemoteException ;
}
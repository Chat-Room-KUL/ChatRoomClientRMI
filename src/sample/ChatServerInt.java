package sample;

import java.rmi.*;
import java.util.*;

public interface ChatServerInt extends Remote{
    public boolean login (ChatClientInt a) throws RemoteException ;
    public void publish (String receiver, String message, String Sender) throws RemoteException ;
    public Vector getConnected() throws RemoteException ;
    public void deleteClient(String name) throws RemoteException;
    public boolean checkIfNameExists(String answer) throws RemoteException;
    public List<String> getAllTheUsers() throws RemoteException;
}
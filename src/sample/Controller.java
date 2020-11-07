package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

public class Controller implements Observer {

    private ChatClient client;
    private ChatServerInt server;
    private OnlineUsersList onlineUsersList;

    @FXML
    private ListView<String> onlineUsers;

    private ObservableList<String> items = FXCollections.observableArrayList();

    @FXML
    private Label chatBox;

    @FXML
    private TextField messageInput;

    @FXML
    private Button sendMessageButton;

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Update gedetecteerd");
        System.out.println(onlineUsersList.getOnlineUsers());
        onlineUsers.getItems().clear();
        onlineUsers.setItems(items);
        List<String> users = onlineUsersList.getOnlineUsers();
        for(int i=0;i<users.size();i++){
            items.add(users.get(i));
        }
    }

    public Label getLabel() {
        return chatBox;
    }

    public ChatClient getClient() {
        return this.client;
    }

    public void doConnect(String clientName){
        try{
            client = new ChatClient(clientName);
            client.setGUI(this);
            server = (ChatServerInt) Naming.lookup("rmi://localhost:1099/myabc");
            server.login(client);
            //updateUsers(server.getConnected());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendMessage(ActionEvent actionEvent) throws RemoteException {
        String st = messageInput.getText();
        messageInput.clear();
        //sendMessageToLabel(st);
        System.out.println(st);
        server.publish(st);
    }

    public void sendMessageToLabel(String controller) throws IOException {
        String bestaatAl = chatBox.getText();
        bestaatAl = bestaatAl + "\n" + controller;
        chatBox.setText(bestaatAl);
        if (bestaatAl.endsWith("has just connected.") || bestaatAl.endsWith("has just disconnected.")) {
            updateOnlineUsers();
        }
    }

    public void deleteMyUsername(String name) throws RemoteException {
        server.deleteClient(name);
    }

    public boolean checkName(String answer) throws RemoteException, NotBoundException, MalformedURLException {
        server = (ChatServerInt) Naming.lookup("rmi://localhost:1099/myabc");
        if (server.checkIfNameExists(answer)) {
            return true;
        }
        else {
            return false;
        }
    }


    // USERLIST METHODS

    public void removeOnlineUser(String name) {
        onlineUsersList.removeOnlineUser(name);
    }

    public void addOnlineUser(String name) {
        onlineUsersList.addOnlineUser(name);
    }

    public void setOnlineUsersList(OnlineUsersList onlineUsers){
        this.onlineUsersList = onlineUsers;
        System.out.println("Toekennen");
    }


    public void updateOnlineUsers() throws IOException {
        List<String> onlineUsers = new ArrayList<String>();
        onlineUsers.add("GroupChat");
        onlineUsers = server.getAllTheUsers();
        System.out.println(onlineUsers);
        onlineUsersList.setOnlineUsers(onlineUsers);
    }

}

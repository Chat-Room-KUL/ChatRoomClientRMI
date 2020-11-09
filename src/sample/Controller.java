package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    private List<ChatRoom> chatRooms = new ArrayList<ChatRoom>();
    private String currentActiveChatRoom = "GroupChat";


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

    public synchronized void doConnect(String clientName){
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
        List<String> onlineUsers2 = server.getAllTheUsers();
        for (int i = 0; i < onlineUsers2.size(); i++) {
            onlineUsers.add(onlineUsers2.get(i));
        }
        System.out.println(onlineUsers);
        onlineUsersList.setOnlineUsers(onlineUsers);
    }

    // CHATROOM METHODS

    public void addChatRoom(ChatRoom chatroom) {
        chatRooms.add(chatroom);
    }

    public void addMessageToChatRoom(String chatName, String message) {

        //Find chat with appropriate name
        boolean chatroomFound = false;
        ChatRoom chatroom = null;
        int i = 0;
        while(!chatroomFound && chatRooms.size()!=i){
            System.out.println("Zoeken naar chatroom");
            System.out.println(chatName);
            System.out.println(chatRooms.get(i).getChatRoomName());
            System.out.println(chatRooms.get(i).getChatRoomName().equals(chatName));
            if(chatRooms.get(i).getChatRoomName().equals(chatName)){
                chatroom = chatRooms.get(i);
                chatroomFound = true;
                chatroom.addMessage(message);
                System.out.println(message);

            }
            i++;
        }

        if(!chatroomFound) {
            chatroom = new ChatRoom(chatName);
            this.addChatRoom(chatroom);
            chatroom.addMessage(message);
        }

        System.out.println(chatroom.toString());
        if(chatName.equals(currentActiveChatRoom)){
            chatBox.setText(chatroom.toString());
        }

    }

    public void createPrivateChat(MouseEvent mouseEvent) {
        String name = onlineUsers.getSelectionModel().getSelectedItem();
        System.out.println("clicked on " + onlineUsers.getSelectionModel().getSelectedItem());
        ChatRoom currentChatRoom = new ChatRoom();

        boolean chatRoomExists = false;
        int i=0;
        while(!chatRoomExists && i != chatRooms.size()){
            if(chatRooms.get(i).getChatRoomName().equals(name)){
                chatRoomExists = true;
                currentChatRoom = chatRooms.get(i);
            }
            System.out.println(currentChatRoom);
            i++;
        }

        if(!chatRoomExists){
            currentChatRoom = new ChatRoom(name);
            this.addChatRoom(currentChatRoom);
        }
        currentActiveChatRoom = name;
        chatBox.setText(currentChatRoom.toString());
        System.out.println("After clicking, the current active chatroom is: " + currentActiveChatRoom);
    }

    public void sendMessage(ActionEvent actionEvent) throws RemoteException {
        String st = messageInput.getText();
        messageInput.clear();
        System.out.println("The current active chatroom is: " + currentActiveChatRoom + " En de sender is: " + client.getName());
        server.publish(currentActiveChatRoom, client.getName() + ": " + st, client.getName());
        this.addMessageToChatRoom(currentActiveChatRoom, client.getName() + ": " + st);
    }

    public void sendMessageToLabel(String groupchat, String message, String sender) throws IOException {
        System.out.println(groupchat);
        if(groupchat.equals("GroupChat")) {
            this.addMessageToChatRoom("GroupChat", message);
        } else {
            //currentActiveChatRoom = sender;
            this.addMessageToChatRoom(sender, message);
        }

        if(!currentActiveChatRoom.equals(client.getName())){
            System.out.println("Send Message");
            System.out.println(currentActiveChatRoom);
            System.out.println(message);
        }

        for (int i = 0; i < chatRooms.size(); i++) {
            if (chatRooms.get(i).getChatRoomName().equals("GroupChat")) {
                if (chatRooms.get(i).returnLastMessage().endsWith(" has just connected.") || chatRooms.get(i).returnLastMessage().endsWith(" has just disconnected.")) {
                    updateOnlineUsers();
                }
            }
        }
    }
}

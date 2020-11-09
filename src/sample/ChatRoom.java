package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ChatRoom extends Observable {

    //In private chats the chatRoomName is the username of the other users
    private String chatRoomName;
    private List<String> messages = new ArrayList<>();

    public ChatRoom(){

    }

    public ChatRoom(String name){
        chatRoomName = name;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message){
        messages.add(message);
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public String toString(){
        String chatBoxString = "";
        StringBuffer string = new StringBuffer();

        for(int i=0; i<messages.size();i++){
            string.append(messages.get(i)+"\n");
        }
        chatBoxString = string.toString();

        return chatBoxString;
    }

    public String returnLastMessage() {
        return messages.get(messages.size()-1);
    }
}

package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class OnlineUsersList extends Observable {

    private List<String> onlineUsers = new ArrayList<>();

    public OnlineUsersList(List<String> onlineUsers){
        this.onlineUsers = onlineUsers;
    }

    public OnlineUsersList(){

    }

    public List<String> getOnlineUsers(){
        return onlineUsers;
    }

    public void setOnlineUsers(List<String> onlineUsers){
        this.onlineUsers = onlineUsers;
        setChanged();
        notifyObservers();
    }

    public void removeOnlineUser(String name){
        onlineUsers.remove(name);
        setChanged();
        notifyObservers();
    }

    public void addOnlineUser(String name) {
        onlineUsers.add(name);
        setChanged();
        notifyObservers();
    }
}

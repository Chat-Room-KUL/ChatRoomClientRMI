package sample;

import com.sun.security.ntlm.Client;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.rmi.RemoteException;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = (Parent)loader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        Controller controller = loader.getController();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                try {
                    controller.deleteMyUsername(controller.getClient().getName());
                    System.exit(0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        //Username creation and check
        boolean usernameChosen = false;
        String dialogHeader = "Choose your username";

        String userName = null;

        while(!usernameChosen) {
            TextInputDialog dialog = new TextInputDialog();

            // inlezen van de usernaam
            dialog.initOwner(primaryStage);
            dialog.setTitle("Welcome");
            dialog.setHeaderText(dialogHeader);
            dialog.setContentText("Username");
            dialog.setGraphic(null);

            try {
                userName = dialog.showAndWait().get();
            }
             catch (Exception e) {
            }

            if(userName != null && !userName.trim().equals("")){

                System.out.println("Hello: " + userName);
                if (controller.checkName(userName)) {
                    usernameChosen = false;
                } else {
                    usernameChosen = true;
                    controller.doConnect(userName);
                    primaryStage.setTitle(userName + "'s Chat Room");

                }

            }
        }

        //receiving online users, after last received online users "" is send
        OnlineUsersList onlineUsersList = new OnlineUsersList();

        controller.setOnlineUsersList(onlineUsersList);

        onlineUsersList.addObserver(controller);

        controller.updateOnlineUsers();

        ChatRoom chatroom = new ChatRoom("GroupChat");
        controller.addChatRoom(chatroom);

        new ClientThread(controller).start();

    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}


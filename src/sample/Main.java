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
                String answer = userName;
                System.out.println("Hello: " + answer);
                if (controller.checkName(answer) == true) {
                    usernameChosen = false;
                } else {
                    usernameChosen = true;
                    controller.doConnect(answer);
                    primaryStage.setTitle(answer + "'s Chat Room");

                }

            }
        }

        //receiving online users, after last received online users "" is send
        OnlineUsersList onlineUsersList = new OnlineUsersList();

        controller.setOnlineUsersList(onlineUsersList);

        onlineUsersList.addObserver(controller);

        //controller.addOnlineUser(userName);

        controller.updateOnlineUsers();

        new ClientThread(controller).run();

    }
    public static void main(String[] args) {

        //Main main= new Main();
        //main.doTest();

        Application.launch(args);
    }
}


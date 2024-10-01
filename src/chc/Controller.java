/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chc;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 * @author user
 */
public class Controller {
    
    @FXML
    StackPane stackPane;
    @FXML
    JFXDialog dialog;

    public void customDialog(String title, String body, String icon, boolean btnIncluded, Button defaultBtn){
        JFXDialogLayout layout = new JFXDialogLayout();
        initLayout(layout, title, body, icon);

        loadDialog(layout, btnIncluded, defaultBtn);
    }
    
    public void customDialog(String title, String body, String icon, boolean btnIncluded){
        
            JFXDialogLayout layout = new JFXDialogLayout();
            initLayout(layout, title, body, icon);
            
            loadDialog(layout);
    }

    public void exceptionLayout(Exception e, Button defaultBtn){
        JFXDialogLayout layout = new JFXDialogLayout();
        initLayout(layout, "UNEXPECTED ERROR", e.getLocalizedMessage(), "error_small.png");

        loadDialog(layout, true, defaultBtn);
    }

    public void loadDialog(JFXDialogLayout layout, boolean btnIncluded, Button defaultBtn){

        stackPane.setVisible(true);
        JFXButton btn = new JFXButton("OK");
        btn.setDefaultButton(true);
        defaultBtn.setDefaultButton(false);
        btn.setId("alert-btn");
        btn.setCursor(Cursor.HAND);
        btn.setOnAction(Action -> {
            dialog.close();
            stackPane.setVisible(false);
            btn.setDefaultButton(false);
            defaultBtn.setDefaultButton(true);
        });
        if(btnIncluded){
            layout.setActions(btn);
        }
        dialog = new JFXDialog(stackPane, layout , JFXDialog.DialogTransition.CENTER);
        dialog.setOverlayClose(false);
        dialog.show();

    }
    
    public void loadDialog(JFXDialogLayout layout){

        stackPane.setVisible(true);
        dialog = new JFXDialog(stackPane, layout , JFXDialog.DialogTransition.CENTER);
        dialog.setOverlayClose(false);
        dialog.show();

    }

    public static void initLayout(JFXDialogLayout layout, String header, String body, String icon){

        Image image = new Image(ClassLoader.class.getResourceAsStream("/icons/" + icon));
        ImageView imageView = new ImageView(image);
        Label label = new Label(header);
        label.graphicProperty().setValue(imageView);
        layout.setHeading(label);
        layout.setBody(new Text(body));

    }
    
}

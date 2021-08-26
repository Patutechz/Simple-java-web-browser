package com.patutechz.ptwebbrowser;
import java.io.File;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
public class AboutBox {
    
    public void show()
 {
        String about = "<center><h1>PT Web Browser</h1></center>"
                +"<br ><center><p>PT Web Browser is a free and open source project made by the team at patutechz.com</p><center>";
        
        Stage stage = new Stage(); 
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.setTitle("About PT Web Browser"); 
        stage.setWidth(500); 
        stage.setHeight(500);
        WebView myweb1 = new WebView();
        WebEngine myenginee = myweb1.getEngine();
        File aboutFile = new File("about.html");
                        try{
                          myenginee.loadContent(about);
                        //myenginee.load(aboutFile.toURI().toURL().toExternalForm());
                        }catch(Exception ex){
                            
                        }
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("africa.png")));
        Button btnOK = new Button(); 
        btnOK.setText("OK");
        btnOK.setOnAction(e -> stage.close()); 
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER_RIGHT);
        hb.getChildren().add(btnOK);
        VBox pane = new VBox(20); 
        pane.getChildren().addAll(imageView,myweb1, hb);
        pane.setAlignment(Pos.CENTER); 
        Scene scene = new Scene(pane); 
       // File f = new File("patutechz.png");
       // Image img = new Image(f.toURI().toString());
       Image img = new Image(getClass().getResourceAsStream("africa.png"));
        stage.getIcons().add(img);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait(); 
 }
    
}

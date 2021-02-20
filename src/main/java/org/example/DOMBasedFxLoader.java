package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * JavaFX App
 */
public class DOMBasedFxLoader extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException, ParserConfigurationException, SAXException {

        MenuBar bar = new MenuBar();
        File xmlFile = new File("src/main/resources/menus.xml");
        Document document = createDocumentFromFile(xmlFile);
        buildMenuBar(document, bar);

        TextArea editor = new TextArea();

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(bar);
        borderPane.setCenter(editor);

        scene = new Scene(borderPane, 640, 480);
        stage.setScene(scene);
        stage.setTitle("Custom FXML Loader using DOM & SAX");
        stage.show();
    }

    public Document createDocumentFromFile(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        return parser.parse(xmlFile);
    }

    private void buildMenuBar(Document document, MenuBar bar) {
        System.out.println("Recursivly traversing documnet...");
        Element root = document.getDocumentElement();
        traverseDOMTreeRecursivly(root, bar);

    }

    private void traverseDOMTreeRecursivly(Node parentNode, MenuBar bar) {


        // Check for attributes and print them
        NamedNodeMap nodeMap = parentNode.getAttributes();

        if (nodeMap != null) {
            for (int i = 0; i < nodeMap.getLength(); i++) {
                Node node = nodeMap.item(i);
                if (parentNode.getNodeName() == "menu")
                    bar.getMenus().add(new Menu(node.getNodeValue()));

            }
        }

        // Traversing child nodes recursively
        NodeList childNodes = parentNode.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {

            Node node = childNodes.item(i);

            if (node.getNodeName() == "menu_item") {

                MenuItem menuItem = new MenuItem(node.getTextContent());
                bar.getMenus().forEach(menu -> {
                    menu.getItems().add(menuItem);
                });
            }
            traverseDOMTreeRecursivly(node, bar);


        }

    }


    public static void main(String[] args) {
        launch();
    }

}
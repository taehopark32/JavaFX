import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Taeho Park
 * @version 1.0
 */
public class ToDoList extends Application {
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> types = new ArrayList<>();
    private ArrayList<String> hoursInfo = new ArrayList<>();
    private ArrayList<Date> dates = new ArrayList<>();
    private ListView<String> listView;
    private Alert a = new Alert(Alert.AlertType.ERROR);
    private static int c = 0;
    private static int r = 0;

    private String font = "Verdana";

    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = new BorderPane();
        stage.setTitle("To-Do List");

        // Top
        VBox vertical = new VBox();
        Text title = new Text("To-Do List");
        title.setFont(Font.font(font, FontWeight.BOLD, 30));

        HBox box = new HBox();
        Text completed = new Text("Numbers of Tasks Completed:");
        Text completedNum = new Text("" + c);
        Text remaining = new Text("Numbers of Tasks Remaining:");
        Text remainingNum = new Text("" + r);
        box.getChildren().addAll(completed, completedNum, remaining, remainingNum);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);

        vertical.getChildren().addAll(title, box);
        vertical.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(vertical, Pos.CENTER);
        root.setTop(vertical);

        // Middle
        listView = new ListView<>();
        listView.setPadding(new Insets(50));
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        root.setCenter(listView);

        // Bottom
        HBox horizontal = new HBox();
        TextField name = new TextField();
        name.setPromptText("Task Name");
        ChoiceBox<String> type = new ChoiceBox<>();
        type.getItems().addAll("Study", "Shop", "Cook", "Sleep");
        ChoiceBox<String> hours = new ChoiceBox<>();
        hours.getItems().addAll("1", "2", "3", "4", "5");
        Button add = new Button("enqueue");
        Button delete = new Button("dequeue");

        horizontal.getChildren().addAll(name, type, hours, add, delete);
        horizontal.setPadding(new Insets(20));
        horizontal.setAlignment(Pos.CENTER);
        horizontal.setSpacing(10);
        BorderPane.setAlignment(horizontal, Pos.CENTER);
        root.setBottom(horizontal);

        add.setOnAction(e -> {
            String n = name.getText();
            String t = type.getValue();
            String h = hours.getValue();
            if (n.isEmpty() || t.isEmpty() || h.isEmpty()) {
                a.setContentText("Please fill in the boxes");
                a.show();
            } else if (names.contains(n) && types.contains(t) && hoursInfo.contains(h)) {
                a.setContentText("It already exist");
                a.show();
            } else if (names.contains(n) && types.contains(t)) {
                int num = 0;
                for (String tempName : names) {
                    if (n.equals(tempName)) {
                        break;
                    }
                    num++;
                }
                Date current = new Date();
                Date date = dates.get(num);
                if (current.getTime() > date.getTime()) {
                    date.setTime(date.getTime() + (3600000 * Integer.parseInt(hoursInfo.get(num)))
                            + (3600000 * Integer.parseInt(h)));
                    hoursInfo.set(num, Integer
                            .toString(Integer.parseInt(hoursInfo.get(num)) + Integer.parseInt(hours.getValue())));
                } else {
                    date.setTime(date.getTime() - (3600000 * Integer.parseInt(hoursInfo.get(num)))
                            + (3600000 * Integer.parseInt(h)));
                    hoursInfo.set(num, hours.getValue());
                }
                listView.getItems().set(num, "Task: " + n + " - Type: " + t + " - Complete by " + date);
                dates.set(num, date);

                name.setText("");
                type.setValue("");
                hours.setValue("");
            } else if (names.contains(n) && (types.contains(t) || hoursInfo.contains(h))) {
                a.setContentText("It already exists");
                a.show();
            } else {
                Date date = new Date();
                date.setTime(date.getTime() + 3600000 * Integer.parseInt(h));

                listView.getItems().addAll("Task: " + n + " - Type: " + t + " - Complete by " + date);

                names.add(n);
                types.add(t);
                hoursInfo.add(h);
                dates.add(date);

                name.setText("");
                type.setValue("");
                hours.setValue("");

                r++;
                remainingNum.setText("" + r);
            }
        });

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                listView.getItems().remove(0);
                names.remove(0);
                types.remove(0);
                hoursInfo.remove(0);
                dates.remove(0);

                c++;
                completedNum.setText("" + c);
                r--;
                remainingNum.setText("" + r);
            }
        });

        // additional feature.
        VBox additional = new VBox();

        // Rectangle
        Rectangle rectangle = new Rectangle(100, 100, Color.BLACK);
        // Mouse.
        Label mouse = new Label("Mouse");

        // Clear button.
        Button clear = new Button("clear");
        clear.setOnAction(e -> {
            listView.getItems().clear();
            names.clear();
            types.clear();
            hoursInfo.clear();
            dates.clear();
            c = r = 0;
            completedNum.setText("" + c);
            remainingNum.setText("" + r);
        });

        // make it italic.
        CheckBox italic = new CheckBox("Italic");
        EventHandler<ActionEvent> handler = e -> {
            if (italic.isSelected()) {
                title.setFont(Font.font(font, FontWeight.BOLD, FontPosture.ITALIC, 30));
            } else {
                title.setFont(Font.font(font, FontWeight.BOLD, 30));
            }
        };
        italic.setOnAction(handler);

        // Font.
        ToggleGroup tg = new ToggleGroup();
        RadioButton r1 = new RadioButton("Verdana");
        RadioButton r2 = new RadioButton("Arial");
        r1.setOnAction(e -> {
            font = "Verdana";
            if (italic.isSelected()) {
                title.setFont(Font.font(font, FontWeight.BOLD, FontPosture.ITALIC, 30));
            } else {
                title.setFont(Font.font(font, FontWeight.BOLD, 30));
            }
        });
        r2.setOnAction(e -> {
            font = "Arial";
            if (italic.isSelected()) {
                title.setFont(Font.font(font, FontWeight.BOLD, FontPosture.ITALIC, 30));
            } else {
                title.setFont(Font.font(font, FontWeight.BOLD, 30));
            }
        });
        r1.setToggleGroup(tg);
        r2.setToggleGroup(tg);

        // Combo.
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("Black", "Blue");
        combo.setOnAction(e -> {
            if (combo.getValue().equals("Black")) {
                rectangle.setFill(Color.BLACK);
            } else {
                rectangle.setFill(Color.BLUE);
            }
        });
        combo.setPromptText("Rectangle Color");

        additional.getChildren().addAll(rectangle, mouse, clear, italic, r1, r2, combo);
        additional.setPadding(new Insets(10));
        additional.setSpacing(10);
        additional.setOnMousePressed(e -> {
            mouse.setTextFill(Color.BLUE);
        });

        additional.setOnMouseExited(e -> {
            mouse.setTextFill(Color.BLACK);
        });

        root.setRight(additional);

        // final
        Scene scene = new Scene(root, 800, 500);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
package eu.mihosoft.fx.tutorials.milkglass;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Java FX Milk Glass Demo.
 * 
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class Main extends Application {
    
    // Circle colors
    Color[] colors = {
        new Color(0.2,0.5,0.8, 1.0).saturate().brighter().brighter(),
        new Color(0.3,0.2,0.7,1.0).saturate().brighter().brighter(),
        new Color(0.8,0.3,0.9,1.0).saturate().brighter().brighter(),
        new Color(0.4,0.3,0.9,1.0).saturate().brighter().brighter(),
        new Color(0.2,0.5,0.7,1.0).saturate().brighter().brighter()};

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // toplevel pane
        StackPane rootPane = new StackPane();

        // circle container
        Pane container = new Pane();

        // circle container is a child of the root pane
        rootPane.getChildren().add(container);

        // background style for the container
        container.setStyle("-fx-background-color: rgb(25,40,80)");

        // create a scene with size 1280x800
        Scene scene = new Scene(rootPane, 1280, 800);

        // number of nodes that shall be spawned
        int spawnNodes = 800;

        // spawn the nodes (circles)
        for (int i = 0; i < spawnNodes; i++) {
            spawnNode(scene, container);
        }

        // create the milk glass pane
        MilkGlassPane milkGlassPane = new MilkGlassPane(container);
        milkGlassPane.setMaxSize(600, 400);
        
        // add the milk glass pane to the root pane
        rootPane.getChildren().add(milkGlassPane);

        // final stage setup
        primaryStage.setTitle("Milk Glass Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Spawns a node (circle).
     * @param scene scene
     * @param container circle container
     */
    private void spawnNode(Scene scene, Pane container) {

        // create a circle node
        Circle node = new Circle(0);
        
        // circle shall be ignored by parent layout
        node.setManaged(false);
        
        // randomly pick one of the colors
        node.setFill(colors[(int) (Math.random() * colors.length)]);
        
        // choose a random position
        node.setCenterX(Math.random() * scene.getWidth());
        node.setCenterY(Math.random() * scene.getHeight());
        
        // add the container to the circle container
        container.getChildren().add(node);

        // create a timeline that fades the circle in and and out and also moves
        // it across the screen
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(node.radiusProperty(), 0),
                        new KeyValue(node.centerXProperty(), node.getCenterX()),
                        new KeyValue(node.centerYProperty(), node.getCenterY()),
                        new KeyValue(node.opacityProperty(), 0)),
                new KeyFrame(
                        Duration.seconds(5 + Math.random() * 5),
                        new KeyValue(node.opacityProperty(), Math.random()),
                        new KeyValue(node.radiusProperty(), Math.random() * 20)),
                new KeyFrame(
                        Duration.seconds(10 + Math.random() * 20),
                        new KeyValue(node.radiusProperty(), 0),
                        new KeyValue(node.centerXProperty(), Math.random() * scene.getWidth()),
                        new KeyValue(node.centerYProperty(), Math.random() * scene.getHeight()),
                        new KeyValue(node.opacityProperty(), 0))
        );

        // timeline shall be executed once
        timeline.setCycleCount(1);
        
        // when we are done we spawn another node
        timeline.setOnFinished(evt -> {
            container.getChildren().remove(node);
            spawnNode(scene, container);
        });

        // finally, we play the timeline
        timeline.play();
    }
}

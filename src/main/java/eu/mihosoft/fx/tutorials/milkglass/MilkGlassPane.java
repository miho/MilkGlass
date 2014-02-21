/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.fx.tutorials.milkglass;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import jfxtras.labs.util.event.MouseControlUtil;

/**
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class MilkGlassPane extends Pane {

    // circle container
    private final Pane container;

    // blur (milk glass effect)
    private final BoxBlur blur;
    private final double initialBlur = 15;

    // background image
    private WritableImage image;
    
    
    // <editor-fold defaultstate="collapsed" desc="Zom/Lense Effect">
    private double scale = 1;
    // </editor-fold>

    public MilkGlassPane(Pane container) {

        // milk glass effect:
        // set the blur and color adjust effects
        blur = new BoxBlur(initialBlur, initialBlur, 3);
        setEffect(blur);
        blur.setInput(new ColorAdjust(0, 0, 0.4, 0));

        // circle container
        this.container = container;

        // update the background every pulse
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long time) {
                updateBackground();
            }
        };

        timer.start();

        // <editor-fold defaultstate="collapsed" desc=" Dragging Behavior ">
        MouseControlUtil.makeDraggable(this);

        addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent t) -> {
            setManaged(false);
        });
        addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent t) -> {
            setManaged(true);
            updateBackground();
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Scroll Zoom Behavior ">
        addEventHandler(ScrollEvent.SCROLL, (ScrollEvent t) -> {
            scale += t.getDeltaY() * 0.0025;
            if (scale > 5) {
                scale = 5;
            } else if (scale < 1) {
                scale = 1;
            }

            blur.setWidth(Math.min(initialBlur * scale, 30));
            blur.setHeight(Math.min(initialBlur * scale, 30));
        });
        // </editor-fold>
    }

    /**
     * Updates the background. Create a snapshot of the circle container that
     * fits exactly this pane's bounds and updates the background.
     */
    private void updateBackground() {

        // return null if this pane is invisible
        if (getWidth() <= 0
                || getHeight() <= 0
                || this.getOpacity() == 0) {
            return;
        }

        // creates a new writable image and update background if dimensions do not match
        if (image == null
                || getWidth() != image.getWidth()
                || getHeight() != image.getHeight()) {
            
            image = new WritableImage(
                    (int) (this.getWidth()),
                    (int) (this.getHeight()));

            // create the backgrouhnd image
            BackgroundImage backgroundImg = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

            setBackground(new Background(backgroundImg));
        }

        // x, y location of this pane
        double x = getLayoutX();
        double y = getLayoutY();

        // create the snapshot parameters (defines viewport)
        SnapshotParameters sp = new SnapshotParameters();
        Rectangle2D rect = new Rectangle2D(
                x,
                y,
                getWidth(),
                getHeight());
        sp.setViewport(rect);

        // <editor-fold defaultstate="collapsed" desc="Lense/Zoom Effect">
        Scale scaleT = new Scale(scale, scale, 1);
        scaleT.setPivotX(container.getWidth() / 2);
        scaleT.setPivotY(container.getHeight() / 2);
        sp.setTransform(scaleT);
        // </editor-fold>

        // create the snaphot
        container.snapshot(sp, image);

    }
}

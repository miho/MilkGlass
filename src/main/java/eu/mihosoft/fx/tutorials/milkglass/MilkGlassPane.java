/*
 * Copyright 2017 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Michael Hoffer <info@michaelhoffer.de>.
 */

package eu.mihosoft.fx.tutorials.milkglass;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;

/**
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class MilkGlassPane extends Region {

    // circle container
    private final Pane container;

    // blur (milk glass effect)
    private final BoxBlur blur;
    private final double initialBlur = 15;

    // background image
    private WritableImage image;


    public MilkGlassPane(Pane container) {

        // milk glass effect:
        // set the blur and color adjust effects
        blur = new BoxBlur(initialBlur, initialBlur, 3);
        setEffect(blur);
        blur.setInput(new ColorAdjust(0, 0, 0.4, 0.0));

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

    }

    /**
     * Updates the background. Create a snapshot of the circle container that
     * fits exactly this pane's bounds and updates the background.
     */
    private void updateBackground() {

        // return null if this pane is invisible
        if (getWidth() < 1
                || getHeight() < 1
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

        // create the snapshot
        container.snapshot(sp, image);

    }
}

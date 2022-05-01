package Raytracer.UI;

import Raytracer.Renderer;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class RenderViewComponent extends JComponent {
    @Serial
    private static final long serialVersionUID = 1L;
    private Renderer renderer;

    public RenderViewComponent(Renderer renderer) {
        this.renderer = renderer;

        setPreferredSize(new Dimension(renderer.getRender().getWidth(), renderer.getRender().getHeight()));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(renderer.getRender(), 0, 0, null);
    }
}

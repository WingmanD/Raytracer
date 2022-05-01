package Raytracer.UI;

import Raytracer.Renderer;
import Raytracer.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.Serial;

public class Raytracer extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private Renderer renderer;

    private JLabel sceneLabel;

    public Raytracer() {
        super("Raytracer");

        renderer = new Renderer(new Scene());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(renderer.getRender().getWidth() + 450, renderer.getRender().getHeight());
        setLocation(0, 0);

        initGUI();
    }

    private void initGUI() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new RenderViewComponent(renderer), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Renderer v1.0"));

        JPanel scenePanel = new JPanel(new FlowLayout());
        sceneLabel = new JLabel("Scene: " + renderer.getScene().name);
        sceneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scenePanel.add(sceneLabel);

        JButton loadButton = new JButton("Open");
        loadButton.setAction(load);
        scenePanel.add(loadButton);
        scenePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(scenePanel);
        getContentPane().add(panel, BorderLayout.EAST);

        pack();
    }

    private final Action load = new AbstractAction() {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Load");
        }
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Raytracer().setVisible(true));
    }

}

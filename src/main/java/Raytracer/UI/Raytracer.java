package Raytracer.UI;

import Raytracer.Renderer;
import Raytracer.Scene;
import Raytracer.Light;
import Raytracer.Util.Vector3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.Serial;

public class Raytracer extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private Renderer renderer;
    private JLabel sceneLabel;
    private JPanel detailsPanel;
    private GridBagConstraints gbc = new GridBagConstraints();

    public Raytracer() {
        super("Raytracer");

        renderer = new Renderer(new Scene());
        setupActions();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(renderer.getRender().getWidth() + 350, renderer.getRender().getHeight()));
        setMaximumSize(new Dimension(renderer.getRender().getWidth() + 350, renderer.getRender().getHeight()));

        initGUI();
    }

    private void initGUI() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new RenderViewComponent(renderer), BorderLayout.WEST);

        detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Renderer v1.0", JLabel.CENTER);
        detailsPanel.add(titleLabel, gbc);

        JPanel scenePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sceneLabel = new JLabel("Scene: " + renderer.getScene().name, JLabel.LEFT);
        sceneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scenePanel.add(sceneLabel);

        scenePanel.add(new JButton(load));
        detailsPanel.add(scenePanel, gbc);



        detailsPanel.add(createCameraPanel(), gbc);

        detailsPanel.add(new JButton(addLight), gbc);
        detailsPanel.add(new JButton(render), gbc);
        detailsPanel.add(new JButton(save), gbc);

        getContentPane().add(detailsPanel, BorderLayout.CENTER);
    }

    private final Action load = new AbstractAction() {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Load");
        }
    };

    private final Action render = new AbstractAction() {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Render");
        }
    };

    private final Action addLight = new AbstractAction() {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            detailsPanel.add(createLightPanel(), gbc);
            detailsPanel.revalidate();
            detailsPanel.repaint();
        }
    };

    private final Action save = new AbstractAction() {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Save");
        }
    };

    private void setupActions() {
        load.putValue(Action.NAME, "Open");
        render.putValue(Action.NAME, "Render");
        addLight.putValue(Action.NAME, "Add Point Light");
        save.putValue(Action.NAME, "Save Render As");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Raytracer().setVisible(true));
    }


    private JPanel createCameraPanel(){
        JPanel cameraPanel = new JPanel();
        cameraPanel.setLayout(new BoxLayout(cameraPanel, BoxLayout.Y_AXIS));
        JLabel cameraLabel = new JLabel("Camera", JLabel.CENTER);
        cameraLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel positionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel cameraPositionLabel = new JLabel("Position: ", JLabel.LEFT);
        JTextField cameraPositionX = new JTextField(5);
        JTextField cameraPositionY = new JTextField(5);
        JTextField cameraPositionZ = new JTextField(5);
        positionPanel.add(cameraPositionLabel);
        positionPanel.add(cameraPositionX);
        positionPanel.add(cameraPositionY);
        positionPanel.add(cameraPositionZ);

        JPanel rotationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel cameraRotationLabel = new JLabel("Rotation: ", JLabel.LEFT);
        JTextField cameraRotationX = new JTextField(5);
        JTextField cameraRotationY = new JTextField(5);
        JTextField cameraRotationZ = new JTextField(5);
        rotationPanel.add(cameraRotationLabel);
        rotationPanel.add(cameraRotationX);
        rotationPanel.add(cameraRotationY);
        rotationPanel.add(cameraRotationZ);

        JPanel fovPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel cameraFOVLabel = new JLabel("FOV: ", JLabel.LEFT);
        JTextField cameraFOV = new JTextField(5);
        fovPanel.add(cameraFOVLabel);
        fovPanel.add(cameraFOV);

        cameraPanel.add(cameraLabel);
        cameraPanel.add(positionPanel);
        cameraPanel.add(rotationPanel);
        cameraPanel.add(fovPanel);

        return cameraPanel;
    }

    private JPanel createLightPanel() {
        Light light = new Light(new Vector3(0, 0, 0), new Vector3(1, 1, 1), 1);
        renderer.getScene().addLight(light);
        JPanel lightPanel = new JPanel();
        lightPanel.setLayout(new BoxLayout(lightPanel, BoxLayout.Y_AXIS));
        JLabel lightLabel = new JLabel("Light " + renderer.getScene().getLights().indexOf(light) + 1, JLabel.CENTER);
        lightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel positionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lightPositionLabel = new JLabel("Position: ", JLabel.LEFT);
        JTextField lightPositionX = new JTextField(String.valueOf(light.position.x), 5);
        JTextField lightPositionY = new JTextField(String.valueOf(light.position.y), 5);
        JTextField lightPositionZ = new JTextField(String.valueOf(light.position.z), 5);
        positionPanel.add(lightPositionLabel);
        positionPanel.add(lightPositionX);
        positionPanel.add(lightPositionY);
        positionPanel.add(lightPositionZ);

        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lightColorLabel = new JLabel("Color: ", JLabel.LEFT);
        JTextField lightColorR = new JTextField(String.valueOf(light.color.x), 5);
        JTextField lightColorG = new JTextField(String.valueOf(light.color.y), 5);
        JTextField lightColorB = new JTextField(String.valueOf(light.color.z), 5);
        colorPanel.add(lightColorLabel);
        colorPanel.add(lightColorR);
        colorPanel.add(lightColorG);
        colorPanel.add(lightColorB);

        JPanel fovPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lightIntensityLabel = new JLabel("Intensity: ", JLabel.LEFT);
        JTextField intensity = new JTextField(String.valueOf(light.getIntensity()), 5);
        fovPanel.add(lightIntensityLabel);
        fovPanel.add(intensity);

        lightPanel.add(lightLabel);
        lightPanel.add(positionPanel);
        lightPanel.add(colorPanel);
        lightPanel.add(fovPanel);

        return lightPanel;
    }
}

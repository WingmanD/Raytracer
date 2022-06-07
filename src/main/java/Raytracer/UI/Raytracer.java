package Raytracer.UI;

import Raytracer.Renderer;
import Raytracer.Scene;
import Raytracer.Camera;
import Raytracer.Light;
import Raytracer.OBJLoader;
import Raytracer.Util.Vector3;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.List;

public class Raytracer extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private Renderer renderer;
    private JLabel sceneLabel;
    //private JTextField samplesField;
    private JPanel detailsPanel;
    private GridBagConstraints gbc = new GridBagConstraints();

    private RenderViewComponent renderViewComponent;
    private JButton renderButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton addLightButton;

    public Raytracer() {
        super("Raytracer");

        renderer = new Renderer(new Scene());
        setupActions();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(renderer.getRender().getWidth() + 350, renderer.getRender().getHeight()));
        setMaximumSize(new Dimension(renderer.getRender().getWidth() + 350, renderer.getRender().getHeight()));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        initGUI();
    }

    private void initGUI() {
        getContentPane().setLayout(new BorderLayout());
        renderViewComponent = new RenderViewComponent(renderer);
        getContentPane().add(renderViewComponent, BorderLayout.WEST);

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

        loadButton = new JButton(load);
        scenePanel.add(loadButton);

        detailsPanel.add(scenePanel, gbc);

        /*JPanel samplesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel samplesLabel = new JLabel("Samples: ", JLabel.LEFT);
        samplesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //todo
        samplesField = new JTextField("1", 5);
        samplesPanel.add(samplesLabel);
        samplesPanel.add(samplesField);*/

        renderButton = new JButton(render);
        renderButton.setEnabled(false);
        saveButton = new JButton(save);
        saveButton.setEnabled(false);
        addLightButton = new JButton(addLight);
        addLightButton.setEnabled(false);


        detailsPanel.add(renderButton, gbc);
        detailsPanel.add(saveButton, gbc);
        detailsPanel.add(createCameraPanel(), gbc);
        detailsPanel.add(addLightButton, gbc);

        getContentPane().add(detailsPanel, BorderLayout.CENTER);
    }

    private final Action load = new AbstractAction() {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return true;
                    /*try {
                        return f.getCanonicalPath().endsWith(".obj");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }*/
                }

                @Override
                public String getDescription() {
                    return "Wavefront OBJ files";
                }
            });

            if (fileChooser.showOpenDialog(Raytracer.this) == JFileChooser.APPROVE_OPTION) {
                try {
                    System.out.println("Loading " + fileChooser.getSelectedFile().getAbsolutePath());
                    sceneLabel.setText("Scene: " + fileChooser.getSelectedFile().getName());
                    renderer.setScene(new OBJLoader().loadScene(fileChooser.getSelectedFile().getAbsolutePath()));
                    if (renderer.getScene() != null) {
                        renderButton.setEnabled(true);
                        saveButton.setEnabled(true);
                        addLightButton.setEnabled(true);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    };

    private final Action render = new AbstractAction() {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            renderButton.setEnabled(false);
            var worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    renderer.render(1);
                    return null;
                }

                @Override
                protected void process(List<Void> chunks) {
                    renderViewComponent.update();
                }

                @Override
                protected void done() {
                    super.done();
                    renderButton.setEnabled(true);
                }
            };
            worker.execute();
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


    private JPanel createCameraPanel() {
        Camera camera = renderer.getScene().getCamera();

        JPanel cameraPanel = new JPanel();
        cameraPanel.setLayout(new BoxLayout(cameraPanel, BoxLayout.Y_AXIS));
        JLabel cameraLabel = new JLabel("Camera", JLabel.CENTER);
        cameraLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel positionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel cameraPositionLabel = new JLabel("Position: ", JLabel.LEFT);
        JTextField cameraPositionX = new JTextField(String.valueOf(camera.getPosition().x), 5);
        JTextField cameraPositionY = new JTextField(String.valueOf(camera.getPosition().y), 5);
        JTextField cameraPositionZ = new JTextField(String.valueOf(camera.getPosition().z), 5);


        addCameraPositionUpdateListener(cameraPositionX, camera, 0);
        addCameraPositionUpdateListener(cameraPositionY, camera, 1);
        addCameraPositionUpdateListener(cameraPositionZ, camera, 2);

        positionPanel.add(cameraPositionLabel);
        positionPanel.add(cameraPositionX);
        positionPanel.add(cameraPositionY);
        positionPanel.add(cameraPositionZ);

        JPanel lookAtPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel cameraLookAtLabel = new JLabel("Look at: ", JLabel.LEFT);
        JTextField cameraLookAtX = new JTextField(String.valueOf(camera.getLookAt().x), 5);
        JTextField cameraLookAtY = new JTextField(String.valueOf(camera.getLookAt().y), 5);
        JTextField cameraLookAtZ = new JTextField(String.valueOf(camera.getLookAt().z), 5);

        addCameraLookAtUpdateListener(cameraLookAtX, camera, 0);
        addCameraLookAtUpdateListener(cameraLookAtY, camera, 1);
        addCameraLookAtUpdateListener(cameraLookAtZ, camera, 2);

        lookAtPanel.add(cameraLookAtLabel);
        lookAtPanel.add(cameraLookAtX);
        lookAtPanel.add(cameraLookAtY);
        lookAtPanel.add(cameraLookAtZ);

        cameraPanel.add(cameraLabel);
        cameraPanel.add(positionPanel);
        cameraPanel.add(lookAtPanel);

        return cameraPanel;
    }

    private void addCameraPositionUpdateListener(JTextField cameraPositionLabel, Camera camera, int axis) {
        cameraPositionLabel.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double value = Double.parseDouble(cameraPositionLabel.getText());
                    switch (axis) {
                        case 0 ->
                                camera.setPosition(new Vector3(value, camera.getPosition().y, camera.getPosition().z));
                        case 1 ->
                                camera.setPosition(new Vector3(camera.getPosition().x, value, camera.getPosition().z));
                        case 2 ->
                                camera.setPosition(new Vector3(camera.getPosition().x, camera.getPosition().y, value));
                    }
                    renderer.getScene().setCamera(camera);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                insertUpdate(e);
            }
        });
    }


    private void addCameraLookAtUpdateListener(JTextField cameraLookAtLabel, Camera camera, int axis) {
        cameraLookAtLabel.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double value = Double.parseDouble(cameraLookAtLabel.getText());
                    switch (axis) {
                        case 0 -> camera.setLookAt(new Vector3(value, camera.getLookAt().y, camera.getLookAt().z));
                        case 1 -> camera.setLookAt(new Vector3(camera.getLookAt().x, value, camera.getLookAt().z));
                        case 2 -> camera.setLookAt(new Vector3(camera.getLookAt().x, camera.getLookAt().y, value));
                    }
                    renderer.getScene().setCamera(camera);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                insertUpdate(e);
            }
        });
    }

    private void addLightPositionUpdateListener(JTextField lightPositionLabel, Light light, int axis) {
        lightPositionLabel.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double value = Double.parseDouble(lightPositionLabel.getText());
                    switch (axis) {
                        case 0 -> light.setPosition(new Vector3(value, light.getPosition().y, light.getPosition().z));
                        case 1 -> light.setPosition(new Vector3(light.getPosition().x, value, light.getPosition().z));
                        case 2 -> light.setPosition(new Vector3(light.getPosition().x, light.getPosition().y, value));
                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                insertUpdate(e);
            }
        });
    }

    private void addLightColorUpdateListener(JTextField lightColorLabel, Light light, int axis) {
        lightColorLabel.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double value = Double.parseDouble(lightColorLabel.getText());
                    switch (axis) {
                        case 0 -> light.setColor(new Vector3(value, light.getColor().y, light.getColor().z));
                        case 1 -> light.setColor(new Vector3(light.getColor().x, value, light.getColor().z));
                        case 2 -> light.setColor(new Vector3(light.getColor().x, light.getColor().y, value));
                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                insertUpdate(e);
            }
        });
    }

    private JPanel createLightPanel() {
        Light light = new Light(new Vector3(0, 0, 0), new Vector3(1, 1, 1), 1);
        renderer.getScene().addLight(light);
        JPanel lightPanel = new JPanel();
        lightPanel.setLayout(new BoxLayout(lightPanel, BoxLayout.Y_AXIS));
        JLabel lightLabel = new JLabel("Light " + renderer.getScene().getLights().indexOf(light), JLabel.CENTER);
        lightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel positionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lightPositionLabel = new JLabel("Position: ", JLabel.LEFT);
        JTextField lightPositionX = new JTextField(String.valueOf(light.position.x), 5);
        JTextField lightPositionY = new JTextField(String.valueOf(light.position.y), 5);
        JTextField lightPositionZ = new JTextField(String.valueOf(light.position.z), 5);

        addLightPositionUpdateListener(lightPositionX, light, 0);
        addLightPositionUpdateListener(lightPositionY, light, 1);
        addLightPositionUpdateListener(lightPositionZ, light, 2);

        positionPanel.add(lightPositionLabel);
        positionPanel.add(lightPositionX);
        positionPanel.add(lightPositionY);
        positionPanel.add(lightPositionZ);

        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lightColorLabel = new JLabel("Color: ", JLabel.LEFT);
        JTextField lightColorR = new JTextField(String.valueOf(light.color.x), 5);
        JTextField lightColorG = new JTextField(String.valueOf(light.color.y), 5);
        JTextField lightColorB = new JTextField(String.valueOf(light.color.z), 5);

        addLightColorUpdateListener(lightColorR, light, 0);
        addLightColorUpdateListener(lightColorG, light, 1);
        addLightColorUpdateListener(lightColorB, light, 2);

        colorPanel.add(lightColorLabel);
        colorPanel.add(lightColorR);
        colorPanel.add(lightColorG);
        colorPanel.add(lightColorB);

        JPanel fovPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lightIntensityLabel = new JLabel("Intensity: ", JLabel.LEFT);
        JTextField intensity = new JTextField(String.valueOf(light.getIntensity()), 5);

        intensity.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double value = Double.parseDouble(intensity.getText());
                    light.setIntensity(value);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                insertUpdate(e);
            }
        });

        fovPanel.add(lightIntensityLabel);
        fovPanel.add(intensity);

        lightPanel.add(lightLabel);
        lightPanel.add(positionPanel);
        lightPanel.add(colorPanel);
        lightPanel.add(fovPanel);

        return lightPanel;
    }
}

package sample;

        import javafx.beans.value.ChangeListener;
        import javafx.concurrent.Task;
        import javafx.embed.swing.SwingFXUtils;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.*;
        import javafx.scene.control.Button;
        import javafx.scene.control.TextField;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.scene.paint.Color;
        import javafx.scene.shape.Rectangle;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;
        import org.apache.commons.imaging.color.ColorCieLab;
        import org.apache.commons.imaging.color.ColorConversions;

        import javax.imageio.ImageIO;
        import java.awt.*;
        import java.awt.image.BufferedImage;
        import java.io.File;
        import java.io.IOException;

public class Controller {

    public File imageFile;

    private int rBefore = 0;
    private int gBefore = 0;
    private int bBefore = 0;
    private int rAfter = 0;
    private int gAfter = 0;
    private int bAfter = 0;

    @FXML
    private Slider bAfterSlider;

    @FXML
    private Slider rBeforeSlider;

    @FXML
    private ToggleButton hueToggle;

    @FXML
    private ImageView imageBefore;

    @FXML
    private Button transformButton;

    @FXML
    private Slider gAfterSlider;

    @FXML
    private ImageView imageAfter;

    @FXML
    private Slider gBeforeSlider;

    @FXML
    private TextField faultTextField;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Slider bBeforeSlider;

    @FXML
    private Slider rAfterSlider;

    @FXML
    private Rectangle rectAfter;

    @FXML
    private Rectangle rectBefore;

    @FXML
    private ProgressBar progressBar;

    @FXML
    public Button saveButton;

    private BufferedImage bufferedImageBefore;
    private BufferedImage bufferedImageAfter;

    @FXML
    void transformImage(ActionEvent event) throws InterruptedException {
        progressBar.setStyle("-fx-accent: #0093ff;");

        try {
            bufferedImageAfter = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int fault;
        try {
            fault = Integer.parseInt(faultTextField.getText());
            if(fault < 0 || fault > 100) {
                throw new IllegalArgumentException("Fault must be between 0 and 100.");
            }
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.show();
            return;
        }

        int width = bufferedImageAfter.getWidth();
        int height = bufferedImageAfter.getHeight();


        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for(int x = 0; x < width; x++) {
                    for(int y = 0; y < height; y++) {
                        updateProgress((double) x, (double) width);

                        ColorCieLab actColor =
                                ColorConversions.convertXYZtoCIELab(ColorConversions.convertRGBtoXYZ(bufferedImageAfter.getRGB(x, y)));
                        java.awt.Color colorRGBBefore = new java.awt.Color(rBefore, gBefore, bBefore);
                        ColorCieLab colorBefore =
                                ColorConversions.convertXYZtoCIELab(ColorConversions.convertRGBtoXYZ(colorRGBBefore.getRGB()));
                        if(Math.sqrt(Math.pow((actColor.a - colorBefore.a), 2) + Math.pow((actColor.b - colorBefore.b), 2) +
                                Math.pow((actColor.L - colorBefore.L) / 3, 2)) <= fault) {
                            ColorCieLab colorAfter =
                                    ColorConversions.convertXYZtoCIELab(ColorConversions.convertRGBtoXYZ(new java.awt.Color(rAfter, gAfter, bAfter).getRGB()));
                            ColorCieLab finalColorLab = new ColorCieLab((colorAfter.L + (actColor.L - colorBefore.L)),
                                    (colorAfter.a + (actColor.a - colorBefore.a)),
                                    (colorAfter.b + (actColor.b - colorBefore.b)));
                            java.awt.Color finalColor =
                                    new java.awt.Color(ColorConversions.convertXYZtoRGB(ColorConversions.convertCIELabtoXYZ(finalColorLab)));
                            bufferedImageAfter.setRGB(x, y, finalColor.getRGB());
                        }
                    }
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());

        progressBar.progressProperty().addListener(observable -> {
            if (progressBar.getProgress() >= 1 - 0.005) {
                progressBar.setStyle("-fx-accent: forestgreen;");
                Image imageFx = SwingFXUtils.toFXImage(bufferedImageAfter, null);
                imageAfter.setImage(imageFx);
            }
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    void setSliders(ActionEvent event) {
        Color color = colorPicker.getValue();
        rAfterSlider.setValue(color.getRed() * 255);
        gAfterSlider.setValue(color.getGreen() * 255);
        bAfterSlider.setValue(color.getBlue() * 255);
    }

    private void setSliderListeners() {
        ChangeListener<Number> changeBeforeValuesListener = (observable, oldValue, newValue) -> {
            rBefore = (int) rBeforeSlider.getValue();
            gBefore = (int) gBeforeSlider.getValue();
            bBefore = (int) bBeforeSlider.getValue();
            rectBefore.setFill(Color.rgb(rBefore, gBefore, bBefore));
        };

        ChangeListener<Number> changeAfterValuesListener = (observable, oldValue, newValue) -> {
            rAfter = (int) rAfterSlider.getValue();
            gAfter = (int) gAfterSlider.getValue();
            bAfter = (int) bAfterSlider.getValue();
            rectAfter.setFill(Color.rgb(rAfter, gAfter, bAfter));
            colorPicker.setValue(Color.rgb(rAfter, gAfter, bAfter));
        };

        rBeforeSlider.valueProperty().addListener(changeBeforeValuesListener);
        gBeforeSlider.valueProperty().addListener(changeBeforeValuesListener);
        bBeforeSlider.valueProperty().addListener(changeBeforeValuesListener);

        rAfterSlider.valueProperty().addListener(changeAfterValuesListener);
        gAfterSlider.valueProperty().addListener(changeAfterValuesListener);
        bAfterSlider.valueProperty().addListener(changeAfterValuesListener);
    }

    @FXML
    void initialize() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.png", "*.bmp"));
            fileChooser.setTitle("Open image");
            imageFile = fileChooser.showOpenDialog(new Stage());

            bufferedImageBefore = ImageIO.read(imageFile);
            Image imageFx = SwingFXUtils.toFXImage(bufferedImageBefore, null);
            imageAfter.setImage(imageFx);
            imageBefore.setImage(imageFx);

            setSliderListeners();

            imageBefore.setOnMouseClicked(event -> {
                Point point = MouseInfo.getPointerInfo().getLocation();
                try {
                    java.awt.Color color = new Robot().getPixelColor(point.x, point.y);
                    rBeforeSlider.setValue(color.getRed());
                    gBeforeSlider.setValue(color.getGreen());
                    bBeforeSlider.setValue(color.getBlue());
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveImage(ActionEvent actionEvent) {
        FileChooser chooser  = new FileChooser();
        chooser.setTitle("Save Image");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*.png"));
        File file = chooser.showSaveDialog(new Stage());
        try {
            ImageIO.write(bufferedImageAfter, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
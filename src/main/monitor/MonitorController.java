package main.monitor;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.LongStringConverter;
import main.monitor.attribute.Attribute;
import main.monitor.attribute.Catalog;
import main.monitor.attribute.exception.AttrNameIsNotUniqueException;
import main.monitor.attribute.exception.AttrTypeNotFoundException;
import main.monitor.attribute.exception.AttrWrongDefinitionException;
import main.monitor.attribute.exception.BuildCatalogException;
import main.monitor.attribute.parser.FileConfParser;
import main.monitor.attribute.parser.IParser;
import main.monitor.attribute.parser.JmxConfParser;

import javax.management.MalformedObjectNameException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.*;

public class MonitorController {
    public TableColumn<Attribute, String> attrNameColumn;
    public TableColumn<Attribute, String> attrTypeColumn;
    public TableColumn<Attribute, String> attrValueColumn;
    public TableColumn<Attribute, Long> attrPeriodColumn;
    public Label errorMessage;
    private Stage primaryStage;
    public Button enableButton;
    public Button loadFileButton;
    public Button sourceButton;
    public TableView<Attribute> tableView;
    public Label fileAddress;

    private static boolean isMonitorEnabled = true;
    private static boolean readFromFile = true;
    private File defaultDirectory = null;
    private IParser parser = null;
    private File configFile = null;
    private ScheduledExecutorService scheduledExecutorService = null;
    private ConcurrentHashMap<String, ScheduledFuture> sheduledTaskMap = new ConcurrentHashMap<>();

    /**
     * Init Monitor controller. Set default style for page.
     * @param primaryStage
     */
    public void init(Stage primaryStage) {
        // set primary stage.
        this.primaryStage = primaryStage;
        //set buttons stle
        enableButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
        sourceButton.setStyle("-fx-background-color: #377780;-fx-text-fill:#78ff00;");

        ObservableList<Attribute> attributes = FXCollections.observableList(new ArrayList<Attribute>());
        tableView.setItems(attributes);

        // set cell factory for edition of values.
        attrValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        attrPeriodColumn.setCellFactory(
                TextFieldTableCell.<Attribute, Long>forTableColumn(new LongStringConverter()));
    }


    /**
     * Update monitoring status (ON/OFF).
     *
     * @param actionEvent - event.
     */
    public void updateStatus(ActionEvent actionEvent) {
        isMonitorEnabled = !isMonitorEnabled;
        if (isMonitorEnabled) {
            enableButton.setText("Вкл.");
            enableButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            enableButton.setAlignment(Pos.BASELINE_LEFT);
            if (parser != null) {
                updateValuesInTable();
            }
        } else {
            switchOffMonitoring();
        }
    }

    /**
     * Update data source: file or JMX server.
     * Option 'file'
     *
     * @param actionEvent
     */
    public void updateSource(ActionEvent actionEvent) {
        readFromFile = !readFromFile;
        if (readFromFile) {
            sourceButton.setText("Файл");
            sourceButton.setStyle("-fx-background-color: #377780;-fx-text-fill:#78ff00;");
            sourceButton.setContentDisplay(ContentDisplay.RIGHT);
            loadFileButton.setDisable(false);

            if (configFile == null) {
                fileAddress.setText("Файл не выбран.");
                tableView.getItems().clear();
                shutDownExecutor();
            } else {
                fileAddress.setText("Файл: " + configFile.getName());
                parser = new FileConfParser(configFile.toPath());
                updateValuesInTable();
            }
        } else {
            String url = "service:jmx:rmi:///jndi/rmi://localhost:" + 1617 + "/jmxrmi";
            fileAddress.setText("JMX url: " + url);
            sourceButton.setText("JMX");
            sourceButton.setStyle("-fx-background-color: #2c8075;-fx-text-fill:#a61c24;");
            sourceButton.setContentDisplay(ContentDisplay.LEFT);
            loadFileButton.setDisable(true);
            try {
                parser = new JmxConfParser(url);
                sourceButton.setText("JMX");
                updateValuesInTable();
            } catch (MalformedURLException e) {
                errorMessage.setVisible(true);
                errorMessage.setText("Не получается соединиться с сервером.");
                initMessageHide();
            } catch (MalformedObjectNameException e) {
                errorMessage.setVisible(true);
                errorMessage.setText("Не найден JMX объект с атрибутами.");
                initMessageHide();
            } catch (IOException e) {
                errorMessage.setVisible(true);
                errorMessage.setText("Ошибка связи.");
                initMessageHide();
            }
        }
    }

    /**
     * Upload new configuration file and if source is file - rebuild table.
     *
     * @param actionEvent - event.
     */
    public void uploadFileConfig(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл с конфигурацией");
        if (defaultDirectory != null) {
            fileChooser.setInitialDirectory(defaultDirectory);
        }
        FileChooser.ExtensionFilter extFilter = new FileChooser
                .ExtensionFilter("TXT or CONF files (*.txt, *.conf)", "*.txt", "*.conf");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedConfig = fileChooser.showOpenDialog(primaryStage);
        if (selectedConfig != null) {
            defaultDirectory = selectedConfig.getParentFile();
            configFile = selectedConfig;
            if (readFromFile) {
                fileAddress.setText("Файл: " + configFile.getName());
                parser = new FileConfParser(configFile.toPath());
                updateValuesInTable();
            }
        }
    }

    /**
     * Update attribute config value.
     *
     * @param stCellEditEvent - event
     */
    public void updateAttrConf(TableColumn.CellEditEvent<Attribute, String> stCellEditEvent) {
        Attribute updatedAttribute = stCellEditEvent.getTableView().getItems().get(
                stCellEditEvent.getTablePosition().getRow());
        updatedAttribute.setAttrConfig(stCellEditEvent.getNewValue());
        parser.saveAttribute(updatedAttribute);
    }

    /**
     * Update attribute period.
     *
     * @param stCellEditEvent - event
     */
    public void updateAttrPeriod(TableColumn.CellEditEvent<Attribute, Long> stCellEditEvent) {
        Attribute updatedAttribute = stCellEditEvent.getTableView().getItems().get(
                stCellEditEvent.getTablePosition().getRow());
        updatedAttribute.setPollPeriod(stCellEditEvent.getNewValue());
        parser.saveAttribute(updatedAttribute);
    }

    private void updateValuesInTable() {
        try {
            // stop all task before update table.
            shutDownExecutor();
            Catalog catalog = parser.getNewCatalog();
            ObservableList<Attribute> items = tableView.getItems();
            items.clear();
            items.addAll(catalog.getAttrMap().values());
            if (isMonitorEnabled) {
                updateSheduler();
            }
        } catch (AttrWrongDefinitionException e) {
            errorMessage.setVisible(true);
            errorMessage.setText("Не получается обработать аттрибут. Ошибка: " + e.getMessage());
            initMessageHide();
        } catch (BuildCatalogException e) {
            errorMessage.setVisible(true);
            errorMessage.setText("Не получается построить каталог. Ошибка: " + e.getMessage());
            initMessageHide();
        } catch (AttrTypeNotFoundException e) {
            errorMessage.setVisible(true);
            errorMessage.setText("Не найден тип. Ошибка: " + e.getMessage());
            initMessageHide();
        } catch (AttrNameIsNotUniqueException e) {
            errorMessage.setVisible(true);
            errorMessage.setText("Атрибут задан несколько раз. Ошибка: " + e.getMessage());
            initMessageHide();
        }
    }

    private void updateSheduler() {
        scheduledExecutorService = Executors.newScheduledThreadPool(2);
        if (parser == null ) {
            return;

        }
        ConcurrentHashMap<String, Attribute> attrMap = parser.getAttrMap();
        if (attrMap == null) {
            return;
        }
        for (Attribute attribute : attrMap.values()) {
            addTaskInSheduler(attribute);
        }
    }

    private void addTaskInSheduler(Attribute attribute) {
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(
                new Watcher(attribute.getAttrName()),
                0,
                attribute.getPollPeriod(), TimeUnit.SECONDS
        );
        sheduledTaskMap.put(attribute.getAttrName(), scheduledFuture);
    }

    private void initMessageHide() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(true);
        Duration duration = Duration.millis(10000);
        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                errorMessage.setVisible(false);
            }
        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    /**
     * Runnable class to watch for values in sheduledExecutionService.
     */
    private class Watcher implements Runnable {
        private String attrName;

        public Watcher(String attrName) {
            this.attrName = attrName;
        }

        @Override
        public void run() {
            try {
                Attribute newAttr = parser.getAttribute(attrName);
                Attribute prevValue = parser.getAttrMap().get(attrName);
                if (!newAttr.equals(prevValue)) {
                    System.out.println("NOT EQUALS ");
                    System.out.println("newAttr = " + newAttr + ", prevValue = " + prevValue);
                    // This also change Attribute in observable List too!
                    prevValue.setAttrConfig(newAttr.getAttrConfig());
                    prevValue.setAttrName(newAttr.getAttrName());
                    prevValue.setAttrType(newAttr.getAttrType());
                    if (!newAttr.getPollPeriod().equals(prevValue.getPollPeriod())) {
                        prevValue.setPollPeriod(newAttr.getPollPeriod());
                        TaskRestarter taskRestarter = new TaskRestarter(newAttr);
                        taskRestarter.start();
                    }
                    // list update didn't updated gui.
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // element change of observableList with SimpleStringProperty didn't change view ->
                            // use clear with 'addAll'
                            tableView.getItems().clear();
                            tableView.getItems().addAll(parser.getAttrMap().values());
                        }
                    });
                }
            } catch (UndeclaredThrowableException e) {
                errorMessage.setVisible(true);
                errorMessage.setText("Проблема с JMX сервером.");
                switchOffMonitoring();
                initMessageHide();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TaskRestarter extends Thread {
        private Attribute attribute;

        public TaskRestarter(Attribute attribute) {
            this.attribute = attribute;
        }

        @Override
        public void run() {
            try {
                ScheduledFuture scheduledFuture = sheduledTaskMap.get(attribute.getAttrName());
                scheduledFuture.cancel(false);
                addTaskInSheduler(attribute);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void switchOffMonitoring() {
        enableButton.setText("Выкл.");
        enableButton.setStyle("-fx-background-color: grey;-fx-text-fill:black;");
        enableButton.setAlignment(Pos.BASELINE_RIGHT);
        shutDownExecutor();
    }

    private void shutDownExecutor() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
    }
}

package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.util.*;

public class Main extends Application {

    private Connection connection = null;
    private Statement statement = null;

    public int columnsSize;

    @Override
    public void start(Stage primaryStage) {

//        ObservableList<TableColumn> tableColumns = FXCollections.observableArrayList(new TableColumn());
        TableView<Item> tableView = new TableView();
//        tableView.itemsProperty().setValue(tableColumns);

        primaryStage.setScene(new Scene(tableView, 900, 600));
        primaryStage.show();

        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:E:\\db\\CarShop.db");
        } catch (SQLException e) {
            e.printStackTrace();
            if (this.connection != null) {
                try {
                    this.connection.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }

        try {
            this.statement = this.connection.createStatement();
            List<String> columns = getColumnNames("Transport");
            columnsSize = columns.size();
            if (columns != null) {
                Map<String, Data> stringDataMap = executeQuery("SELECT * FROM Transport");
                if (stringDataMap != null) {

                    Iterator<String> iterator = stringDataMap.keySet().iterator();
                    ObservableList<String> items = FXCollections.observableArrayList();



                    while (iterator.hasNext()) {
                        String next = iterator.next();
                        Data data = stringDataMap.get(next);
                        for (String d : data.data) {
                            System.out.println("ss" + d);
                        }

                        items.add(data.data.toString().replaceAll("\\[|\\]", ""));
//                        for(String it : data.data) {
//                            Item item = new Item(it);
//                        }
//                       TableColumn tableColumn = new TableColumn(data.column_name);
//                        tableColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
//                         tableView.getColumns().add(tableColumn);

//                        for (String d : data.data) {
//                            items.add(new Item(d));
//                        }
                        //  tableView.setItems(items);
                    }

                    for (String str : items){
                    //    System.out.println("ss " + str);
                    }


                    List<String[]> listT = new LinkedList<>();
                    for(String item: items){
                        String[] split = item.split(",");
                        listT.add(split);
                    }
                    String[] big_array = new String[listT.size()*listT.get(0).length];

                    int offset = 0;
                    for(String[] s:listT) {
                        System.arraycopy(s, 0, big_array, offset, s.length);
                        offset+=s.length;
//                        System.out.println(Arrays.toString(big_array));

                    }
                //    System.out.println("AAAAAAAAAAAAAAAAA "+Arrays.toString(big_array));

                    String[] array = new String[(items.get(0).split(", ").length - 1) * columnsSize];

                    System.out.println(items.size());
                    for (int k = 0; k < columnsSize; k++) {
                        String[] arr1 = items.get(k).split(", ");
                        for (int j = 1; j < arr1.length; j++) {
                            array[j - 1 + (k * (arr1.length - 1))] = arr1[j];
                        }
                    }

                    for (String a : array) {
                //        System.out.println("dd " + a);
                    }

//
//                    ObservableList<String> str1 = FXCollections.observableArrayList();
//
//                    System.out.println("pp" + items);

                    ObservableList<Item> itm = FXCollections.observableArrayList();

//
                    //   TableColumn<> tbl = new TableColumn<Item>();


                    for (int i = 0; i < columns.size(); i++) {
                        String[] arr = items.get(i).split(", ");

                        TableColumn tableColumn = new TableColumn(columns.get(i));
                        for (String str : arr) {

                            //     Item item = new Item(str);
//                            itm.add(new Item(str));
                            tableColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

                        }


                        tableView.getColumns().add(tableColumn);

                        tableView.setItems(itm);


                    }


//                    String[] arry = items.get(1).replaceAll("\\[|\\]", "").split(", ");
//
//                    for ( String str : arry){
//                        System.out.println(str);
//                    }

                    //   System.out.println(items.indexOf(1));
//                    iterator = stringDataMap.keySet().iterator();
//                    List<Item> items = new LinkedList<>();
//                    while(iterator.hasNext()){
//                        String next = iterator.next();
//                        for(String d: stringDataMap.get(next).data) {
//                            items.add(new Item(d));
//                        }
//                    }
                    ///

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


//        LinkedList<Boolean> list = new LinkedList<>();
//        list.add(true);
//        list.add(true);
//        list.add(false);
//        list.add(true);
//        list.add(false);
//        list.add(true);
//        Collections.sort(list);
//        System.out.println(Arrays.toString(list.toArray()));

//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        primaryStage.initModality(Modality.WINDOW_MODAL);
//        StackPane stackPane = new StackPane();
//        primaryStage.setResizable(false);

//        primaryStage.setAlwaysOnTop(true);
//        Text txt = new Text("ИДЕТ КОМПИЛЯЦИЯ");
//        txt.setFont(Font.font("Verdana", 120));
//        txt.setFill(Color.WHITESMOKE);
//        stackPane.getChildren().add(txt);

//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(stackPane, 300, 275, Color.TRANSPARENT));
//        primaryStage.setFullScreen(true);
//        primaryStage.show();

//        primaryStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println(newValue.booleanValue());
//        });
//        try {
//            statement.close();
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            System.exit(0);
//        }
    }

    public ObservableList<Item> getItemm() {
        ObservableList<Item> items1 = FXCollections.observableArrayList();

        return items1;
    }

    public List<String> getColumnNames(String table_name) {
        List<String> list_names = new LinkedList<>();
        DatabaseMetaData metaData = null;
        try {
            metaData = this.connection.getMetaData();
            ResultSet dogs = metaData.getColumns(null, null, table_name, null);

            while (dogs.next()) {
                String column_name = dogs.getString("COLUMN_NAME");
                list_names.add(column_name);
            }
        } catch (SQLException e) {
        }
        return list_names;
    }

    /**
     * @param sql Запрос
     * @return Map с именами столбцов Data class в качестве значения хранящее в себе значения строк
     **/
    public Map<String, Data> executeQuery(String sql) {
        Map<String, Data> list = new LinkedHashMap<>();
        ResultSet resultSet = null;
        try {
            resultSet = this.statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (!resultSet.isAfterLast()) {

                for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                    String tableName = metaData.getTableName(i);
                    String columnName = metaData.getColumnName(i);
                    String string = resultSet.getString(i);
//                    System.out.println(columnName+" "+string+" "+tableName+"\n");
                    if (list.containsKey(columnName)) {
                        Data data = list.get(columnName);
                        data.addData(string);
                    } else {
                        list.put(columnName, new Data(tableName, columnName, string));
                    }
                }

                resultSet.next();
            }
        } catch (SQLException e) {
            return null;
        }
        return list;
    }


    public class Item {

        public String[] itms = null;

        public Item(String[] item) {
            this.itms = item;

        }


//        public Item(String[] item){
//            for (int i = 0; i < item.length; i++)
//                itms[i] = item[i];
//        }

        public String[] getItem() {
            return itms;
        }

        public void setItem(String[] item) {
            this.itms = item;
        }

        @Override
        public String toString() {
            return itms[0];
        }
    }

    //
    public class Data {
        private String table_name = null;
        private String column_name = null;
        private ObservableList<String> data = null;

        public Data(String table_name, String column_name, String data) {
            this.table_name = table_name;
            this.column_name = column_name;
            this.data = FXCollections.observableList(new LinkedList<>());
            this.data.add(data);
        }

        public void addData(String data) {
            this.data.add(data);

        }

        public void setColumnName(String name) {
            column_name = name;
        }

        public String getColumnName() {
            return column_name;
        }

        public ObservableList<String> getData() {
            return data;
        }

        public String getTableName() {
            return table_name;
        }

        public void setTableName(String table_name) {
            this.table_name = table_name;
        }

        @Override
        public String toString() {
            return "[TABLE_NAME: " + table_name + ", COLUMN_NAME: " + column_name + ", DATA: " + Arrays.toString(data.toArray()) + "]";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package sample;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Callable;

public class TableViewExample extends Application {

    private Connection connection = null;
    private Statement statement = null;
    public List<String> dd = new ArrayList<>();
    public int size1;


    @Override
    public void start(Stage primaryStage) {

        initializeDatabase();

        TableView<Column> table = new TableView<>();

        Scene scene = new Scene(table, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        List<String> columns2 = getColumnNames("Seller");


        size1 = dd.size()/columns2.size();

      //  System.out.println("cc" + dd.size()/columns2.size());
//
//        String[] arr = new String[]{"1", "1", "1", "1"};
//        String[] arr1 = new String[]{"2", "2", "2", "2"};
//        String[] arr2 = new String[]{"3", "3", "3", "3"};
//        String[] arr3 = new String[]{"4", "4", "4", "4"};

        ObservableList<Column> columnsObservable = FXCollections.observableArrayList();


        //System.out.println(columns2.size());

        for (int j = 0; j < size1; j++) {
            String[] arr4 = new String[columns2.size()];
            List<String> list = new ArrayList<>();
            for (int i = j; i < dd.size(); i+=size1) {
                list.add(dd.get(i));
            }
            list.toArray(arr4);
            columnsObservable.add(new Column(arr4));
        }

//        columnsObservable.add(new Column(arr));
//        columnsObservable.add(new Column(arr1));
//        columnsObservable.add(new Column(arr2));
//        columnsObservable.add(new Column(arr3));


        ObservableList<TableColumn<Column, ?>> columns = table.getColumns();


       // for (int j =0; j < 3; j++) {
            for (int i = 0; i < columns2.size(); i++) {
                TableColumn<Column, String> column1 = new TableColumn<>(columns2.get(i).toString());

                int finalI = i;
                column1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Column, String>, ObservableValue<String>>() {
                    //  int count = 0;
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Column, String> cd) {
                        Column a = cd.getValue();

                        return Bindings.createObjectBinding(new Callable<String>() {
                            @Override
                            public String call() {
                                //  if(count>a.getName4().size()-1)count=0;
                                return a.getName4().get(finalI).trim();
                            }
                        }, a.dateProperty());
                    }
                });
                columns.add(column1);

          //  }
        }

        table.setItems(columnsObservable);

    }
    ;

    private void initializeDatabase() {
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
            List<String> columns = getColumnNames("Seller");
            System.out.println("rr" + columns.get(2));
            if (columns != null) {
                Map<String, Data> stringDataMap = executeQuery("SELECT * FROM Seller");
                if (stringDataMap != null) {

                    Iterator<String> iterator = stringDataMap.keySet().iterator();
                    ObservableList<String> items = FXCollections.observableArrayList();


                    while (iterator.hasNext()) {
                        String next = iterator.next();
                        Data data = stringDataMap.get(next);
                        dd.addAll(data.data);
                    }

                }

            }
        }catch (Exception e){}
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

    class Data {
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

package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;

public class Column {

    private final ObjectProperty<String> date = new SimpleObjectProperty<>();
    private String name;
    private String name2;
    private String name3;
    public List<String> name4;

    public Column(String[] name) {
        name4 = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            name4.add(name[i]);
        }
        System.out.println(name4.toString());

//        this.name = name;
//        this.name2 = name2;
//        this.name3 = name3;
//        this.name4 = name4;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public List<String> getName4() {
        return name4;
    }

    public void setName4(List<String> name4) {
        this.name4 = name4;
    }

    public String getDate() {
        return date.get();
    }

    public ObjectProperty<String> dateProperty() {
        return date;
    }
}

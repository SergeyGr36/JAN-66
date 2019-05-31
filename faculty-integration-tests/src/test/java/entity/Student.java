
package entity;

public class Student extends com.ra.course.janus.faculty.entity.Student {
    private static int id;
    private String name;
    private String surname;
    private String code;
    private String description;
    public Student() {
    }

    public Student(String name, String surname, int id, String code, String description) {
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.code = code;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public static String getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public String toString() {
        return "Student [" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", id=" + id +
                ']';
    }

}


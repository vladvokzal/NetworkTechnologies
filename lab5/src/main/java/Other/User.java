package Other;

public class User {

    public User(String name, int id) {
        this.userName = name;
        this.id = id;
    }

    public User(){
        this.userName = "";
        this.id = Constants.DEFAULT_ID;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.userName = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return userName;
    }


    private int id;
    private String userName;

}

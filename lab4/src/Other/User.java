package Other;

public class User {

    public User(String name, int id, String token, boolean isOnline) {
        this.userName = name;
        this.id = id;
        this.token = token;
        this.isOnline = isOnline;
    }

    public User(){
        this.userName = "";
        this.id = Constants.DEFAULT_ID;
        this.token = "";
        this.isOnline = false;
    }


    public void setToken(String token) {
        this.token = token;
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

    public String getToken(){
        return token;
    }

    public boolean isOnline() {
        return isOnline;
    }

    private int id;
    private String userName;
    private boolean isOnline;
    private String token;
}

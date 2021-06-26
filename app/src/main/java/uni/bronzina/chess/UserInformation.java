package uni.bronzina.chess;

class Userinformation {
    public String phoneNumber;
    public String name;
    public String email;

    public Userinformation(){
    }

    public Userinformation(String name, String email, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getUserPhoneNumber() {
        return phoneNumber;
    }
    public String getUserName() {
        return name;
    }
    public String getUserEmail() {
        return email;
    }
}

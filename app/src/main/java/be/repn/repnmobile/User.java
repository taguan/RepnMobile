package be.repn.repnmobile;

public class User {
    private String email;
    private String lastName;
    private String firstName;
    private String city;
    private String street;
    private String streetNumber;
    private Long birthDate;
    private String phone;
    private String mobile;

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress(){
        StringBuilder sb = new StringBuilder();
        if(street != null) sb.append(street).append(" ");
        if(streetNumber != null) sb.append(streetNumber).append(" ");
        if(city != null) sb.append(city);
        return sb.toString();
    }

    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        if(street != null) sb.append(street).append(", ");
        if(streetNumber != null) sb.append(streetNumber).append("\n");
        if(city != null) sb.append(city);
        return sb.toString();
    }
}

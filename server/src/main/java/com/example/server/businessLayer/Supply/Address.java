package com.example.server.businessLayer.Supply;

public class Address {

    private String name ;
    private String address;
    private String city;
    private String country;
    private String zip;

    public Address(String name, String address, String city, String country, String zip) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.zip = zip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }



    public boolean isLegal() {
        try{
            Integer.parseInt(zip);
        }
        catch (Exception e){return false;}
        return ((city!=null && !city.isEmpty()) & (address!=null && !address.isEmpty()) & (zip!=null && !zip.isEmpty()) & (country!=null && !country.isEmpty()));
    }
}

package org.example.univer.dao.models;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Person {
    private String firstName;
    private String lastName;
    private Gender gender;
    private String address;
    private String email;
    private String phone;
    private LocalDate birthday;

    public Person(String firstName, String lastName, Gender gender, String address, String email, String phone, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    public Person() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender.toString();
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddres() {
        return address;
    }

    public void setAddres(String addres) {
        this.address = addres;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;
        return firstName.equals(person.firstName) && lastName.equals(person.lastName) && gender.equals(person.gender) && address.equals(person.address) && email.equals(person.email) && phone.equals(person.phone) && birthday.equals(person.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, gender, address, email, phone, birthday);
    }
}

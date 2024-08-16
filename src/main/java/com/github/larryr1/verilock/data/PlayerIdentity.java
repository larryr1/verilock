package com.github.larryr1.verilock.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PlayerIdentity {

    private final String firstName;
    private final String lastName;
    private final String idNumber;
    private final String birthday;
    private final int age;
    private final int grade;
    private final String house;

    @JsonCreator
    @JsonPropertyOrder({ "firstName", "lastName", "idNumber", "birthday", "age", "house" }) // important!
    public PlayerIdentity(@JsonProperty("firstName") String firstName,
                          @JsonProperty("lastName") String lastName,
                          @JsonProperty("idNumber") String idNumber,
                          @JsonProperty("birthday") String birthday,
                          @JsonProperty("age") int age,
                          @JsonProperty("grade") int grade,
                          @JsonProperty("house") String house) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy");
        try {
            LocalDate.parse(birthday, dtf);
            this.birthday = birthday;
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e);
        }

        this.age = age;
        this.grade = grade;
        this.house = house;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("idNumber")
    public String getIdNumber() {
        return idNumber;
    }

    @JsonProperty("birthday")
    public String getBirthday() {
        return birthday;
    }

    @JsonProperty("age")
    public int getAge() {
        return age;
    }

    @JsonProperty("grade")
    public int getGrade() {
        return grade;
    }

    @JsonProperty("house")
    public String getHouse() {
        return house;
    }

    public String toString() {
        return this.getClass().getName() + String.format("[firstName=%s,lastName=%s,idNumber=%s,birthday=%s,age=%s,grade=%s,house=%s]", firstName, lastName, idNumber, birthday, age, grade, house);
    }
}

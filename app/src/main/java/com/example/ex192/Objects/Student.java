package com.example.ex192.Objects;

/**
 * Student class:
 * saves data about each student - its name, grade, class, and 2 vaccines info.
 * @author Ori Roitzaid <or1901 @ bs.amalnet.k12.il>
 * @version	1
 * @since 12/4/2024
 */
public class Student {
    private String privateName, familyName, id;
    private int grade, classNum;
    private boolean canImmune;
    private Vaccine firstVaccine, secondVaccine;

    public Student(String privateName, String familyName, String id, int grade, int classNum,
                   boolean canImmune, Vaccine firstVaccine, Vaccine secondVaccine) {
        this.privateName = privateName;
        this.familyName = familyName;
        this.id = id;
        this.grade = grade;
        this.classNum = classNum;
        this.canImmune = canImmune;
        this.firstVaccine = firstVaccine;
        this.secondVaccine = secondVaccine;
    }

    public Student() {
        this.privateName = "";
        this.familyName = "";
        this.id = "";
        this.grade = 0;
        this.classNum = 0;
        this.canImmune = false;
        this.firstVaccine = new Vaccine();
        this.secondVaccine = new Vaccine();
    }

    public String getPrivateName() {
        return this.privateName;
    }

    public String getFamilyName() {
        return this.familyName;
    }

    public String getId() {
        return this.id;
    }

    public int getGrade() {
        return this.grade;
    }

    public int getClassNum() {
        return this.classNum;
    }

    public boolean getCanImmune() {
        return this.canImmune;
    }

    public Vaccine getFirstVaccine() {
        return this.firstVaccine;
    }

    public Vaccine getSecondVaccine() {
        return this.secondVaccine;
    }

    public void setPrivateName(String name) {
        this.privateName = name;
    }

    public void setFamilyName(String name) {
        this.familyName = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }

    public void setCanImmune(boolean canImmune) {
        this.canImmune = canImmune;
    }

    public void setFirstVaccine(Vaccine vaccine) {
        this.firstVaccine = new Vaccine(vaccine.getPlaceTaken(), vaccine.getDate());
    }

    public void setSecondVaccine(Vaccine vaccine) {
        this.secondVaccine = new Vaccine(vaccine.getPlaceTaken(), vaccine.getDate());
    }
}

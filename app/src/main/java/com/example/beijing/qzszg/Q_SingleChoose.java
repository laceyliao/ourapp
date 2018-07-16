package com.example.beijing.qzszg;

import java.io.Serializable;

public class Q_SingleChoose implements Serializable{
    private int tid;
    private String describe;
    private String sectionA,sectionB,sectionC,sectionD,sectionE,sectionF;
    private String answer;
    private String userAnswer;
    private boolean collected;

    public Q_SingleChoose(){
        setDescribe("");
        setSectionA("");
        setSectionB("");
        setSectionC("");
        setSectionD("");
        setSectionE("");
        setSectionF("");
        setAnswer("");
        setUserAnswer("");
        setCollected(false);
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }

    public String getSectionA() {
        return sectionA;
    }

    public void setSectionA(String sectionA) {
        this.sectionA = sectionA;
    }

    public String getSectionB() {
        return sectionB;
    }

    public void setSectionB(String sectionB) {
        this.sectionB = sectionB;
    }

    public String getSectionC() {
        return sectionC;
    }

    public void setSectionC(String sectionC) {
        this.sectionC = sectionC;
    }

    public String getSectionD() {
        return sectionD;
    }

    public void setSectionD(String sectionD) {
        this.sectionD = sectionD;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSectionE() {
        return sectionE;
    }

    public void setSectionE(String sectionE) {
        this.sectionE = sectionE;
    }

    public String getSectionF() {
        return sectionF;
    }

    public void setSectionF(String sectionF) {
        this.sectionF = sectionF;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean getCollected() { return collected; }

    public void setCollected(boolean is) { this.collected = is; }
}

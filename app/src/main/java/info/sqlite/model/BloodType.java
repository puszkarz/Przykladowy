package info.sqlite.model;

public class BloodType {

    private int RHplus;
    private int antigenA;
    private int antigenB;

    public BloodType(int RHplus, int antigenA, int antigenB) {
        this.RHplus = RHplus;
        this.antigenA = antigenA;
        this.antigenB = antigenB;
    }

    //getters
    public int getRHplus() {
        return RHplus;
    }
    public int getAntigenA() {
        return antigenA;
    }
    public int getAntigenB() {
        return antigenB;
    }

    //setters
    public void setRHplus(int RHplus) {
        this.RHplus = RHplus;
    }
    public void setAntigenA(int antigenA) {
        this.antigenA = antigenA;
    }
    public void setAntigenB(int antigenB) {
        this.antigenB = antigenB;
    }

    public String toString() {
        String out = "";
        if (antigenA == 0 && antigenB == 0) out += "0";
        else {
            if (antigenA == 1) out += "A";
            if (antigenB == 1) out += "B";
        }

        if (RHplus == 1) out += "+";
        else out += "-";

        return out;
    }

}

package de.hssfds.bikeshop;

public class EBike {
    private double preis;
    private double akkukap;

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public void setAkkukap(double akkukap) {
        this.akkukap = akkukap;
    }

    public void setDrehmoment(double drehmoment) {
        this.drehmoment = drehmoment;
    }

    public void setProduktname(String produktname) {
        this.produktname = produktname;
    }

    private double drehmoment;
    private String produktname;
    private int zustand;
    private int id;

    public EBike(String produktname, double preis, double akkukap, double drehmoment, int zustand, int id) {
        this.preis = preis;
        this.akkukap = akkukap;
        this.drehmoment = drehmoment;
        this.produktname = produktname;
        this.zustand = zustand;
        this.id = id;
    }

    public double getPreis() {
        return preis;
    }

    public double getAkkukap() {
        return akkukap;
    }

    public double getDrehmoment() {
        return drehmoment;
    }

    public String getProduktname() {
        return produktname;
    }

    public int getZustand() {
        return zustand;
    }

    public void setZustand(int zustand) {
        this.zustand = zustand;
    }

    public int getId() {return id;}

    public String toString() {

        return "Name: " + produktname+ "\n"+
                "Preis: " + preis + " Euro \n" +
                "Akkukapazität: " + akkukap + "Wh\n" +
                "Drehmoment: " + drehmoment+ "Nm\n" +
                "Zustand: "+ zustand + "%"+
                "id: "+ id;
    }

    public void reparieren(){

        zustand=100;
    }

    public double verkaufsPreis(){

        return preis*(zustand/100.0);
    }

    public void info(String produktname, double preis, double akkukap,double drehmoment, int zustand){
        this.produktname = produktname;
        this.preis = preis;
        this.akkukap = akkukap;
        this.drehmoment = drehmoment;
        this.zustand = zustand;
        this.id = id;
    }
}





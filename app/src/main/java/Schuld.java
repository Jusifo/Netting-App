public class Schuld {

    private Person Schuldner;
    private Person Empfaenger;
    private double Summe;

    public Schuld(Person schuldner, Person empfaenger, double summe) {
        Schuldner = schuldner;
        Empfaenger = empfaenger;
        Summe = summe;
    }

    private void edit(double aenderung){
        this.Summe += aenderung;
    }

    public Person getSchuldner() {
        return Schuldner;
    }

    public Person getEmpfaenger() {
        return Empfaenger;
    }

    public double getSumme() {
        return Summe;
    }

    public void setSumme(double summe) {
        Summe = summe;
    }
}

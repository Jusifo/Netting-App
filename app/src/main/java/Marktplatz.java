import java.util.*;


public class Marktplatz {

    private List<Person> personList;
    private List<Debt> Schuldenbeziehungen;

    public Marktplatz(List<Person> personList) {
        this.personList = personList;
        this.Schuldenbeziehungen = new ArrayList<>();
    }

    public Marktplatz() {
        this.personList = new ArrayList<>();
        this.Schuldenbeziehungen = new ArrayList<>();
    }

    public void berechnen(){
        boolean weiter = true;
        Scanner scanner = new Scanner(System.in);
        int peoplecount = 2;

        System.out.println("Hallo, für wie viele Leute willst du heute die Nettoschulden begleichen?");

        while (true){
            try {
                peoplecount = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Perfekt lass uns anfangen!");
                break;
            }
            catch (InputMismatchException e){
                System.out.println("Du solltest eine Nummer eingeben!");
                scanner.next();
            }
        }

        if (peoplecount <= 1){
            System.out.println("Du Witzbold");
            return;
        }
        if (peoplecount > 10){
            System.out.println("Vergisses");
            return;
        }

        for (int i = 0; i < peoplecount; i++){
            if (i == 0)
                System.out.println("Alles klar, mit wem wollen wir anfangen?");
            else if (i == peoplecount - 1) {
                System.out.println("Perfekt und nun zur letzten Person!");
            } else
                System.out.println("Alles klar! Gebe jetzt den Namen einer weiteren Person an: ");
            personList.add(new Person(scanner.nextLine()));
        }
        System.out.println("Perfekt, danke für die Personen. Ich werde dich jetzt danach fragen, was jeder jeweils jedem anderen Schuldet und dir danach sagen, welche Transaktionen durchgeführt werden müssen :)");


        int anzahlBeziehungen = (personList.size() * (personList.size() - 1)) / 2;
        int anzahlDreierPaare = calculateCombinations(peoplecount, 3);
        int anzahlDreierBEZIEHUNGEN = anzahlDreierPaare * 6;

        System.out.println("Insgesammt werde ich dich nach " + anzahlBeziehungen * 2 + " Schuldbeziehungen Fragen. \nAußerdem gibt es " + anzahlDreierPaare + " mögliche 3er Paare. \n[Enter zum Fortfahren!]");
        scanner.nextLine();

        int schuld = 0;
        for (int i = 0; i < personList.size(); i++) {
            for (int j = 0; j < personList.size(); j++) {
                if (j == i)
                    continue;

                String personA = personList.get(i).getName();
                String personB = personList.get(j).getName();

                System.out.println("Wie viel geld schuldet " + personA + " noch " + personB + "?");
                schuld = scanner.nextInt();
                scanner.nextLine();
                Schuldenbeziehungen.add(new Debt(personList.get(i), personList.get(j), schuld));
            }
        }
        System.out.println();
        System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
        System.out.println("|                                   Grundsätzlich                                   |");
        System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
        System.out.println();
        for (int i = 0; i < Schuldenbeziehungen.size(); i++) {
            Debt x = Schuldenbeziehungen.get(i);
            System.out.println(x.getDebtor().getName() + " schuldet " + x.getCreditor().getName() + " " + x.getAmount() + "€.");
        }


        boolean goon = true;
        Person a;
        int alpha;
        Person b;
        int beta;
        Person c;
        int gamma;

        Debt A;
        Debt B;
        Debt C;

        int mod = anzahlDreierBEZIEHUNGEN / personList.size();
        int modus = personList.size() - 2;
        int counter = 0;
        boolean firsttime = true;

        while (goon){
            goon = false;
            for (int i = 0; i < Schuldenbeziehungen.size(); i++) {
                Debt current = Schuldenbeziehungen.get(i);
                if (current.getAmount() != 0.0){
                    for (int i1 = 0; i1 < Schuldenbeziehungen.size(); i1++) {
                        Debt competator = Schuldenbeziehungen.get(i1);
                        if (current.getDebtor().equals(competator.getCreditor()) && current.getCreditor().equals(competator.getDebtor())){
                            if (competator.getAmount() != 0.0){
                                doubleswitch(current, competator);
                            }
                            break;
                        }
                    }
                }
            }

            if (firsttime) {
                System.out.println();
                System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
                System.out.println("|                                Nach zweier Netting                                |");
                System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
                System.out.println();
                for (int i = 0; i < Schuldenbeziehungen.size(); i++) {
                    Debt x = Schuldenbeziehungen.get(i);
                    System.out.println(x.getDebtor().getName() + " schuldet " + x.getCreditor().getName() + " " + x.getAmount() + "€.");
                }
                firsttime = false;
            }
            else
                System.out.println("New Line!");

            //TODO ------------------------------------------------------------------------------------------------------------------------------  TODO
            //In diesem Abschnitt muss jede mögliche 3er Kombination gefunden werden die es für die eingegebenen Namen gibt und überprüft werden ob
            // zwei Transaktionen die 'in eine Richtung gehen' vorliegen

            for (int i = 0; i < anzahlDreierBEZIEHUNGEN; i++) {
                //1.
                alpha = i/mod;
                a = personList.get(alpha);

                //2.
                beta = (i%mod)/modus;
                if (beta >= alpha) {
                    beta++;
                }
                b = personList.get(beta);

                //3.
                gamma = i%modus;
                if (gamma == 0) {
                    counter = 0;
                }
                gamma += counter;
                if (alpha == gamma || beta == gamma) {
                    gamma++;
                    counter++;
                }
                if (alpha == gamma || beta == gamma) {
                    gamma++;
                    counter++;
                }
                c = personList.get(gamma);

                A = Schuldfinder(a,b);
                B = Schuldfinder(b,c);
                C = Schuldfinder(a,c);

                goon = tripleswitch(A,B,C);
                if (goon) {
                    break;
                }

            }
            //TODO - END ------------------------------------------------------------------------------------------------------------------------------  TODO

        }

        System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
        System.out.println("|                               Nach finalem Netting                                |");
        System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
        for (int i = 0; i < Schuldenbeziehungen.size(); i++) {
            Debt x = Schuldenbeziehungen.get(i);
            System.out.println(x.getDebtor().getName() + " schuldet " + x.getCreditor().getName() + " " + x.getAmount() + "€.");
        }
        scanner.close();
    }

    // Methode zur Berechnung der Kombinationen
    public static int calculateCombinations(int n, int k) {
        if (k > n) {
            return 0; // Keine Kombinationen möglich, wenn k größer als n ist
        }
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    // Methode zur Berechnung der Fakultät
    public static int factorial(int number) {
        if (number == 0) {
            return 1; // Fakultät von 0 ist 1
        }
        int result = 1;
        for (int i = 1; i <= number; i++) {
            result *= i;
        }
        return result;
    }

    public Debt Schuldfinder (Person a, Person b){
        for (Debt current : Schuldenbeziehungen) {
            if (current.getDebtor().equals(a) && current.getCreditor().equals(b))
                return current;
        }
        System.err.println("Kein Schuldverhältnis gefunden hä?");
        return null;
    }

    public void doubleswitch(Debt a, Debt b){
        if (b.getAmount() > a.getAmount()){
            Debt temp = a;
            a = b;
            b = temp;
        }
        a.setAmount(a.getAmount() - b.getAmount());
        b.setAmount(0);
    }

    public boolean tripleswitch(Debt a, Debt b, Debt c){
        boolean change = false;
        if (a.getAmount() != 0 && b.getAmount() != 0){
            change = true;
            double sum = Math.min(a.getAmount(), b.getAmount());
            a.setAmount(a.getAmount()-sum);
            b.setAmount(b.getAmount()-sum);
            c.setAmount(c.getAmount()+sum);
        }
        return change;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }



}

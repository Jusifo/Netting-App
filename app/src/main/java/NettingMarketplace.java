import java.util.*;

// Direct translation of Marktplatz.java
public class NettingMarketplace {

    private List<Person> personList;
    private List<Debt> debtRelationships; // Schuld -> Debt, Schuldenbeziehungen -> debtRelationships
    private int numberOfTripletRelationships;
    private int numberOfTriplets;
    private int numberOfRelationships;

    public NettingMarketplace(List<Person> personList) {
        this.personList = personList;
        this.debtRelationships = new ArrayList<>();
    }

    public NettingMarketplace() {
        this.personList = new ArrayList<>();
        this.debtRelationships = new ArrayList<>();
    }

    public void run(){ // berechnen -> calculate

        count_debts();
        list_debts();


        //Start of Two Way Netting
        boolean go_on = true;
        Person a;
        Person b;
        Person c;

        int id_person1;
        int id_person2;
        int id_person3;

        Debt A;
        Debt B;
        Debt C;

        int mod = numberOfTripletRelationships / personList.size();
        int modus = personList.size() - 2;
        int counter = 0;
        boolean firstTime = true; // firsttime -> firstTime

        do{
            go_on = false;
            for (int i = 0; i < debtRelationships.size(); i++) {
                Debt current = debtRelationships.get(i);
                if (current.getAmount() != 0.0){
                    debtRelationships.stream()
                            .filter(competitor -> current.getDebtor().equals(competitor.getCreditor()) && current.getCreditor().equals(competitor.getDebtor()))
                            .findFirst()
                            .ifPresent(competitor -> {
                                if (competitor.getAmount() > 0) {
                                    doubleSwitch(current, competitor);
                                }
                            });
                }
            }

            if (firstTime) {
                firstTime = false;
                System.out.println();
                System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
                System.out.println("|                                After 2-way netting                                |"); // Translated
                System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
                System.out.println();
                for (int i = 0; i < debtRelationships.size(); i++) {
                    Debt x = debtRelationships.get(i);
                    System.out.println(x.getDebtor().getName() + " owes " + x.getCreditor().getName() + " " + x.getAmount() + "€.");
                }
            }
            else
                System.out.println("New Line!");

            //In this section, every possible triplet combination for the entered names is found and checked
            //if two transactions 'go in one direction' A->B->C

            for (int i = 0; i < numberOfTripletRelationships; i++) {
                //1.
                id_person1 = i/mod;
                a = personList.get(id_person1);

                //2.
                id_person2 = (i%mod)/modus;
                if (id_person2 >= id_person1) {
                    id_person2++;
                }
                b = personList.get(id_person2);

                //3.
                id_person3 = i%modus;
                if (id_person3 == 0) {
                    counter = 0;
                }
                id_person3 += counter;
                if (id_person1 == id_person3 || id_person2 == id_person3) {
                    id_person3++;
                    counter++;
                }
                if (id_person1 == id_person3 || id_person2 == id_person3) {
                    id_person3++;
                    counter++;
                }
                c = personList.get(id_person3);

                A = findDebt(a,b);
                B = findDebt(b,c);
                C = findDebt(a,c);

                go_on = transitive_elimination(A,B,C);
            }
        } while (go_on);

        System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
        System.out.println("|                                After final netting                                |"); // Translated
        System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
        for (int i = 0; i < debtRelationships.size(); i++) {
            Debt x = debtRelationships.get(i);
            System.out.println(x.getDebtor().getName() + " owes " + x.getCreditor().getName() + " " + x.getAmount() + "€.");
        }

    }

    public void count_debts(){
        int peopleCount;
        System.out.println("Hello, for how many people do you want to settle the net debts today?"); // Translated

        Scanner scanner = new Scanner(System.in);
        while (true){
            try {
                peopleCount = scanner.nextInt();
                scanner.nextLine();
                if (peopleCount <= 1){
                    System.out.println("Netting requires at least two people.");
                    return;
                }
                if (peopleCount > 10){
                    System.out.println("This application supports a maximum of 10 people.");
                    return;
                }
                System.out.println("Perfect, let's get started!");
                break;
            }
            catch (InputMismatchException e){
                System.out.println("You should enter a number!");
                scanner.next();
            }
        }


        for (int i = 0; i < peopleCount; i++){
            if (i == 0)
                System.out.println("Alright, with whom shall we start?");
            else if (i == peopleCount - 1) {
                System.out.println("Perfect, and now for the last person!");
            } else
                System.out.println("Alright! Now enter the name of another person:");
            personList.add(new Person(scanner.nextLine()));
        }
        System.out.println("Perfect, thank you for the names. I will now ask you what each person owes everyone else, and then I will tell you which transactions need to be made :)");


        numberOfRelationships = (personList.size() * (personList.size() - 1)) / 2;
        numberOfTriplets = calculateCombinations(peopleCount, 3);
        numberOfTripletRelationships = numberOfTriplets * 6;

        System.out.println("In total, I will ask you about " + numberOfRelationships * 2 + " debt relationships. \nAdditionally, there are " + numberOfTriplets + " possible triplets. \n\n[Press Enter to continue!]"); // Translated
        scanner.nextLine();

        int debtAmount;
        for (int i = 0; i < personList.size(); i++) {
            for (int j = 0; j < personList.size(); j++) {
                if (j == i)
                    continue;

                String personA = personList.get(i).getName();
                String personB = personList.get(j).getName();

                System.out.println("How much money does " + personA + " still owe " + personB + "?");
                debtAmount = scanner.nextInt();
                scanner.nextLine();
                debtRelationships.add(new Debt(personList.get(i), personList.get(j), debtAmount));
            }
        }
        scanner.close();
    }

    public void list_debts(){
        System.out.println("\n --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
        System.out.println("|                                   Basically                                           |");
        System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- \n");

        for (int i = 0; i < debtRelationships.size(); i++) {
            Debt x = debtRelationships.get(i);
            System.out.println(x.getDebtor().getName() + " owes " + x.getCreditor().getName() + " " + x.getAmount() + "€."); // getSchuldner -> getDebtor, etc.
        }
    }

    // Method for calculating combinations
    public static int calculateCombinations(int n, int k) {
        if (k > n) {
            return 0; // No combinations possible if k is greater than n
        }
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    // Method for calculating factorial
    public static int factorial(int number) {
        if (number == 0) {
            return 1; // Factorial of 0 is 1
        }
        int result = 1;
        for (int i = 1; i <= number; i++) {
            result *= i;
        }
        return result;
    }

    public Debt findDebt (Person a, Person b){
        for (Debt current : debtRelationships) {
            if (current.getDebtor().equals(a) && current.getCreditor().equals(b))
                return current;
        }
        System.err.println("No debt relationship found");
        return null;
    }

    public void doubleSwitch(Debt a, Debt b){
        if (b.getAmount() > a.getAmount()){
            Debt temp = a;
            a = b;
            b = temp;
        }
        a.setAmount(a.getAmount() - b.getAmount());
        b.setAmount(0);
    }

    public boolean transitive_elimination(Debt a, Debt b, Debt c){
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

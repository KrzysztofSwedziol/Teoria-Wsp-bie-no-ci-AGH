import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Zadanie 1
class FlagaRewolwerowca {
    public Boolean flag;

    public FlagaRewolwerowca() {
        this.flag = false;  // Now it's mutable
    }
}


class Rewolwerowiec extends Thread {
    private String name;
    private int startCount;
    private int delay;
    private FlagaRewolwerowca flag;

    public Rewolwerowiec(String name, int startCount, int delay, FlagaRewolwerowca flag) {
        this.name = name;
        this.startCount = startCount;
        this.delay = delay;
        this.flag = flag;
    }

    @Override
    public void run() {
        try {
            for (int i = startCount; i > 0; i--) {
                if (this.flag.flag) {
                    break;
                }
                System.out.println(name + ": " + i);
                Thread.sleep(delay);
            }
            if (!this.flag.flag) {
                System.out.println(name + ": Pif! Paf!");
            }
            this.flag.flag = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

//zadanie 2
class Klasa extends Thread {
    private static int value;

    public Klasa() {
        value = 0;
    }

    public void run() {
        for (int i = 0; i < 1000; i++) {
            increment();
            incrementStatic();
        }
        System.out.println("Final value: " + value);
    }

    public synchronized void increment() {

        value++;
    }

    public static synchronized void incrementStatic() {
        value++;
    }
}

//zadanie 3
class doubleChecker extends Thread {
    public double value = 0.0;

    public void increment(){
        this.value += 1.0;
    }
}
class StringChecker extends Thread {
    public String value = "";
}
class Incrementor extends Thread {
    public doubleChecker value;
    public Incrementor(doubleChecker value) {
        this.value = value;
    }
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            value.increment();
        }
        System.out.println("Final value: " + value.value);
    }
}

class StringEditor extends Thread {
    public StringChecker value;
    public StringEditor(StringChecker value) {
        this.value = value;
    }
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            this.value.value = this.value.value + "1";
        }
        System.out.println("Final value length: " + value.value.length());
    }
}
//zadanie 4
class ContainedValue extends Thread {
    public int value = 0;

    public void increment(){
        this.value += 1;
    }
}
class ValueIncrementor extends Thread {
    public ContainedValue value;
    public Semafor semafor;
    public ValueIncrementor(ContainedValue value, Semafor semafor) {
        this.value = value;
        this.semafor = semafor;
    }
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            this.semafor.Blokuj();
            value.increment();
            this.semafor.Zwolnij();
        }
        //System.out.println("Final value: " + value.value);
    }
}

class ContainedValue2 extends Thread {
    public int value = 0;

    public void increment(){
        this.value += 1;
    }
}

class ValueIncrementor2 extends Thread {
    public ContainedValue2 value1;
    public ContainedValue2 value2;
    public Semafor semafor;
    public ValueIncrementor2(ContainedValue2 value1, ContainedValue2 value2, Semafor semafor) {
        this.value1 = value1;
        this.value2 = value2;
        this.semafor = semafor;
    }
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            this.semafor.semaforDecrement(this.value1, this.value2);
            this.semafor.semaforIncrement();
        }
    }
}


class Semafor {
    private boolean stan = true;
    public int accessValue;
    public Semafor() {
        this.accessValue = 0;
    }
    public synchronized void Blokuj(){
        if(this.stan == false){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.stan = false;
    }
    public synchronized void Zwolnij() {
        this.stan = true;
        notify();
    }
    public synchronized void semaforDecrement(ContainedValue2 value1, ContainedValue2 value2) {
        if(this.accessValue <= 0){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if(this.accessValue == 1){
            value1.increment();
        }
        if(this.accessValue == 2){
            value2.increment();
        }
        this.accessValue--;
    }
    public synchronized void semaforIncrement() {
        this.accessValue++;
        notify();
    }
}

//zadanie 5
class AllVotes{
    public int votes;
    public int votesCounted = 0;
    public AllVotes(int votes){
        this.votes = votes;
    }
    public synchronized void incrementVotes(){
        votesCounted++;
    }
}
class Komitet extends Thread {
    public int valueGiven;
    public AllVotes allVotes;

    public Komitet(int valueGiven, AllVotes allVotes) {
        this.valueGiven = valueGiven;
        this.allVotes = allVotes;
    }
    public void run() {
        for(int i=0; i<valueGiven; i++){
            allVotes.incrementVotes();
        }
    }

}

class CurrBingoNumber{
    public int value = 0;
    public void setValue(int value){
        this.value = value;
    }
}

class Bingo extends Thread {
    private static List<Bingo> gracze = new ArrayList<Bingo>();
    private List<Integer> playerStats;
    public boolean isBingo;

    public Bingo(List<Bingo> gracze, Boolean isBingo) {
        this.gracze = gracze;
        this.isBingo = isBingo;
        generatePlayerNumbers();
    }
    public void generateValue(){
        Random random = new Random();
        int randomValue = random.nextInt(10) + 1;
    }
    public void run() {
        if(isBingo == false){

        }else{
            
        }
    }
    public void generatePlayerNumbers(){
        List<Integer> randomIntegers = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int randomNumber = random.nextInt(10) + 1;
            randomIntegers.add(randomNumber);
        }
        this.playerStats = randomIntegers;
    }
}

class player extends Thread {

}


public class Main {
    public static void main(String[] args) {
//        //zadanie 1
//        FlagaRewolwerowca flag = new FlagaRewolwerowca();
//
//
//        Rewolwerowiec r1 = new Rewolwerowiec("Rewolwerowiec 1", 5, 1000, flag);
//        Rewolwerowiec r2 = new Rewolwerowiec("Rewolwerowiec 2", 3, 2000, flag);
//        Rewolwerowiec r3 = new Rewolwerowiec("Rewolwerowiec 3", 7, 500, flag);
//
//        r1.start();
//        r2.start();
//        r3.start();

//        //zadanie 2
//        Klasa k = new Klasa();
//        Thread thread1 = new Thread(k);
//        Thread thread2 = new Thread(k);
//        thread1.start();
//        thread2.start();

        //zadanie 3
        //Zapis do zmiennych tyfu Float i Double nie jest atomowy. Oznacza to że
        //kilka wątków edytujących daną zmienną tego typu może coś zepsuć.
//        doubleChecker checker = new doubleChecker();
//        Incrementor i1 = new Incrementor(checker);
//        Incrementor i2 = new Incrementor(checker);
//        Incrementor i3 = new Incrementor(checker);
//
//        i1.start();
//        i2.start();
//        i3.start();

        //w przypadku działania powinniśmy otrzymać liczbę 3000 a tak się nie dzieje.

        //Analogicznie ze stringami :
        //za każdym razem w wątku tworzymy nowego stringa z dodaną wartością wydłużającą jego
        //długość o 1. Po takich operacjach, długość stringa powinna wynosić 3000.

//        StringChecker checker = new StringChecker();
//        StringEditor e1 = new StringEditor(checker);
//        StringEditor e2 = new StringEditor(checker);
//        StringEditor e3 = new StringEditor(checker);
//
//        e1.start();
//        e2.start();
//        e3.start();
        //Przez zmianę referencji, odczzyt nie pokazuje pełni idei natomiast same stringi są niemutowalne,
        //co może oznaczać że operacje na nich są atomowe

//        //zadanie 4
//        //podpunkt a
//        //Po skończeniu się programu, wartość powinna wynosić 3000 i tak właśnie się dzieje.
//        ContainedValue containedValue = new ContainedValue();
//        Semafor semafor = new Semafor();
//        ValueIncrementor v1 = new ValueIncrementor(containedValue, semafor);
//        ValueIncrementor v2 = new ValueIncrementor(containedValue, semafor);
//        ValueIncrementor v3 = new ValueIncrementor(containedValue, semafor);
//
//        v1.start();
//        v2.start();
//        v3.start();
//        try {
//            v1.join();
//            v2.join();
//            v3.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(containedValue.value);
//
//        //podpunkt b
//        //W tym podpunkcie stworzyłem dwie wartości value1 i value2. Gdy semafor ma pełną wartość,
//        //inkrementuje jedną value a gdy nie ma pełnej ale też nie jest ona równa 0, wtedy inkrementuję drugą.
//        //W takim rozwiązaniu aby udowodnić że semafor działa poprawnie, value1 + value2 powinno nam dać 3000
//        //i tak się właśnie dzieje
//        ContainedValue2 value1 = new ContainedValue2();
//        ContainedValue2 value2 = new ContainedValue2();
//        Semafor semafor2 = new Semafor();
//
//        ValueIncrementor2 v2_1 = new ValueIncrementor2(value1, value2, semafor2);
//        ValueIncrementor2 v2_2 = new ValueIncrementor2(value1, value2, semafor2);
//        ValueIncrementor2 v2_3 = new ValueIncrementor2(value1, value2, semafor2);
//
//        v2_1.start();
//        v2_2.start();
//        v2_3.start();
//        try {
//            v2_1.join();
//            v2_2.join();
//            v2_3.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.printf("Pierwsza wartość to: %d%n", value1.value);
//        System.out.printf("Druga wartość to: %d%n", value2.value);
//        int sum = value1.value + value2.value;
//        System.out.printf("Suma value1 i value2 to: %d%n", sum);

        //zadanie 5
        //w tym przypadku aby sprawdzić działanie, suma policzonych głosów powinna być równa sumie
        //wszystkich z liczonych komitetów wartości i tak się dzieje.
        AllVotes allVotes = new AllVotes(1000);
        Komitet komitet1 = new Komitet(275, allVotes);
        Komitet komitet2 = new Komitet(425, allVotes);
        Komitet komitet3 = new Komitet(300, allVotes);

        komitet1.start();
        komitet2.start();
        komitet3.start();

        try {
            komitet1.join();
            komitet2.join();
            komitet3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(allVotes.votesCounted);

        //podpunkt b

    }
}

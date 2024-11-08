
//zadanie 1
class Producer extends Thread{
    public DishesTable table;
    public Producer(DishesTable table){
        this.table = table;
    }
    public void run(){
        while(true){
            this.table.produce();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Consumer extends Thread{
    public DishesTable table;
    public int name;
    public Consumer(DishesTable table, int name){
        this.table = table;
        this.name = name;
    }
    public void run(){
        while(true){
            this.table.consume(this);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}


class DishesTable{
    public int foodReadyToEat;
    public int produced_counter = 0;
    public int consumed_number = 0;
    public int max_buffor = 5;

    public synchronized void produce(){
        if(foodReadyToEat < max_buffor){
            foodReadyToEat += 1;
            produced_counter += 1;
            System.out.println("Meal number: " + this.produced_counter + " is ready to eat");
            //System.out.println("Buffor value: " + this.foodReadyToEat);
            if(foodReadyToEat >= max_buffor){
                System.out.println("Buffor pełny");
            }
            notify();
        }else{
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public synchronized void consume(Consumer consumer){
        if(foodReadyToEat > 0){
            if(foodReadyToEat >= max_buffor){
                notify();
            }
            foodReadyToEat--;
            consumed_number ++;
            System.out.println("Consumer number:  " + consumer.name + " ate food number: " + consumed_number);
        }else{
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}



public class Main {
    public static void main(String[] args) {
        //zadanie 1
//        DishesTable table = new DishesTable();
//        Producer producer = new Producer(table);
//        Consumer consumer = new Consumer(table, 1);
//        producer.start();
//        consumer.start();

        //zadanie 2
//        DishesTable table = new DishesTable();
//        Consumer consumer1 = new Consumer(table, 1);
//        Consumer consumer2 = new Consumer(table, 2);
//        Consumer consumer3 = new Consumer(table, 3);
//        Producer producer = new Producer(table);
//
//        producer.start();
//        consumer1.start();
//        consumer2.start();
//        consumer3.start();

        //zadanie 3
//        DishesTable dishesTable = new DishesTable();
//        Producer producer1 = new Producer(dishesTable);
//        Producer producer2 = new Producer(dishesTable);
//        Producer producer3 = new Producer(dishesTable);
//        Consumer consumer1 = new Consumer(dishesTable, 1);
//
//        producer1.start();
//        producer2.start();
//        producer3.start();
//        consumer1.start();
        //zadanie 4
//        DishesTable table = new DishesTable();
//        Producer producer1 = new Producer(table);
//        Producer producer2 = new Producer(table);
//        Producer producer3 = new Producer(table);
//        Consumer consumer1 = new Consumer(table, 1);
//        Consumer consumer2 = new Consumer(table, 2);
//        //Consumer consumer3 = new Consumer(table, 3);
//
//        producer1.start();
//        producer2.start();
//        producer3.start();
//        consumer1.start();
//        consumer2.start();
//        consumer3.start();

    }
}
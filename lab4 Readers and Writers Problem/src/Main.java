import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Condition;
import java.lang.Thread;
import java.lang.Runnable;

class MyObject{
    public Object value;
}


class Node {
    Object value;
    Node next;
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    Node(Object value) {
        this.value = value;
        this.next = null;
    }
}

class LinkedList {
    private Node head;
    private int size = 0;
    private final int maxSize;
    private final ReentrantLock listLock = new ReentrantLock();
    private final Condition notFull = listLock.newCondition();
    private final Condition notEmpty = listLock.newCondition();

    public LinkedList(int maxSize) {
        this.maxSize = maxSize;
        this.head = new Node(null);
    }

    public boolean contains(Object o) {
        Node current = head;
        Node next = current.next;

        while (next != null) {
            if(current.lock.getReadLockCount() == 0){
                current.lock.readLock().lock();
            }
            next.lock.readLock().lock();

            try {
                if (next.value.equals(o)) {
                    current.lock.readLock().unlock();
                    next.lock.readLock().unlock();
                    return true;
                }

                if(current.lock.getReadLockCount() != 0){
                    current.lock.readLock().unlock();
                }
                current = next;
                next = next.next;

            } finally {
                if (next == null && current != null) {
                    current.lock.readLock().unlock();
                }
            }
        }
        return false;
    }

    public boolean remove(Object o) {
        listLock.lock();
        try {
            while (size == 0) {
                notEmpty.await();
            }

            Node current = head;
            Node prev = null;

            while (current != null) {
                current.lock.readLock().lock();
                //current.lock.writeLock().lock();

                if(prev != null){
                    if(prev.lock.getReadLockCount() != 0){
                        prev.lock.readLock().unlock();
                    }
//                    if(prev.lock.isWriteLocked()){
//                        prev.lock.writeLock().unlock();
//                    }
                    prev.lock.writeLock().lock();
                }

                try {
                    if(current.value != null){
                        if (current.value.equals(o)) {
                            if(prev == null){
                                head = new Node(null);
                            }
                            if (prev.value != null) {
                                //prev.lock.writeLock().lock();
                                prev.next = current.next;
                                prev.lock.writeLock().unlock();
                                current.lock.readLock().unlock();
                            } else {
                                //prev.lock.writeLock().lock();
                                prev.next = current.next;
                                prev.lock.writeLock().unlock();
                                current.lock.readLock().unlock();
                            }
//                            if(prev != null){
//                                prev.lock.readLock().unlock();
//                            }
                            //current.lock.readLock().unlock();
                            //current.lock.writeLock().unlock();
                            size--;
                            notFull.signal();
                            return true;
                        }
                    }
                    if(prev != null){
                        prev.lock.writeLock().unlock();
                    }
                    prev = current;
                    current = current.next;
                } finally {
                    if(prev != null){
                        //prev.lock.writeLock().unlock();
                        //prev.lock.readLock().unlock();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            listLock.unlock();
        }
        return false;
    }

    public void add(Object o) {
        listLock.lock();
        try {
            while (size >= maxSize) {
                notFull.await();
            }

            Node newNode = new Node(o);
            if (head == null) {
                head = newNode;
            } else {
                Node current = head;
                while (current.next != null) {
                    current.lock.readLock().lock();
                    try {
                        current.lock.readLock().unlock();
                        current = current.next;
                    } finally {
                        //current.lock.readLock().unlock();
                    }
                }
                current.lock.writeLock().lock();
                try {
                    current.next = newNode;
                    current.lock.writeLock().unlock();
                } finally {
                }
            }
            size++;
            notEmpty.signal();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            listLock.unlock();
        }
    }
}

class Reader extends Thread{
    private LinkedList list;
    private Object element;

    Reader(LinkedList list, Object element) {
        this.list = list;
        this.element = element;
    }

    @Override
    public void run() {
        System.out.println("Czy lista zawiera '" + element + "'? " + list.contains(element));
    }

}


class Writer extends Thread{
    private LinkedList list;
    private Object element;

    Writer(LinkedList list, Object element) {
        this.list = list;
        this.element = element;
    }
    public void edit(Object o, MyObject object) {
        object.value = o;
    }

    @Override
    public void run() {
        list.add(element);
        //this.edit(this.element, new MyObject());
        System.out.println("Dodano: " + element);
    }
}


class Remover extends Thread{
    private LinkedList list;
    private Object element;

    Remover(LinkedList list, Object element) {
        this.list = list;
        this.element = element;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        list.remove(element);
        System.out.println("Usunięto: " + element);
    }
}



public class Main {
    public static void main(String[] args) {
        int amount = 10;
        LinkedList list = new LinkedList(amount);

        ArrayList<Reader> readers = new ArrayList<>();
        ArrayList<Writer> writers = new ArrayList<>();
        ArrayList<Remover> removers = new ArrayList<>();


        for(int i=0; i<amount; i++){
            MyObject elementX = new MyObject();
            Writer writerX = new Writer(list, elementX);
            writers.add(writerX);
            Reader readerX = new Reader(list, elementX);
            readers.add(readerX);
            Remover removerX = new Remover(list, elementX);
            removers.add(removerX);
        }
        for(int i=0; i<amount; i++){
            writers.get(i).start();
            readers.get(i).start();
            removers.get(i).start();
        }
        for(int i=0; i<amount; i++){
            try {
                writers.get(i).join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                readers.get(i).join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                removers.get(i).join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
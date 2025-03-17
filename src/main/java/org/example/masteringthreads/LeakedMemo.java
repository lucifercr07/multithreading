package org.example.masteringthreads;

class Test {
    private int x;
    private int y;

    public void f() {
        int a = x;
        y = 3;
        System.out.println("a: " + a + " " + Thread.currentThread().getName());
    }

    public void g() {
        int b = y;
        x = 4;
        System.out.println("b: " + b + " " + Thread.currentThread().getName());
    }
}

public class LeakedMemo {
    public static void main(String[] args) throws InterruptedException {
        Test t = new Test();
        Runnable r = () -> {
          t.f();
          t.g();
        };
        Thread t1 = new Thread(r, "t1");
        Thread t2 = new Thread(r, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
package service;

import entity.Element;

import java.util.Queue;

public class SideProcess implements Runnable {
    private final Queue<Element> queue;

    public SideProcess(Queue<Element> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        queue.add(new Element(1, 1));
        queue.add(new Element(2, 1));
        queue.add(new Element(3, 1));
        queue.add(new Element(4, 1));
        queue.add(new Element(5, 1));
        queue.add(new Element(6, 1));
        queue.add(new Element(7, 1));
        queue.add(new Element(8, 1));
        queue.add(new Element(9, 1));
        queue.add(new Element(1, 2));
        queue.add(new Element(2, 2));
        queue.add(new Element(11, 1));
        queue.add(new Element(1, 3));
        queue.add(new Element(3, 2));
        queue.add(new Element(2, 3));
        queue.add(new Element(15, 1));
    }
}
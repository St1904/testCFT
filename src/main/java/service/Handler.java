package service;

import entity.Element;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.*;

public class Handler implements Runnable {
    private final Queue<Element> queue;
    private final int nThreads;
    private volatile boolean keepIterating;

    public Handler(Queue<Element> queue, int nThreads) {
        this.nThreads = nThreads;
        this.queue = queue;
    }

    @Override
    public void run() {
        ConcurrentMap<Integer, Boolean> runningTasks = new ConcurrentHashMap<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
        Queue<Element> finished = new ConcurrentLinkedQueue<>();

        Element element;
        Iterator<Element> it;
        while (true) {
            keepIterating = true;
            for (Element elem : finished) {
                queue.remove(elem);
            }

            if (queue.isEmpty()) continue;

            it = queue.iterator();
            do {
                element = it.next();
            }
            while (keepIterating
                    && (runningTasks.containsKey(element.getGroupId()) && runningTasks.get(element.getGroupId()))
                    && it.hasNext());

            if (!keepIterating) continue;

            if (!runningTasks.containsKey(element.getGroupId()) || !runningTasks.get(element.getGroupId())) {
                runningTasks.put(element.getGroupId(), true);

                final Element finalElement = element;
                threadPool.submit(() -> {
                    doWork(finalElement.getGroupId(), finalElement.getItemId());

                    finished.add(finalElement);
                    this.stopIterating();
                    runningTasks.put(finalElement.getGroupId(), false);
                });
            }
        }
    }

    private void stopIterating() {
        keepIterating = false;
    }

    private void doWork(int groupId, int itemId) {
        System.out.format("groupId: %s, itemId: %s\n", groupId, itemId);
    }
}
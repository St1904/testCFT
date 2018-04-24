package service;

import entity.Element;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.*;

public class Handler implements Runnable {
    private final Queue<Element> queue;
    private final int nThreads;
    private volatile boolean isRunning;

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
            isRunning = true;
            for (Element elem : finished) {
                queue.remove(elem); //ConcurrentModificationException
            }

            if (queue.isEmpty()) continue;

            //выбираем из очереди первый "подходящий" элемент (сверяемся с мапой)
            it = queue.iterator();
            do {
                element = it.next(); //ConcurrentModificationException??
            }
            while (isRunning && !(runningTasks.isEmpty() || !runningTasks.containsKey(element.getGroupId()) || !runningTasks.get(element.getGroupId())) && it.hasNext());

            if (!isRunning) continue;

            //скармливаем его пулу потоков, отмечаем это в мапе
            if (runningTasks.isEmpty() || !runningTasks.containsKey(element.getGroupId()) || !runningTasks.get(element.getGroupId())) {
                runningTasks.put(element.getGroupId(), true);

                Element finalElement = element;
                threadPool.submit(() -> {
                    doWork(finalElement.getGroupId(), finalElement.getItemId());

                    //задача завершена - удаляем из очереди, отмечаем в мапе
                    finished.add(finalElement);
                    this.kill();
                    runningTasks.put(finalElement.getGroupId(), false);
                });
            }
        }
    }

    public void kill() {
        isRunning = false;
    }

    private void doWork(int groupId, int itemId) {
//        System.out.println(Thread.currentThread().getName());
/*        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.format("groupId: %s, itemId: %s\n", groupId, itemId);
    }
}

import entity.Element;
import service.Handler;
import service.SideProcess;

import java.util.ArrayDeque;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Queue<Element> queue = new ArrayDeque<>();

        new Thread(new SideProcess(queue)).start();
        new Thread(new Handler(queue, 2)).start();
    }
}

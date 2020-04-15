package cn.jesse.magicbox.view;

import java.util.LinkedList;

/**
 * 固定体积队列, 满后入队则需要先出队老数据
 *
 * @param <E>
 */
public class MagicBoxLimitQueue<E> {
    private int limit;
    private LinkedList<E> queue = new LinkedList<E>();

    public MagicBoxLimitQueue(int limit) {
        this.limit = limit;
    }

    /**
     * 设置容量限制
     *
     * @param limit 容量
     */
    public void setLimit(int limit) {
        if (limit <= 0) {
            return;
        }
        this.limit = limit;
    }

    /**
     * 入队, 如果队列到达限制则poll后再入队
     *
     * @param e data
     */
    public void offer(E e) {
        if (queue.size() >= limit) {
            queue.poll();
        }
        queue.offer(e);
    }

    /**
     * 返回原始数据
     *
     * @return queue
     */
    public LinkedList<E> getRealData() {
        return queue;
    }

    public E get(int position) {
        return queue.get(position);
    }

    public E getLast() {
        return queue.getLast();
    }

    public E getFirst() {
        return queue.getFirst();
    }

    public int getLimit() {
        return limit;
    }

    public int size() {
        return queue.size();
    }

}

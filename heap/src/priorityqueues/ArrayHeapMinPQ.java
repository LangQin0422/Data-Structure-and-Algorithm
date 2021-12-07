package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;

    private HashMap<T, Integer> dic;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        items.add(null);
        dic = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.

    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        PriorityNode<T> curr = items.get(b);
        dic.put(temp.getItem(), b);
        dic.put(curr.getItem(), a);
        items.set(a, curr);
        items.set(b, temp);
    }

    /**
     * A helper method for percolate up through the heap.
     */
    private void percolateUp(int a) {
        if (a / 2 >= START_INDEX) {
            int b = a / 2;
            if (items.get(b).getPriority() > items.get(a).getPriority()) {
                swap(a, b);
                percolateUp(b);
            }
        }
    }

    /**
     * A helper method for percolate down through the heap.
     */
    private void percolateDown(int a) {
        if (items.size() > 2 * a) {
            double currP = items.get(a).getPriority();
            double leftP = items.get(2 * a).getPriority();
            double rightP = Double.MAX_VALUE;
            if (items.size() > 2 * a + 1) {
                rightP = items.get(2 * a + 1).getPriority();
            }
            if (leftP < rightP & leftP < currP) {
                swap(a, 2 * a);
                percolateDown(2 * a);
            } else {
                if (rightP < currP) {
                    swap(a, 2 * a + 1);
                    percolateDown(2 * a + 1);
                }
            }
        }
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        items.add(new PriorityNode<>(item, priority));
        dic.put(item, items.size() - 1);
        percolateUp(items.size() - 1);
    }

    @Override
    public boolean contains(T item) {
        return dic.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (items.size() <= START_INDEX) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();
    }

    @Override
    public T removeMin() {
        if (items.size() <= START_INDEX) {
            throw new NoSuchElementException();
        }
        T result = items.get(START_INDEX).getItem();
        PriorityNode<T> temp = items.get(items.size() - START_INDEX);
        dic.remove(result);
        items.set(START_INDEX, temp);
        items.remove(items.size() - 1);
        dic.put(temp.getItem(), START_INDEX);
        percolateDown(START_INDEX);
        return result;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int index = dic.get(item);
        double temp = items.get(index).getPriority();
        items.get(index).setPriority(priority);
        if (temp > priority) {
            percolateUp(index);
        } else {
            percolateDown(index);
        }
    }

    @Override
    public int size() {
        return items.size() - START_INDEX;
    }
}

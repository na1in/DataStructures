package datastructures.lists;

import datastructures.EmptyContainerException;
//import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Note: For more info on the expected behavior of your methods:
 * @see IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    /*
    Warning:
    You may not rename these fields or change their types.
    We will be inspecting these in our secret tests.
    You also may not add any additional fields.

    Note: The fields below intentionally omit the "private" keyword. By leaving off a specific
    access modifier like "public" or "private" they become package-private, which means anything in
    the same package can access them. Since our tests are in the same package, they will be able
    to test these properties directly.
     */
    Node<T> front;
    Node<T> back;
    int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        Node<T> newElement = new Node<>(item);

        if (this.front == null) {
            this.front = newElement;
            this.front.prev = null;
            this.back = newElement;
            this.back.next = null;
        } else {
            newElement.prev = this.back;
            this.back.next = newElement;
            this.back = newElement;
            this.back.next = null;
        }
        this.size++;
    }

    @Override
    public T remove() {
        if (this.front == null) {
            throw new EmptyContainerException();
        }
        T data;
        if (this.size == 1) {
            data = this.front.data;
            this.front = null;
            this.back = null;
        } else {
            data = this.back.data;
            Node<T> curr = this.back;
            this.back = curr.prev;
            curr.prev = null;
            this.back.next = null;
        }
        this.size--;
        return data;
    }

    @Override
    public T get(int index) {
        helperException(index, 0, this.size);

        Node<T> curr;

        if (index > this.size / 2) {
            curr = backCursor(this.size - index - 1);
        } else {
            curr = nodeCursor(index);
        }

        return curr.data;

    }

    private void helperException(int index, int min,  int max) {
        if (index < min || index >= max) {
            throw new IndexOutOfBoundsException();
        }
    }

    private Node<T> nodeCursor(int index) {
        Node<T> curr = this.front;

        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr;
    }

    @Override
    public T set(int index, T item) {
        helperException(index, 0, this.size);

        Node<T> newElement = new Node<>(item);

        T dataReturn;

        if (index == 0) {
            dataReturn = this.front.data;
            newElement.next = this.front.next;
            if (this.front.next != null) {
                newElement.next.prev = newElement;
            }
            this.front.next = null;
            this.front = newElement;
        } else if (index == this.size - 1) {
            dataReturn = this.back.data;
            newElement.prev = this.back.prev;
            this.back.prev.next = newElement;
            this.back.prev = null;
            this.back = newElement;
        } else {
            Node<T> curr = nodeCursor(index);
            dataReturn = curr.data;
            newElement.next = curr.next;
            newElement.prev = curr.prev;
            curr.prev.next = newElement;
            curr.next.prev = newElement;
            curr.prev = null;
            curr.next = null;

        }
        return dataReturn;
    }

    @Override
    public void insert(int index, T item) {
        helperException(index, 0, this.size + 1);

        if (this.size == 0 || index == this.size) {
            add(item);
        } else if (index == 0) {
            Node<T> newElement = new Node<>(item);

            newElement.next = this.front;
            this.front.prev = newElement;
            this.front = newElement;
            this.size++;
        } else {
            Node<T> newElement = new Node<>(item);

            Node<T> nextIndex;

            if (index > this.size / 2) {
                nextIndex = backCursor(this.size - index - 1);
            } else {
                nextIndex = nodeCursor(index);
            }
            Node<T> prevIndex = nextIndex.prev;

            newElement.next = nextIndex;
            newElement.prev = prevIndex;
            nextIndex.prev = newElement;
            prevIndex.next = newElement;
            this.size++;
        }
    }

    private Node<T> backCursor(int index) {
        Node<T> curr = this.back;

        for (int i = 0; i < index; i++) {
            curr = curr.prev;
        }

        return curr;
    }

    @Override
    public T delete(int index) {
        helperException(index, 0, this.size);

        T dataReturn;

        if (index == this.size - 1) {
            dataReturn =  remove();
        } else if (index == 0){
            dataReturn = this.front.data;
            this.front = this.front.next;
            this.front.prev.next = null;
            this.front.prev = null;
            this.size--;
        } else {
            Node<T> curr;

            if (index > this.size / 2) {
                curr = backCursor(this.size - index - 1);
            } else {
                curr = nodeCursor(index);
            }

            curr.prev.next = curr.next;
            curr.next.prev = curr.prev;
            curr.prev = null;
            curr.next = null;
            dataReturn =  curr.data;
            this.size--;
        }
        return dataReturn;
    }

    @Override
    public int indexOf(T item) {
        Node<T> curr = this.front;
        for (int i = 0; i < this.size; i++) {
            if (Objects.equals(curr.data, item)) {
                return i;
            } else {
                curr = curr.next;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        Node<T> curr = this.front;
        for (int i = 0; i < this.size; i++) {
            if (Objects.equals(curr.data, other)) {
                return true;
            } else {
                curr = curr.next;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString();

        /*
        After you've implemented the iterator, comment out the line above and uncomment the line
        below to get a better string representation for objects in assertion errors and in the
        debugger.
        */

        // return IList.toString(this);
    }

    @Override
    public Iterator<T> iterator() {
        /*
        Note: we have provided a part of the implementation of an iterator for you. You should
        complete the methods stubs in the DoubleLinkedListIterator inner class at the bottom of
        this file. You do not need to change this method.
        */
        return new DoubleLinkedListIterator<>(this.front);
    }

    static class Node<E> {
        // You may not change the fields in this class or add any new fields.
        final E data;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> next;

        public DoubleLinkedListIterator(Node<T> front) {
            // You do not need to make any changes to this constructor.
            this.next = front;
        }

        /**
         * Returns `true` if the iterator still has elements to look at;
         * returns `false` otherwise.
         */
        public boolean hasNext() {
            return this.next != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            T dataReturn;
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                dataReturn = this.next.data;
                this.next = this.next.next;
            }
            return dataReturn;
        }
    }
}

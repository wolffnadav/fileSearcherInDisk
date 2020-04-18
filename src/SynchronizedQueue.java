// Nadav Wolff 204098610

/**
 * A synchronized bounded-size queue for multithreaded producer-consumer applications.
 *
 * @param <T> Type of data items
 */
public class SynchronizedQueue<T> {

    private T[] buffer;
    private int producers;

    private int capacity;
    private int dequeueIndex;
    private int enqueueIndex;
    private int size;


    /**
     * Constructor. Allocates a buffer (an array) with the given capacity and
     * resets pointers and counters.
     *
     * @param capacity Buffer capacity
     */
    @SuppressWarnings("unchecked")
    public SynchronizedQueue(int capacity) {
        this.buffer = (T[]) (new Object[capacity]);
        this.producers = 0;
        this.capacity = this.buffer.length;
        this.size = 0;
        this.dequeueIndex = 0;
        this.enqueueIndex = 0;
    }

    /**
     * Dequeues the first item from the queue and returns it.
     * If the queue is empty but producers are still registered to this queue,
     * this method blocks until some item is available.
     * If the queue is empty and no more items are planned to be added to this
     * queue (because no producers are registered), this method returns null.
     *
     * @return The first item, or null if there are no more items
     * @see #registerProducer()
     * @see #unregisterProducer()
     */
    public T dequeue() {
        T element = null;
        synchronized (this) {
            while (this.size == 0) {
                if (this.producers == 0) return null;
                try {
                    this.wait();
                } catch (InterruptedException e) {

                }
            }

            element = this.buffer[this.dequeueIndex];
            this.dequeueIndex = (this.dequeueIndex + 1) % this.capacity;
            this.size--;
            this.notifyAll();
        }

        return element;
    }

    /**
     * Enqueues an item to the end of this queue. If the queue is full, this
     * method blocks until some space becomes available.
     *
     * @param item Item to enqueue
     */
    public void enqueue(T item) {
        synchronized (this) {
            while (this.capacity == this.size) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                }
            }

            this.buffer[this.enqueueIndex] = item;
            this.enqueueIndex = (this.enqueueIndex + 1) % this.capacity;
            this.size++;
            this.notifyAll();
        }

    }

    /**
     * Returns the capacity of this queue
     *
     * @return queue capacity
     */
    public int getCapacity() {
        return this.capacity;

    }

    /**
     * Returns the current size of the queue (number of elements in it)
     *
     * @return queue size
     */
    public int getSize() {
        return this.size;

    }

    /**
     * Registers a producer to this queue. This method actually increases the
     * internal producers counter of this queue by 1. This counter is used to
     * determine whether the queue is still active and to avoid blocking of
     * consumer threads that try to dequeue elements from an empty queue, when
     * no producer is expected to add any more items.
     * Every producer of this queue must call this method before starting to
     * enqueue items, and must also call <see>{@link #unregisterProducer()}</see> when
     * finishes to enqueue all items.
     *
     * @see #dequeue()
     * @see #unregisterProducer()
     */
    public synchronized void registerProducer() {
        synchronized (this) {
            this.producers++;
        }

    }

    /**
     * Unregisters a producer from this queue. See <see>{@link #registerProducer()}</see>.
     *
     * @see #dequeue()
     * @see #registerProducer()
     */
    public synchronized void unregisterProducer() {
        synchronized (this) {
            this.producers--;
            if (this.producers == 0) {
                this.notifyAll();
            }
        }

    }
}

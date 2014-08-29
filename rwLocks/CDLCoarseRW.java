/* Name :Nisha Chaudhari
 * Email:nishakes@buffalo.edu 
 */
package rwLocks;

import java.util.concurrent.atomic.AtomicInteger;

//class to implement coarse grained locking for RW locks
public class CDLCoarseRW<T> extends CDLList<T> {
	volatile int wait = 0;
	private final AtomicInteger flag = new AtomicInteger();

	public CDLCoarseRW(T v) {
		super(v);
		// TODO Auto-generated constructor stub
	}

	// method to get read lock
	// If there is any writer waiting then reader should wait for all the
	// readers to get over
	// If there is any writer executing then reader should wait for the writer
	// to get over
	// Else if there is no writer waiting or executing then reader can get the
	// lock
	public void lockRead() {
		if (wait < 0) {
			while (flag.compareAndSet(0, 1)) {
			}
		} else {
			flag.incrementAndGet();
		}
	}

	// If other readers are accessing the flag then decrement it
	// else just set the flag to 0 from 1;
	public void unlockRead() {
		if (!flag.compareAndSet(1, 0)) {
			flag.decrementAndGet();
		}
	}

	// As soon as writer comes change the wait flag to make readers understand
	// some writer is waiting
	// Get the lock using compareAndSet method
	public void lockWrite() {
		wait--;
		while (flag.compareAndSet(0, 1)) {
		}

	}

	// unlock the write lock
	// set the lock to 0 from 1 and also increase the write wait flag to notify
	// that writer is done
	public void unlockWrite() {
		flag.set(0);
		wait++;
	}

	@Override
	public Cursor reader(Element from) {
		Cursor c = new Cursor();
		c.current = from;
		return c;
	}

	class Cursor extends CDLList<T>.Cursor {
		@Override
		// method to move the cursor to previous position
		public void previous() {
			try {
				lockRead();
				current = current.prev;
				unlockRead();
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
			}
		}

		@Override
		// Method to move the cursor to next
		public void next() {
			try {
				lockRead();
				current = current.next;
				unlockRead();
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
			}
		}

		@Override
		public Writer writer() {
			Writer w = new Writer();
			w.current = current;
			return w;
		}
	}

	class Writer extends CDLList<T>.Writer {
		@Override
		// Method to insert element before current element by taking the write
		// lock on list
		public boolean insertBefore(T val) {
			try {
				lockWrite();
				Element e = new Element();
				Element t1;
				Element curr;
				e.data = val;
				curr = current;
				t1 = curr.prev;
				curr.prev = e;
				e.next = curr;
				e.prev = t1;
				t1.next = e;
				unlockWrite();
				return true;
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
				return false;
			}
		}

		@Override
		// Method to insert element after current element by taking the write
		// lock on list
		public boolean insertAfter(T val) {
			try {
				lockWrite();
				Element e = new Element();
				Element t1;
				Element curr;
				e.data = val;
				curr = current;
				t1 = curr.next;
				curr.next = e;
				e.prev = curr;
				e.next = t1;
				t1.prev = e;
				unlockWrite();
				return true;
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
				return false;
			}
		}

		@Override
		// Method to delete element by taking the write lock on list
		public boolean delete() {
			try {
				lockWrite();
				Element e1, e2, curr;
				int headFlag = 0;
				curr = current;
				if (current == head) {
					headFlag = 1;
				}
				e1 = current.prev;
				e2 = current.next;
				e1.next = e2;
				e2.prev = e1;
				current = current.next;
				if (headFlag == 1) {
					head = current;
				}
				curr.prev = curr.next = null;
				unlockWrite();
				return true;
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
				return false;
			}
		}
	}

	// method to display the list
	public void displayList() {
		Element e = head();
		do {
			System.out.println(e.value());
			e = e.next;
		} while (e != head());
	}
}
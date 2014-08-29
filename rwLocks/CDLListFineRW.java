/* Name :Nisha Chaudhari
 * Email:nishakes@buffalo.edu 
 */
package rwLocks;

//Class to implement Fine grained RW locks
public class CDLListFineRW<T> extends CDLList<T> {
	Element pseudo;

	public CDLListFineRW(T v) {
		super(v);
		// attach pseudo node before head
		pseudo = new Element();
		pseudo.next = pseudo.prev = head;
		head.next = head.prev = pseudo;
		pseudo.data = (T) "pseudo";
	}

	// Method for implementing read lock
	// If no writer is waiting then increment the flag
	// else compare and set the flag
	public void lockRead(Element e) {
		if (e.wait < 0) {
			while (e.flag.compareAndSet(0, 1)) {
			}
		} else {
			e.flag.incrementAndGet();
		}
	}

	// If other readers are accessing the flag then decrement it
	// else just the flag to 0 from 1;
	public void unlockRead(Element e) {
		if (!e.flag.compareAndSet(1, 0)) {
			e.flag.decrementAndGet();
		}
	}

	// As soon as writer comes change the wait flag to make readers understand
	// some writer is waiting
	// Get the lock using compareAndSet method
	public void lockWrite(Element e) {
		e.wait--;
		while (e.flag.compareAndSet(0, 1)) {
		}
	}

	// unlock the write lock
	// set the lock to 0 from 1 and also increase the write wait flag to notify
	// that writer is done

	public void unlockWrite(Element e) {
		e.flag.set(0);
		e.wait++;
	}

	@Override
	public Cursor reader(Element from) {
		Cursor c = new Cursor();
		c.current = from;
		return c;
	}

	public class Cursor extends CDLList<T>.Cursor {
		@Override
		// method to move the cursor to previous position
		// If cursor is at head then move it to previous element of pseudo
		public void previous() {
			try {
				Element temp = current();
				lockRead(temp);
				{
					if (current == head()) {
						current = current.prev.prev;
					} else {
						current = current.prev;
					}
					unlockRead(temp);
				}
			} catch (Exception e) {
				System.out.println("Invalid Element");
			}
		}

		@Override
		// Method to move the cursor to next
		// If the cursor is at last element then move it to next element of
		// pseudo
		public void next() {
			try {
				Element temp = current();
				lockRead(temp);
				{
					current = current.next;
					if (current.data == "pseudo") {
						current = current.next;
					}
					unlockRead(temp);
				}
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
		// Method for inserting element after current element using the locks on
		// current and next element
		public boolean insertAfter(T val) {
			try {
				Element e2;
				Element e1 = current;

				lockWrite(e1);
				{
					e2 = e1.next;
					lockWrite(e2);
					{
						Element e = new Element();
						Element t1;
						Element curr;
						e.data = val;
						curr = e1;
						t1 = curr.next;
						curr.next = e;
						e.prev = curr;
						e.next = t1;
						t1.prev = e;
						unlockWrite(e2);
					}
					unlockWrite(e1);
					return true;
				}
			} catch (Exception e) {
				System.out.println("Invalid Element");
				return false;
			}

		}

		@Override
		// Method to insert an element before current element
		// by locking previous and current element
		// If current element is head then lock last element of list and pseudo
		// element
		public boolean insertBefore(T val) {
			try {
				Element e2;
				Element e1;

				if (current != head()) {
					e1 = current.prev;
				} else {
					e1 = head.prev.prev;
				}
				lockWrite(e1);
				{
					e2 = e1.next;
					lockWrite(e2);
					{
						Element e = new Element();
						Element t1;
						Element curr;
						e.data = val;
						curr = e1;
						curr.next = e;
						e.prev = curr;
						e.next = e2;
						e2.prev = e;
						unlockWrite(e2);
					}
					unlockWrite(e1);
					return true;
				}
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
				return false;
			}
		}

		@Override
		// Delete current element by locking previous,current and next element
		// After deletion move the cursor to next element in the list
		public boolean delete() {
			try {
				lockWrite(current.prev);
				{
					lockWrite(current);
					{
						lockWrite(current.next);
						{
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

						}
					}
				}
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
			if (e.value() != "pseudo") {
				System.out.println(e.value());
			}
			e = e.next;
		} while (e != head());
	}
}
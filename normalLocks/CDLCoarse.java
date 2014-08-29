/* Name :Nisha Chaudhari
 * Email:nishakes@buffalo.edu 
 */

package normalLocks;

//Class for coarse grain locking implementation without RW locks
public class CDLCoarse<T> extends CDLList<T> {

	public CDLCoarse(T v) {
		super(v);
		// TODO Auto-generated constructor stub
	}

	@Override
	// Creating a new cursor of class CDLCoarse.Cursor
	public Cursor reader(Element from) {
		Cursor c = new Cursor();
		c.current = from;
		return c;
	}

	class Cursor extends CDLList<T>.Cursor {
		@Override
		// Create a new writer of CDLCoarse.Writer class
		public synchronized Writer writer() {
			Writer w = new Writer();
			w.current = current;
			return w;
		}

		@Override
		// Returns the current element
		public synchronized Element current() {
			return current;
		}

		@Override
		// moves the cursor to previous element
		public synchronized void previous() {
			try {
				current = current.prev;
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
			}
		}

		@Override
		// moves the cursor to next element
		public synchronized void next() {
			try {
				current = current.prev;
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
			}
		}
	}

	class Writer extends CDLList<T>.Writer {
		@Override
		// Synchronized insertBefore method
		public synchronized boolean insertBefore(T val) {
			try {
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
				return true;
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
				return false;
			}
		}

		@Override
		// Synchronised insertAfter method
		public synchronized boolean insertAfter(T val) {
			try {
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
				return true;
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
				return false;
			}
		}

		@Override
		// synchronized delete method
		public synchronized boolean delete() {
			try {
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
				return true;
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
				return false;
			}
		}
	}

	// display entire list
	public void displayList() {
		Element e = head();
		do {
			System.out.println(e.value());
			e = e.next;
		} while (e != head());
	}
}
/* Name :Nisha Chaudhari
 * Email:nishakes@buffalo.edu 
 */
package normalLocks;

//Class for implementation of Circular Doubly Linked List
public class CDLList<T> {
	protected Element head = null;

	// Creating head element in constructor
	public CDLList(T v) {
		Element e = new Element();
		e.data = v;
		e.next = e;
		e.prev = e;
		if (head == null) {
			head = e;
		}
	}

	// Basic element of the list
	public class Element {
		public T data;
		public Element prev;
		public Element next;
		public int number;

		public T value() {
			return data;
		}
	}

	// returns the head of the list
	public Element head() {
		return head;
	}

	// Creates the cursor object
	public Cursor reader(Element from) {
		Cursor c = new Cursor();
		c.current = from;
		return c;
	}

	// Cursor class for moving in the list
	public class Cursor {
		Element current;

		// returns current element
		public Element current() {
			return current;
		}

		// returns previous element
		public void previous() {
			current = current.prev;
		}

		// returns next element
		public void next() {
			current = current.prev;
		}

		// creates writer object for current element
		public Writer writer() {
			Writer w = new Writer();
			w.current = current;
			return w;
		}
	}

	public class Writer {
		Element current;

		// method to insert an element before current element
		protected boolean insertBefore(T val) {
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
		}

		// method to insert an element after current element
		protected boolean insertAfter(T val) {
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
		}

		// method to delete current element
		public boolean delete() {
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
		}
	}
}
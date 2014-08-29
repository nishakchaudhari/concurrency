/* Name :Nisha Chaudhari
 * Email:nishakes@buffalo.edu 
 */
package rwLocks;

import java.util.concurrent.atomic.AtomicInteger;

public class CDLList<T> {
	protected Element head = null;

	public CDLList(T v) {
		Element e = new Element();

		e.data = v;
		e.next = e;
		e.prev = e;
		if (head == null) {
			head = e;
		}
	}

	public class Element {
		public T data;
		public Element prev;
		public Element next;
		volatile int wait = 0;
		volatile int rwFlag = 0;
		final AtomicInteger flag = new AtomicInteger();

		public T value() {
			return data;
		}
	}

	public Element head() {
		return head;
	}

	public Cursor reader(Element from) {
		Cursor c = new Cursor();
		c.current = from;
		return c;
	}

	public class Cursor {
		Element current;

		public Element current() {
			return current;
		}

		public void previous() {
			current = current.prev;
		}

		public void next() {
			current = current.prev;
		}

		public Writer writer() {
			Writer w = new Writer();
			w.current = current;
			return w;
		}
	}

	public class Writer {
		Element current;

		protected void writeValue(T v) {
			current.data = v;
		}

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
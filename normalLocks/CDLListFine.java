/* Name :Nisha Chaudhari
 * Email:nishakes@buffalo.edu 
 */
package normalLocks;

//class to implement Fine grain locking without RW locks
public class CDLListFine<T> extends CDLList<T> {
	public CDLListFine(T v) {
		super(v);
		// After creating head element , create and attach pseudo element before
		// head
		pseudo = new Element();
		pseudo.data = (T) "pseudo";
		head.prev = head.next = pseudo;
		pseudo.next = pseudo.prev = head;
		// TODO Auto-generated constructor stub
	}

	Element pseudo;
	String s;

	@Override
	// Create a new cursor object of CDLListFine
	public Cursor reader(Element e) {
		Cursor c = new Cursor();
		c.current = e;
		return c;
	}

	// class cursor which is used to move around the element
	class Cursor extends CDLList<T>.Cursor {
		@Override
		public void previous() {
			// If element is head then move to previous element of pseudo
			// element
			try {
				if (current == head()) {
					current = current.prev.prev;
				} else {
					current = current.prev;
				}
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
			}
		}

		@Override
		public void next() {
			try {
				current = current.next;
				// If current element is last node in list then move to next of
				// pseudo
				if (current.data == "pseudo") {
					current = current.next;
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

	// class writer in order to override insertBefore and insertAfter , delete
	// methods
	class Writer extends CDLList<T>.Writer {
		@Override
		// method to insert before current element
		// using the synchronization on previous and current element
		public boolean insertBefore(T val) {
			try {
				if (current != head()) {
					synchronized (current.prev) {
						synchronized (current) {
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
						}
					}
				} else {
					headInsertBefore(val);
				}
				return true;
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
				return false;
			}
		}

		@Override
		// method to insert after an element using synchronization on current
		// and next element
		public boolean insertAfter(T val) {
			try {
				synchronized (current) {
					synchronized (current.next) {
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
				}
			} catch (NullPointerException e) {
				System.out.println("Invalid Element");
				return false;
			}
		}

		// method to insert element befor{e head
		public void headInsertBefore(T val) {

			if (current == head()) {
				synchronized (current.prev.prev) {
					synchronized (current.prev) {
						current = current.prev;
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
						current = current.next;
					}
				}
			}
		}

		@Override
		// method to delete current element and cursor moves to next element
		// after deletion
		public boolean delete() {
			try {
				synchronized (current.prev) {
					synchronized (current) {
						synchronized (current.next) {
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

	// Function to display list
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

package ClientServer.SessionManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.AbstractListModel;


//FOr the session manager list

public class SessionList extends AbstractListModel<Object> {
    /**
     *
     */
    ArrayList<User> uList = new ArrayList<User>();

    public void refresh() {
        fireContentsChanged(this, 0, getSize());
    }

    public SessionList(ArrayList<User> users) {
        this.uList = users;
        fireContentsChanged(this, 0, getSize());
    }

    public int getSize() {
        return uList.size();

    }

    public User getElementAt(int index) {
        return uList.get(index);
    }

    // public void SortedListModel() {
    // int i=users.size();
    // String[] temp=new String
    // Arrays.sort(users,cmp);
    // }

    public void add(User element) {
        if (uList.add(element)) {
            fireContentsChanged(this, 0, getSize());
        }
    }

    public void addAll(User elements[]) {
        Collection<User> c = Arrays.asList(elements);
        uList.addAll(c);
        fireContentsChanged(this, 0, getSize());
    }

    public void clear() {
        uList.clear();
        fireContentsChanged(this, 0, getSize());
    }

    public boolean contains(User element) {
        return uList.contains(element);
    }

    public User firstElement() {
        return uList.get(0);
    }

    public Iterator<User> iterator() {
        return uList.iterator();
    }

    public User lastElement() {
        return uList.get(uList.size());
    }

    public boolean removeElement(User element) throws IOException {
        boolean removed = uList.remove(element);
        if (removed) {
            fireContentsChanged(this, 0, getSize());
        }
        return removed;
    }

    public User removeElement(int element) {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    uList.get(element).socket.getOutputStream()));
            out.write("KICK");
            out.newLine();
            out.flush();
            uList.get(element).socket.close();
        } catch (IOException e) {
        }
        User del = uList.remove(element);
        fireContentsChanged(this, 0, getSize());
        return del;
    }

}

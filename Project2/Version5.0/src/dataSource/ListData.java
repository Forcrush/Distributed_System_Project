package dataSource;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.AbstractListModel;

public class ListData extends AbstractListModel {
    private ArrayList<User> users;

    public void refresh() {
        fireContentsChanged(this, 0, getSize());
    }

    public ListData(ArrayList<User> users) {
        this.users = users;
        fireContentsChanged(this, 0, getSize());
    }

    public int getSize() {
        return users.size();

    }

    public User getElementAt(int index) {
        return users.get(index);
    }

    public void add(User element) {
        if (users.add(element)) {
            fireContentsChanged(this, 0, getSize());
        }
    }

    public void addAll(User elements[]) {
        Collection<User> c = Arrays.asList(elements);
        users.addAll(c);
        fireContentsChanged(this, 0, getSize());
    }

    public void clear() {
        users.clear();
        fireContentsChanged(this, 0, getSize());
    }

    public boolean contains(User element) {
        return users.contains(element);
    }

    public User firstElement() {
        return users.get(0);
    }

    public Iterator iterator() {
        return users.iterator();
    }

    public User lastElement() {
        return users.get(users.size());
    }

    public boolean removeElement(User element) throws IOException {
        boolean removed = users.remove(element);
        if (removed) {
            fireContentsChanged(this, 0, getSize());
        }
        return removed;
    }

    public User removeElement(int element) {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(users.get(element).ip.getOutputStream()));
            out.write("KICK");
            out.newLine();
            out.flush();
            users.get(element).ip.close();
        } catch (IOException e) {
            System.out.println("IOException");
        }
        User del = users.remove(element);
        fireContentsChanged(this, 0, getSize());
        return del;
    }

}

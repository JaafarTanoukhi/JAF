package Compiler.Util;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.Collection;

public class Venn<T> {
    private Set<T> elements;

    public Venn() {
        this.elements = Collections.newSetFromMap(new IdentityHashMap<>());
    }

    public void add(T... newElements) {
        Set<T> newSet = Collections.newSetFromMap(new IdentityHashMap<>());
        Collections.addAll(newSet, newElements);
        updateElements(newSet);
    }

    public void add(Collection<? extends T> newElements) {
        Set<T> newSet = Collections.newSetFromMap(new IdentityHashMap<>());
        newSet.addAll(newElements);
        updateElements(newSet);
    }

    private void updateElements(Set<T> newSet) {
        if (elements.isEmpty()) {
            elements.addAll(newSet);
        } else {
            elements.retainAll(newSet);
        }
    }

    public Set<T> getElements() {
        return Collections.unmodifiableSet(elements);
    }
    
        public void clear() {
            elements.clear();
        }

    public boolean isEmpty(){
        return elements.size() == 0;
    }
    
    @Override
    public String toString() {
        return elements.toString();
    }

}

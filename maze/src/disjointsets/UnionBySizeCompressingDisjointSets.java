package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;

    private Map<T, Integer> dic;

    public UnionBySizeCompressingDisjointSets() {
        dic = new HashMap<>();
        pointers = new ArrayList<>();
    }

    @Override
    public void makeSet(T item) {
        if (dic.containsKey(item)) {
            throw new IllegalArgumentException(item + " is already contained in one of these sets");
        }
        dic.put(item, pointers.size());
        pointers.add(-1);
    }

    @Override
    public int findSet(T item) {
        if (!dic.containsKey(item)) {
            throw new IllegalArgumentException(item + " is not in any set.");
        }

        int index = dic.get(item);
        Set<Integer> compressItems = new HashSet<>();
        while (pointers.get(index) >= 0) {
            compressItems.add(index);
            index = pointers.get(index);
        }

        for (Integer compressItem : compressItems) {
            pointers.set(compressItem, index);
        }
        return index;
    }

    @Override
    public boolean union(T item1, T item2) {
        int root1 = findSet(item1);
        int root2 = findSet(item2);

        if (root1 == root2) {
            return false;
        }

        if ((-1 * pointers.get(root1)) > (-1 * pointers.get(root2))) {
            pointers.set(root1, pointers.get(root1) + pointers.get(root2));
            pointers.set(root2, root1);
        } else {
            pointers.set(root2, pointers.get(root1) + pointers.get(root2));
            pointers.set(root1, root2);
        }
        return true;
    }
}

package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.*;

import java.util.*;

// Attention: comparable supported but comparator is not
@SuppressWarnings("WeakerAccess")
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private Node<T> root = null;
    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     * <p>
     * Трудоёмкость: в среднем = О(log n), в худшем случае = О(n).
     * Ресурсоёмкость = О(1).
     */
    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        T value = (T) o;
        int size = size();
        if (root.value.equals(value)) {
            removing(root, false, true);
        }
        searchAndRemove(root, value);
        return size != size();
    }

    private void searchAndRemove(Node<T> current, T value) {          // Поиск занимает О(log n) операций
        if (current != null) {
            if (value.compareTo(current.value) < 0) {
                if (current.left != null) {
                    if (current.left.value.equals(value)) {
                        removing(current, true, false);
                    } else {
                        searchAndRemove(current.left, value);
                    }
                }
            } else if (current.right != null) {
                if (current.right.value.equals(value)) {
                    removing(current, false, false);
                } else {
                    searchAndRemove(current.right, value);
                }
            }
        }
    }

    private void removing(Node<T> current, boolean forLeft, boolean forRoot) {
        Node<T> node;
        if (forRoot) {
            node = root;
        } else if (forLeft) {
            node = current.left;
        } else {
            node = current.right;
        }

        if (node.left == null && node.right == null) {
            node = null;
        } else if (node.left == null) {
            node = node.right;
        } else if (node.right == null) {
            node = node.left;
        } else {
            Node<T> rightSideOfRemovingNode = node.right;
            Node<T> leftSideOfRemovingNode = node.left;
            if (node.right.left == null) {
                node = node.right;
                node.left = leftSideOfRemovingNode;
            } else if (node.left.right == null) {
                node = node.left;
                node.right = rightSideOfRemovingNode;
            } else {
                Node[] newNode = new Node[1];
                searchMinElementFromRightChildren(rightSideOfRemovingNode, newNode);
                node = newNode[0];                       // заменяем удаляемый элемент на минимальный
                node.right = rightSideOfRemovingNode;
                node.left = leftSideOfRemovingNode;
            }
        }
        size--;

        if (forRoot) {
            root = node;
        } else if (forLeft) {
            current.left = node;
        } else {
            current.right = node;
        }
    }

    /**
     * Ищем минимальный элемент справа от удаляемого => Т = О(m), где
     * m - высота поддерева от правого потомка удаляемого элемента до его минимального потомка.
     */
    private void searchMinElementFromRightChildren(Node<T> current, Node[] newNode) {
        if (current.left.left == null) {
            newNode[0] = current.left;
            current.left = current.left.right;
        } else {
            searchMinElementFromRightChildren(current.left, newNode);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     * <p>
     * Трудоёмкость = О(n);
     * Ресурсоёмкость = O(n).
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        SortedSet<T> result = new TreeSet<>();
        searchForHeadSet(root, toElement, result);
        return result;
    }

    private void searchForHeadSet(Node<T> current, T value, Set<T> result) {
        int comparison = value.compareTo(current.value);
        if (comparison < 0) {
            if (current.left != null) {
                searchForHeadSet(current.left, value, result);
            }
        } else {
            if (current.left != null) {
                addElements(current.left, result);
            }
            if (comparison > 0) {
                result.add(current.value);
                if (current.right != null) {
                    searchForHeadSet(current.right, value, result);
                }
            }
        }
    }

    private void addElements(Node<T> current, Set<T> result) {
        result.add(current.value);
        if (current.left != null) {
            addElements(current.left, result);
        }
        if (current.right != null) {
            addElements(current.right, result);
        }
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     * <p>
     * Трудоёмкость = О(n);
     * Ресурсоёмкость = O(n).
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        SortedSet<T> result = new TreeSet<>();
        searchForTailSet(root, fromElement, result);
        return result;
    }

    private void searchForTailSet(Node<T> current, T value, Set<T> result) {
        int comparison = value.compareTo(current.value);
        if (comparison > 0) {
            if (current.right != null) {
                searchForTailSet(current.right, value, result);
            }
        } else {
            result.add(current.value);
            if (current.right != null) {
                addElements(current.right, result);
            }
            if (comparison < 0) {
                if (current.left != null) {
                    searchForTailSet(current.left, value, result);
                }
            }
        }
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        private Node<T> current;
        private Node<T> parent;
        private LinkedList<Node<T>> rememberParents; // вершины, правые потомки которых имеют левые потомки
        private LinkedList<Node<T>> elements;

        private BinaryTreeIterator() {
            current = null;
            rememberParents = new LinkedList<>();
            elements = new LinkedList<>();
            Node<T> node = root;
            while (node != null) {
                elements.add(0, node);
                node = node.left;
            }
        }

        /**
         * Поиск следующего элемента
         * Средняя
         * <p>
         * В лучшем случае трудоёмкость = О(1), в среднем = О(m).
         */
        private Node<T> findNext(boolean fromNext) {
            if (current == null) {
                if (fromNext) {
                    if (elements.size() == 1) {
                        parent = null;
                    } else {
                        parent = elements.get(1);
                    }
                    return elements.pollFirst();
                }
                if (elements.size() == 0) return null;
                else return elements.getFirst();
            }

            Node<T> result = current;

            if (result.right == null && elements.size() != 0) {  // если нет правого потомка, то переходим
                result = elements.getFirst();                    // к родителю => T = O(1)

                if (fromNext) {
                    elements.remove(0);
                    if (rememberParents.size() != 0 && (rememberParents.getFirst().right == result ||
                            rememberParents.getFirst().left == result)) {
                        parent = rememberParents.pollFirst();
                    } else if (elements.size() != 0) {
                        parent = elements.getFirst();
                    } else {
                        parent = null;
                    }
                }
            } else {
                result = result.right;
                parent = current;
                if (result != null) {
                    if (result.left != null && fromNext) {
                        rememberParents.add(0, current);
                    }
                    while (result.left != null) {                 // ecли у правого потомка нет левых потомков,
                        if (fromNext) {                           // то T = O(1), а иначе - находим самого
                            elements.add(0, result);        // маленького потомка => T = O(m)
                            parent = elements.getFirst();
                        }
                        result = result.left;
                    }
                }
            }
            return result;
        }

        @Override
        public boolean hasNext() {
            return findNext(false) != null;
        }

        @Override
        public T next() {
            current = findNext(true);
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         * <p>
         * Т = О(m), где m - высота поддерева от правого потомка удаляемого элемента
         * до его минимального потомка.
         */
        @Override
        public void remove() {
            if (parent == null) {
                removing(current, false, true);
            } else {
                searchAndRemove(parent, current.value);
            }
        }
    }
}

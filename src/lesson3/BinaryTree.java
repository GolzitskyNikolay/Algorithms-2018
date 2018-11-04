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
     * Т.к. операция поиска требует О(log n) операций, а для перемещения элементов нужно О(m) операций =>
     * Трудоёмкость = О(log n).
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

    private void searchAndRemove(Node<T> current, T value) {    // Поиск занимает О(log n) операций
        if (value.compareTo(current.value) < 0) {
            if (current.left != null) {
                if (current.left.value.equals(value)) {
                    removing(current, true, false);
                } else {
                    searchAndRemove(current.left, value);
                }
            }
        } else {
            if (current.right != null) {
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
        if (node.left != null) {
            Node<T> rightSideOfLeftPartOfRemovingNode = node.left.right;
            node.left.right = node.right;
            node = node.left;
            size--;
            if (node.right == null) {
                node.right = rightSideOfLeftPartOfRemovingNode;
            } else {
                addRightSide(node.right, rightSideOfLeftPartOfRemovingNode);   // О(m) операций
            }
        } else {
            size--;
            node = node.right;
        }

        if (forRoot) {
            root = node;
        } else if (forLeft) {
            current.left = node;
        } else {
            current.right = node;
        }
    }

    // Для перемещения элементов нужно О(m) операций.
    private void addRightSide(Node<T> current, Node<T> rightSideOfLeftPartOfRemovingNode) {
        if (current.left == null) {
            current.left = rightSideOfLeftPartOfRemovingNode;
        } else {
            addRightSide(current.left, rightSideOfLeftPartOfRemovingNode);
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
     * Трудоёмкость = О(N);
     * Ресурсоёмкость = O(N).
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
     * Трудоёмкость = О(N);
     * Ресурсоёмкость = O(N).
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
        private Node<T> next;
        private LinkedList<Node<T>> elements;
        private boolean getFirstElement = true;

        private BinaryTreeIterator() {
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
         *
         * В лучшем случае трудоёмкость = О(1), в среднем = О(n).
         */
        private Node<T> findNext() {
            if (getFirstElement) {
                return elements.getFirst();
            }
            Node<T> result = current;
            if (result.right == null && elements.size() != 0) {
                result = elements.getFirst();
                elements.remove(0);
            } else {
                result = result.right;
                if (result != null) {
                    while (result.left != null) {
                        elements.add(0, result);
                        result = result.left;
                    }
                }
            }
            return result;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if (getFirstElement) {
                current = findNext();
                getFirstElement = false;
                elements.remove(0);
            } else {
                current = findNext();
            }
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        @Override
        public void remove() {
            // TODO
            throw new NotImplementedError();
        }
    }
}

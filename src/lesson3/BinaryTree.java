package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.*;

import java.util.*;

// Attention: comparable supported but comparator is not
@SuppressWarnings("WeakerAccess")
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {
    protected Node<T> root;
    protected Node<T> subRoot;
    protected int size = 0;
    protected int subSize = 0;
    private T fromElement;
    private T toElement;

    public int heightOfBinaryTree() {
        return forHeightOfBinaryTree(root);
    }

    private int forHeightOfBinaryTree(Node<T> node) {
        if (node == null) {
            return 0;
        } else {
            return 1 + Math.max(forHeightOfBinaryTree(node.left), forHeightOfBinaryTree(node.right));
        }
    }

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
        if (toElement != null && fromElement != null) {
            if (newNode.value.compareTo(toElement) < 0 &&
                    newNode.value.compareTo(fromElement) >= 0) {
                subSize++;
            }
        } else if (toElement != null && newNode.value.compareTo(toElement) < 0
                || fromElement != null && newNode.value.compareTo(fromElement) >= 0) {
            subSize++;
        }
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
        } else {
            searchAndRemove(root, value);
        }
        return size != size();
    }

    private void searchAndRemove(Node<T> current, T value) {        // Поиск занимает О(log n) операций
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

        size--;
        if (toElement != null && fromElement != null) {
            if (node.value.compareTo(toElement) < 0 &&
                    node.value.compareTo(fromElement) >= 0) {
                subSize--;
            }
        } else if (toElement != null && node.value.compareTo(toElement) < 0 ||
                fromElement != null && node.value.compareTo(fromElement) >= 0) {
            subSize--;
        }

        Node<T> copyOfNodeForSubRoot = new Node<>(node.value);

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
                node = searchMinElementFromRightChildren(rightSideOfRemovingNode);
                node.right = rightSideOfRemovingNode;
                node.left = leftSideOfRemovingNode;
            }
        }

        if (forRoot) {
            root = node;
        } else if (forLeft) {
            current.left = node;
        } else {
            current.right = node;
        }

        if (subRoot != null && subRoot.value == copyOfNodeForSubRoot.value) {
            subRoot = node;
        }
    }

    /**
     * Ищем минимальный элемент справа от удаляемого => Т = О(m), где
     * m - высота поддерева от правого потомка удаляемого элемента до его минимального потомка.
     */
    private Node<T> searchMinElementFromRightChildren(Node<T> current) {
        if (current.left.left == null) {
            Node<T> result = current.left;
            current.left = current.left.right;
            return result;
        } else {
            return searchMinElementFromRightChildren(current.left);
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
        if (fromElement.compareTo(toElement) > 0) throw new IllegalArgumentException();
        return searchForSubSet(root, fromElement, toElement);
    }

    private SortedSet<T> searchForSubSet(Node<T> current, T fromElement, T toElement) {
        int comparison = fromElement.compareTo(current.value);
        if (comparison > 0) {
            if (current.right != null) {
                return searchForHeadSet(current.right, fromElement);
            } else throw new IllegalArgumentException();
        } else {
            subRoot = current;
            this.fromElement = fromElement;
            this.toElement = toElement;
            return new SubTree();
        }
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     * <p>
     * Трудоёмкость = О(log n).
     * Ресурсоёмкость = О(1).
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return searchForHeadSet(root, toElement);
    }

    private SubTree searchForHeadSet(Node<T> current, T value) {
        int comparison = value.compareTo(current.value);
        if (comparison < 0) {
            if (current.left != null) {
                return searchForHeadSet(current.left, value);
            } else throw new IllegalArgumentException();
        } else {
            if (comparison > 0) {
                subRoot = current;
            } else if (current.left != null) {
                subRoot = current.left;
            } else {
                subRoot = null;
            }
            fromElement = null;
            toElement = value;
            return new SubTree();
        }
    }

    private void countSubSize(Node<T> current) {
        if (fromElement != null) {
            if (toElement != null && current.value.compareTo(toElement) == 0) {
                if (current.left != null) {
                    countSubSize(current.left);
                }
            } else if (current.value.compareTo(fromElement) == 0) {
                subSize++;
                if (current.right != null) {
                    countSubSize(current.right);
                }
            } else {
                if (current.value.compareTo(fromElement) > 0 && (toElement == null ||
                        current.value.compareTo(toElement) < 0)) {
                    subSize++;
                    if (current.left != null) {
                        countSubSize(current.left);
                    }
                    if (current.right != null) {
                        countSubSize(current.right);
                    }
                } else {
                    countSubSize(BinaryTree.this.find(current, fromElement));
                }
            }
        }

        if (toElement != null && fromElement == null) {
            if (current.value.compareTo(toElement) == 0 && current.left != null) {
                countSubSize(current.left);
            } else {
                if (fromElement == null || current.value.compareTo(fromElement) >= 0) {
                    subSize++;
                    if (current.left != null && current.left.value.compareTo(toElement) <= 0) {
                        countSubSize(current.left);
                    }
                    if (current.right != null && current.right.value.compareTo(toElement) <= 0) {
                        countSubSize(current.right);
                    }
                } else {
                    Node<T> resultOfFind = BinaryTree.this.find(current, fromElement);
                    if (resultOfFind != current) {
                        countSubSize(resultOfFind);
                    }
                }
            }
        }
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     * <p>
     * Трудоёмкость = О(log n).
     * Ресурсоёмкость = О(1).
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return searchForTailSet(root, fromElement);
    }

    private SubTree searchForTailSet(Node<T> current, T value) {
        int comparison = value.compareTo(current.value);
        if (comparison > 0) {
            if (current.right != null) {
                return searchForHeadSet(current.right, value);
            } else throw new IllegalArgumentException();
        } else {
            subRoot = current;
            fromElement = value;
            toElement = null;
            return new SubTree();
        }
    }

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    class SubTree extends BinaryTree<T> {

        SubTree() {
            BinaryTree.this.subSize = 0;
            countSubSize(BinaryTree.this.subRoot);
        }

        @Override
        public boolean add(T t) {
            Node<T> closest = find(t);
            int comparison = closest == null ? -1 : t.compareTo(closest.value);
            if (comparison == 0) {
                return false;
            }
            Node<T> newNode = new Node<>(t);
            if (closest == null) {
                BinaryTree.this.subRoot = newNode;
            } else if (comparison < 0) {
                assert closest.left == null;
                closest.left = newNode;
            } else {
                assert closest.right == null;
                closest.right = newNode;
            }
            BinaryTree.this.size++;
            BinaryTree.this.subSize++;
            return true;
        }

        @Override
        public boolean remove(Object o) {
            @SuppressWarnings("unchecked")
            T value = (T) o;
            int size = BinaryTree.this.subSize;
            if (BinaryTree.this.subRoot.value.equals(value)) {
                removing(BinaryTree.this.subRoot, false, true);
            } else {
                searchAndRemove(BinaryTree.this.subRoot, value);
            }
            return size != BinaryTree.this.subSize;
        }

        @Override
        public int size() {
            return BinaryTree.this.subSize;
        }

        @Override
        public boolean contains(Object o) {
            @SuppressWarnings("unchecked")
            T t = (T) o;
            Node<T> closest = find(t);
            return closest != null && t.compareTo(closest.value) == 0;
        }

        private Node<T> find(T value) {
            if (BinaryTree.this.subRoot == null) return null;
            return findForSubSet(BinaryTree.this.subRoot, value);
        }

        private Node<T> findForSubSet(Node<T> start, T value) {
            int comparison = value.compareTo(start.value);

            if (BinaryTree.this.fromElement != null) {
                if (start.value.compareTo(BinaryTree.this.fromElement) == 0 &&
                        value.compareTo(BinaryTree.this.fromElement) < 0)
                    return null;

                else if (comparison == 0) {
                    if (BinaryTree.this.toElement == null &&
                            start.value.compareTo(BinaryTree.this.fromElement) >= 0) return start;
                    else if (BinaryTree.this.toElement != null &&
                            start.value.compareTo(BinaryTree.this.fromElement) >= 0 &&
                            start.value.compareTo(BinaryTree.this.toElement) < 0) return start;
                    else return null;

                } else if (comparison < 0) {
                    if (start.left == null) return start;
                    return findForSubSet(start.left, value);

                } else {
                    if (start.right == null) return start;
                    return findForSubSet(start.right, value);
                }
            }

            if (BinaryTree.this.toElement != null) {
                if (comparison == 0) {
                    if (start.value.compareTo(BinaryTree.this.toElement) < 0) return start;
                    return null;

                } else if (comparison < 0) {
                    if (start.left == null) return start;
                    return findForSubSet(start.left, value);

                } else {
                    if (start.right == null) return start;
                    return findForSubSet(start.right, value);
                }
            }
            return null;
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

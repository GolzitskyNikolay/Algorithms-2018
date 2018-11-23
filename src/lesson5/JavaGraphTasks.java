package lesson5;

import kotlin.NotImplementedError;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {

    /**
     * Эйлеров цикл.
     * Средняя
     * <p>
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     * <p>
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     * <p>
     * Дан граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Ответ:
     * <p>
     * G    H
     * |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    public static Graph minimumSpanningTree(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     * <p>
     * Дан граф без циклов (получатель), например
     * <p>
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     * <p>
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     * <p>
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     * <p>
     * В данном случае ответ (A, E, F, D, G, J)
     * <p>
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     * <p>
     * <p>
     * Трудоёмкость = О(V), где V - количество вершин.
     * Ресурсоёмкость = О(V).
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        Set<Graph.Vertex> oddGenerations = new HashSet<>();
        Set<Graph.Vertex> evenGenerations = new HashSet<>();
        Graph.Vertex firstVertex = graph.getVertices().iterator().next();

        evenGenerations.add(firstVertex);
        addInSet(firstVertex, oddGenerations, evenGenerations, graph);

        if (evenGenerations.size() >= oddGenerations.size()) return evenGenerations;
        else return oddGenerations;
    }

    private static void addInSet(Graph.Vertex parent, Set<Graph.Vertex> oddGenerations,
                                 Set<Graph.Vertex> evenGenerations, Graph graph) {

        graph.getNeighbors(parent).forEach(e -> {
            if (!evenGenerations.contains(e) && !oddGenerations.contains(e)) {
                if (evenGenerations.contains(parent)) {
                    oddGenerations.add(e);
                } else {
                    evenGenerations.add(e);
                }
                addInSet(e, oddGenerations, evenGenerations, graph);
            }
        });
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     * <p>
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     * <p>
     * Пример:
     * <p>
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     * <p>
     * <p>
     * Трудоёмкость = О(V!), V - количество вершин в графе.
     * Ресурсоёмкость = О(V).
     */
    public static Path longestSimplePath(Graph graph) {
        List<Graph.Vertex> vertices = new ArrayList<>(graph.getVertices());
        Set<Graph.Vertex> way = new LinkedHashSet<>();
        List<Graph.Vertex> result = new ArrayList<>();

        if (vertices.size() != 0) {
            for (int i = 0; i < vertices.size(); i++) {
                way.add(vertices.get(i));
                way.add(vertices.get(i));
                searchMaxWay(graph, vertices.get(i), result, way);
                if (result.size() == vertices.size()) break;
            }
        }

        Path path = new Path(result.get(0));
        for (int i = 1; i < result.size(); i++) {
            path = new Path(path, graph, result.get(i));
        }
        return path;
    }

    private static void searchMaxWay(Graph graph, Graph.Vertex currentVertex,
                                     List<Graph.Vertex> result, Set<Graph.Vertex> way) {

        graph.getNeighbors(currentVertex).forEach(neighbor -> {
            if (!way.contains(neighbor)) {
                way.add(neighbor);

                if (way.size() != graph.getVertices().size()) {
                    searchMaxWay(graph, neighbor, result, way);
                }
            }
        });

        if (way.size() <= graph.getVertices().size()) {
            if (way.size() > result.size()) {
                result.clear();
                result.addAll(way);
            }
            way.remove(currentVertex);
        }
    }
}
package datastructures.graphs;

import datastructures.dictionaries.ArrayDictionary;
import datastructures.dictionaries.ChainedHashDictionary;
import datastructures.dictionaries.IDictionary;
import datastructures.dictionaries.KVPair;
import datastructures.disjointsets.ArrayDisjointSets;
import datastructures.disjointsets.IDisjointSets;
import datastructures.lists.DoubleLinkedList;
import datastructures.lists.IList;
import datastructures.priorityqueues.ArrayHeapPriorityQueue;
import datastructures.priorityqueues.IPriorityQueue;
import datastructures.sets.ChainedHashSet;
import datastructures.sets.ISet;
import misc.Sorter;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * @param <V> the type of the vertices
 * @param <E> the type of the additional data contained in edges
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E> {
    /*
    Feel free to add as many fields, private helper methods, and private inner classes as you want.

    And of course, as always, you may also use any of the data structures and algorithms we've
    implemented so far.

    Note: If you plan on adding a new class, please be sure to make it a private static inner class
    contained within this file. Our testing infrastructure works by copying specific files from your
    project to ours, and if you add new files, they won't be copied and your code will not compile.
    */

    IDictionary<V, IList<Edge<V, E>>> vertexEdges;
    int numVertices;
    int numEdges;

    /**
     * Constructs a new empty graph.
     */
    public Graph() {
        vertexEdges = new ChainedHashDictionary<>();
        numVertices = 0;
        numEdges = 0;
    }

    /**
     * Adds a vertex to this graph. If the vertex is already in the graph, does nothing.
     */
    public void addVertex(V vertex) {
        if (!vertexEdges.containsKey(vertex)) {
            vertexEdges.put(vertex, new DoubleLinkedList<>());
            numVertices++;
        }
    }
    /**
     * Adds a new edge to the graph, with null data.
     *
     * Every time this method is (successfully) called, a unique edge is added to the graph; even if
     * another edge between the same vertices and with the same weight and data already exists, a
     * new edge will be created and added (where `newEdge.equals(oldEdge)` is false).
     *
     * @throws IllegalArgumentException  if `weight` is null
     * @throws IllegalArgumentException  if either vertex is not contained in the graph
     */
    public void addEdge(V vertex1, V vertex2, double weight) {
        this.addEdge(vertex1, vertex2, weight, null);
    }

    /**
     * Adds a new edge to the graph with the given data.
     *
     * Every time this method is (successfully) called, a unique edge is added to the graph; even if
     * another edge between the same vertices and with the same weight and data already exists, a
     * new edge will be created and added (where `newEdge.equals(oldEdge)` is false).
     *
     * @throws IllegalArgumentException  if `weight` is null
     * @throws IllegalArgumentException  if either vertex is not contained in the graph
     */
    public void addEdge(V vertex1, V vertex2, double weight, E data) {
        if (!vertexEdges.containsKey(vertex1) || !vertexEdges.containsKey(vertex2) || weight < 0) {
            throw new IllegalArgumentException();
        }

        Edge<V, E> edge1to2 = new Edge<>(vertex1, vertex2, weight, data);
        Edge<V, E> edge2to1 = new Edge<>(vertex2, vertex1, weight, data);

        IList<Edge<V, E>> list1 = vertexEdges.get(vertex1);
        IList<Edge<V, E>> list2 = vertexEdges.get(vertex2);

        if (!list1.contains(edge1to2)) {
            list1.add(edge1to2);
        }

        if (!list2.contains(edge2to1)) {
            list2.add(edge2to1);
        }
        numEdges++;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return numVertices;
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return numEdges;
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of this graph.
     *
     * If there exists multiple valid MSTs, returns any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<Edge<V, E>> findMinimumSpanningTree() {
        IDisjointSets<V> mstHelp = new ArrayDisjointSets<>();
        IList<Edge<V, E>> edgeList = new DoubleLinkedList<>();
        ISet<Edge<V, E>> setEdges = new ChainedHashSet<>();

        for (KVPair<V, IList<Edge<V, E>>> vertices : vertexEdges) {
            mstHelp.makeSet(vertices.getKey());

            for (Edge<V, E> edge: vertices.getValue()) {
                if (!edgeList.contains(edge)) {
                    edgeList.add(edge);
                }
            }

        }

        IList<Edge<V, E>> sortedEdges = Sorter.topKSort(edgeList.size(), edgeList);

        for (Edge<V, E> edge: sortedEdges) {
            if (mstHelp.findSet(edge.getVertex1()) != mstHelp.findSet(edge.getVertex2())) {
                setEdges.add(edge);
                mstHelp.union(edge.getVertex1(), edge.getVertex2());
            }
        }

        return setEdges;
    }

    /**
     * Returns the edges that make up the shortest path from `start` to `end`.
     *
     * The first edge in the output list will be the edge leading out of the `start` node; the last
     * edge in the output list will be the edge connecting to the `end` node.
     *
     * Returns an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from `start` to `end`
     * @throws IllegalArgumentException if `start` or `end` is null or not in the graph
     */
    public IList<Edge<V, E>> findShortestPathBetween(V start, V end) {

        if (!vertexEdges.containsKey(start) || !vertexEdges.containsKey(end)) {
            throw new IllegalArgumentException();
        }
        if (start == null || end == null) {
            throw new IllegalArgumentException();
        }
        IList<Edge<V, E>> result = new DoubleLinkedList<>();
        if (start == end){
            return result;
        }



        IDictionary<V, Vertex<V, E>> vertices = new ArrayDictionary<>();

        for (KVPair<V, IList<Edge<V, E>>> edgeVertex : vertexEdges) {
            vertices.put(edgeVertex.getKey(), new Vertex<>(edgeVertex.getKey()));
        }

        vertices.get(start).distance = 0;

        IPriorityQueue<Vertex<V, E>> mpq = new ArrayHeapPriorityQueue<>();

        mpq.add(vertices.get(start));

        while (!mpq.isEmpty()) {
            Vertex<V, E> u = mpq.removeMin();

            for (Edge<V, E> edge : vertexEdges.get(u.vertex)) {
                    double oldDist = vertices.get(edge.getOtherVertex(u.vertex)).distance;
                    double newDist = edge.getWeight() + vertices.get(u.vertex).distance;

                    if (newDist < oldDist) {
                        Vertex<V, E> newVertex = new Vertex<>(edge.getOtherVertex(u.vertex));
                        newVertex.distance = newDist;
                        newVertex.predecessorEdge = edge;
                        newVertex.predecessor = vertices.get(u.vertex);


                        if (oldDist != Double.POSITIVE_INFINITY) {
                            mpq.replace(vertices.get(edge.getOtherVertex(u.vertex)), newVertex);
                        } else {
                            mpq.add(newVertex);
                        }

                        vertices.put(edge.getOtherVertex(u.vertex), newVertex);
                    }
            }

         }

        Vertex<V, E> vertex = vertices.get(end);

        if (vertex.predecessor == null) {
            throw new NoPathExistsException();
        }

        while (vertex.predecessorEdge != null) {
            result.add(vertex.predecessorEdge);
            vertex = vertex.predecessor;
        }

        IList<Edge<V, E>> resultNew = new DoubleLinkedList<>();

        while (!result.isEmpty()) {
            resultNew.add(result.remove());
        }

        return resultNew;
    }

    private static class Vertex<V, E> implements Comparable<Vertex<V, E>> {

        final V vertex;
        double distance;
        Edge<V, E> predecessorEdge;
        Vertex<V, E> predecessor;


        public Vertex(V vertex) {
            this.vertex = vertex;
            this.distance = Double.POSITIVE_INFINITY;
            this.predecessorEdge = null;
            this.predecessor = null;
        }

        public int compareTo(Vertex<V, E> other) {
            // Define compareTo to determine how your vertices will
            // be ordered in the `IPriorityQueue
            return Double.compare(this.distance, other.distance);
        }
    }
}



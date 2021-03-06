package com.github.mauricioaniche.ck.util;

import com.github.mauricioaniche.ck.metric.RunAfter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DependencySorter {

    public <T> List<Class<? extends T>> sort(List<Class<? extends T>> toSort) {
        // the stack will contain the final list of vertexes
        Stack<Integer> sortedStack = new Stack<>();

        // first step, we build a simple adjacent matrix
        boolean[][] adjacentMatrix = deriveAdjacentMatrix(toSort);

        // the array will keep a list of nodes we visited before
        // all marked as 'not visited' at the beginning
        boolean visited[] = new boolean[toSort.size()];

        // visit all of them, in order
        for (int i = 0; i < toSort.size(); i++)
            if (visited[i] == false)
                topologicalSort(i, visited, adjacentMatrix, sortedStack);

        return sortedStack.stream().map(i -> toSort.get(i)).collect(Collectors.toList());
    }

    private void topologicalSort(int v, boolean[] visited, boolean[][] adjacentMatrix, Stack<Integer> sortedStack) {
        // Mark the current node as visited
        visited[v] = true;

        // Visit the adjacent nodes before adding this one in the solution
        IntStream.range(0, adjacentMatrix[v].length)
                .filter(i -> adjacentMatrix[v][i])
                .filter(i -> !visited[i])
                .forEach(i -> topologicalSort(i, visited, adjacentMatrix, sortedStack));

        // After visiting all adjacent nodes (and their adjacents, ...) first,
        // we can add this one to the solution
        sortedStack.push(v);
    }

    private <T> boolean[][] deriveAdjacentMatrix(List<Class<? extends T>> toSort) {

        final boolean[][] adjacentList = new boolean[toSort.size()][toSort.size()];

        // for each element in the list
        IntStream.range(0, toSort.size())
                // remove the ones that do not contain @RunAfter
                .filter(i -> toSort.get(i).getAnnotation(RunAfter.class) != null)
                // get the list of elements they depend upon, together with the index of the current element
                .mapToObj(i -> Pair.of(i,toSort.get(i).getAnnotation(RunAfter.class).metrics()))
                // for each element, for each dependency, mark it as true in the adjacent list
                // we ignore dependencies that are not in the list (a list might contain method-level
                // class-level dependencies, so we should ignore the ones we are not dealing with here)
                .forEach(p -> Arrays.stream(p.getValue())
                        .map(d -> toSort.indexOf(d))
                        .filter(d -> d != -1)
                        .forEach(d -> adjacentList[p.getKey()][d] = true));

        return adjacentList;
    }

}

package com.github.mauricioaniche.ck.metric;

import com.github.mauricioaniche.ck.CKClassResult;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashSet;
import java.util.Set;

/*
Calculates the tight and loose class cohesion for a class.
For more details see: https://www.aivosto.com/project/help/pm-oo-cohesion.html#TCC_LCC
 */
public class TightClassCohesion implements CKASTVisitor, ClassLevelMetric {
    //Two methods are directly connected if:
    //1. both access the same class-level variable
    //2. their call trees access the same class-level variable (only within the class)
    private Set<ImmutablePair> getDirectConnections(CKClassResult result){
        Set<ImmutablePair> directConnections = new HashSet<>();
        for ()

        return directConnections;
    }

    //Two methods are indirectly connected if:
    //1. they are not directly connected
    //2. they are connected via other methods, e.g. X -> Y -> Z
    private Set<ImmutablePair> getIndirectConnections(CKClassResult result){
        Set<ImmutablePair> indirectConnections = new HashSet<>();

        return indirectConnections;
    }

    public void setResult(CKClassResult result) {
        //maximum number of possible connections (N * (N -1))
        float np = result.getMethods().size() * (result.getMethods().size() -1);

        //number of direct connections (number of edges in the connection graph) in this class
        float directConnections = getDirectConnections(result).size();
        //number of indirect connections in this class
        float indirectConnections = getIndirectConnections(result).size();

        result.setTightClassCohesion(directConnections / np);
        result.setLooseClassCohesion((directConnections + indirectConnections) / np);
    }
}
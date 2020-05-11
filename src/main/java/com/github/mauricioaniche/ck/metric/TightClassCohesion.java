package com.github.mauricioaniche.ck.metric;

import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKMethodResult;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/*
Calculates the tight and loose class cohesion for a class.
For more details see: https://www.aivosto.com/project/help/pm-oo-cohesion.html#TCC_LCC
 */
public class TightClassCohesion implements CKASTVisitor, ClassLevelMetric {
    private HashMap<String, Set<String>> accessedFields = new HashMap<>();

    //Two methods are directly connected if:
    //1. both access the same class-level variable
    //2. their call trees access the same class-level variable (only within the class)
    //This currently has a worst runtime of N * N! This can be improved by using the structure of the invocations
    private Set<ImmutablePair> getDirectConnections(CKClassResult result){
        for (CKMethodResult method : result.getMethods()){
            accessedFields.put(method.getMethodName(), method.getFieldsAccessed());
        }

        HashMap<String, Set<String>> allAccessedFields = new HashMap<>();
        for (CKMethodResult method : result.getMethods()){
            Set<String> allLocalFields = collectAccessedFields(method);
            allLocalFields.addAll(method.getFieldsAccessed());
            allAccessedFields.put(method.getMethodName(), allLocalFields);
        }

        Set<ImmutablePair> directConnections = new HashSet<>();
        for(String methodFirst : allAccessedFields.keySet()){
            for(String methodSecond : allAccessedFields.keySet()){
                Set<String> accessedFieldsFirst = Sets.newHashSet(allAccessedFields.get(methodFirst));
                Set<String> accessedFieldsSecond = allAccessedFields.get(methodSecond);
                accessedFieldsFirst.retainAll(accessedFieldsSecond);
                if(!methodFirst.equals(methodSecond) && accessedFieldsFirst.size() > 0){
                    directConnections.add(new ImmutablePair(methodFirst, methodSecond));
                }
            }
        }

        return directConnections;
    }

    //Collect all accessed fields from the invocation tree of a method
    private Set<String> collectAccessedFields(CKMethodResult method){
        Set<String> allLocalInvocations = method.getMethodInvocationsIndirectLocal().keySet();

        Set<String> allLocalFields = new HashSet<>();
        for (String invocation : allLocalInvocations){
            Set<String> currentFields =  accessedFields.get(invocation);
            if(currentFields != null)
                allLocalFields.addAll(currentFields);
        }

        return allLocalFields;
    }

    //Two methods are indirectly connected if:
    //1. they are not directly connected
    //2. they are connected via other methods, e.g. X -> Y -> Z
    private Set<ImmutablePair> getIndirectConnections(CKClassResult result){
        Set<ImmutablePair> indirectConnections = new HashSet<>();

        return indirectConnections;
    }

    public void setResult(CKClassResult result) {
        MethodInvocationsLocal methodInvocationsLocal = new MethodInvocationsLocal();
        methodInvocationsLocal.extractInvocations(result);

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
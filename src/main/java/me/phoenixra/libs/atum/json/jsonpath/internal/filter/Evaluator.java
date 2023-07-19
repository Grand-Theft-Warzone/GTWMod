package me.phoenixra.libs.atum.json.jsonpath.internal.filter;

import me.phoenixra.libs.atum.json.jsonpath.Predicate;

public interface Evaluator {
    boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx);
}
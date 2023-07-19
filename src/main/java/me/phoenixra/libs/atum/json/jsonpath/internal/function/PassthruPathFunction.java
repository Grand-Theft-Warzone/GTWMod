package me.phoenixra.libs.atum.json.jsonpath.internal.function;

import me.phoenixra.libs.atum.json.jsonpath.internal.PathRef;
import me.phoenixra.libs.atum.json.jsonpath.internal.EvaluationContext;

import java.util.List;

/**
 * Defines the default behavior which is to return the model that is provided as input as output
 *
 * Created by mattg on 6/26/15.
 */
public class PassthruPathFunction implements PathFunction {

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
        return model;
    }
}

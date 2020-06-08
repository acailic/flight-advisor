package rs.interview.backend.service.path.graph;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * for routes price is weight function
 *
 * @author a.ilic
 */
@Component
public class GraphWeightFunction {

    private final Map<DirectedGraphNode,
            Map<DirectedGraphNode, Double>> map = new HashMap<>();

    public void put(DirectedGraphNode tail,
                    DirectedGraphNode head,
                    BigDecimal price) {
        map.putIfAbsent(tail, new HashMap<>());
        map.get(tail).put(head, price.doubleValue());
    }

    public Double get(DirectedGraphNode tail,
                      DirectedGraphNode head) {
        return map.get(tail) == null ? Double.POSITIVE_INFINITY : map.get(tail).get(head);
    }
}

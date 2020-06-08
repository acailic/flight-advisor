package rs.interview.backend.service.path;

import rs.interview.backend.service.path.graph.DirectedGraphNode;

import java.util.List;

public interface PathFinder {

    List<DirectedGraphNode> search(DirectedGraphNode source, DirectedGraphNode target);


}

package rs.interview.backend.service.path.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author a.ilic
 */
public class DirectedGraphNode {
    protected final Long id;
    private final Set<DirectedGraphNode> children;
    private final Set<DirectedGraphNode> parents;

    private final Set<DirectedGraphNode> childrenWrapper;
    private final Set<DirectedGraphNode> parentWrapper;

    public DirectedGraphNode(Long id) {
        this.id = id;

        this.children = new LinkedHashSet<>();
        this.parents = new LinkedHashSet<>();

        this.childrenWrapper = Collections.unmodifiableSet(children);
        this.parentWrapper = Collections.unmodifiableSet(parents);
    }


    public void addChild(DirectedGraphNode child) {
        children.add(child);
        child.parents.add(this);
    }


    public boolean hasChild(DirectedGraphNode childCandidate) {
        return children.contains(childCandidate);
    }


    public Collection<DirectedGraphNode> children() {
        return childrenWrapper;
    }


    public Collection<DirectedGraphNode> parents() {
        return parentWrapper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirectedGraphNode that = (DirectedGraphNode) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "[DirectedGraphNode " + id + "]";
    }


    public void removeChild(DirectedGraphNode child) {
        child.parents.remove(this);
        children.remove(child);
    }

    public Long getId() {
        return id;
    }

    public void clear() {
        for (DirectedGraphNode child : children) {
            child.parents.remove(this);
        }

        for (DirectedGraphNode parent : parents) {
            parent.children.remove(this);
        }

        children.clear();
        parents.clear();
    }
}


package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramEdge;
import org.vstu.nodelinkdiagram.DiagramElement;
import org.vstu.nodelinkdiagram.DiagramModel;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kalnov Nikita, Litovkin Dmitry
 */
public abstract class ORM_AbstractRoleConstraintAssociation extends DiagramEdge {

    ORM_AbstractRoleConstraintAssociation(DiagramModel diagram, ORM_RoleConstraint source, ORM_RoleSequence target) {
        super(diagram, source, target);
    }

    // TODO: need to perform an audit!!!

    // Source-node
    public ORM_RoleConstraint getSource() {
        return getSources(ORM_RoleConstraint.class).findFirst().get();
    }

    // Target-edge
    public ORM_RoleSequence getTarget() {
        return getTargets(ORM_RoleSequence.class).findFirst().get();
    }


    static boolean evaluateConnectivityApproximately(DiagramElement source, DiagramElement target) {
        return source instanceof ORM_RoleConstraint && target instanceof ORM_RoleSequence;
    }

    @Override
    public ValidateStatus validate() {
        setValidateStatus(ValidateStatus.Acceptable);
        clearDefects();

        List<ORM_RoleSequence> incidenceNodes =
                getIncidenceElements(ORM_RoleSequence.class).sorted().collect(Collectors.toList());

        if (getDiagramOwner().getElements(ORM_UndirectedRoleConstraintAssociation.class)
                .filter(e -> e != this)
                .anyMatch(e -> incidenceNodes.equals(
                        e.getIncidenceElements(ORM_RoleSequence.class)
                                .sorted()
                                .collect(Collectors.toList())))) {
            addError("Constraint and RoleSequence are already connected");
        }

        return getValidateStatus();
    }
}
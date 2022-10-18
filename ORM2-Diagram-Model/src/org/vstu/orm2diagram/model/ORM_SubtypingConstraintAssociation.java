package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramEdge;
import org.vstu.nodelinkdiagram.DiagramElement;
import org.vstu.nodelinkdiagram.DiagramModel;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Baklan Varvara, Litovkin Dmitry
 */
public class ORM_SubtypingConstraintAssociation extends DiagramEdge {

    protected ORM_SubtypingConstraintAssociation(DiagramModel diagram, ORM_SubtypingConstraint source, ORM_Subtyping target) {
        super(diagram, source, target);
    }

    static boolean evaluateConnectivityApproximately(DiagramElement source, DiagramElement target) {
        return source instanceof ORM_SubtypingConstraint && target instanceof ORM_Subtyping;
    }

    // Source-node
    public ORM_SubtypingConstraint getSource() {
        return getSources(ORM_SubtypingConstraint.class).findFirst().get();
    }

    // Target-edge
    public ORM_Subtyping getTarget() {
        return getTargets(ORM_Subtyping.class).findFirst().get();
    }

    @Override
    public ValidateStatus validate() {
        setValidateStatus(ValidateStatus.Acceptable);
        clearDefects();

        List<DiagramElement> incidenceNodes = getIncidenceElements(DiagramElement.class).sorted().collect(Collectors.toList());

        if (getDiagramOwner().getElements(ORM_SubtypingConstraintAssociation.class).filter(e -> e != this)
                .anyMatch(e -> incidenceNodes.equals(e.getIncidenceElements(DiagramElement.class).sorted().collect(Collectors.toList())))) {
            addError("Subtyping Constraint and Subtyping are connected by several Constraint Association");
        }

        return getValidateStatus();
    }
}

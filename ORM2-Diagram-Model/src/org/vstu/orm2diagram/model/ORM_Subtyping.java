package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramEdge;
import org.vstu.nodelinkdiagram.DiagramElement;
import org.vstu.nodelinkdiagram.DiagramModel;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Litovkin Dmitry
 */
public class ORM_Subtyping extends DiagramEdge {

    // ------------------------------------------------------------------------------------------

    ORM_Subtyping(DiagramModel diagram, ORM_EntityType source, ORM_EntityType target) {
        super(diagram, source, target);
    }

    // ------------------------------------------------------------------------------------------

    static boolean evaluateConnectivityApproximately(DiagramElement source, DiagramElement target) {
        return source instanceof ORM_EntityType && target instanceof ORM_EntityType;
    }

    // ------------------------------------------------------------------------------------------

    public ORM_EntityType getSource() {
        return getSources(ORM_EntityType.class).findFirst().get();
    }

    public ORM_EntityType getTarget() {
        return getTargets(ORM_EntityType.class).findFirst().get();
    }

    // ------------------------------------------------------------------------------------------

    @Override
    public ValidateStatus validate() {

        setValidateStatus(ValidateStatus.Acceptable);
        clearDefects();

        List<ORM_EntityType> incidenceNodes = getIncidenceElements(ORM_EntityType.class).sorted().collect(Collectors.toList());

        if (getSource() == getTarget()) {
            addError("This is a loop");

        } else if (getDiagramOwner().getElements(ORM_Subtyping.class)
                .filter(e -> e != this)
                .anyMatch(e -> incidenceNodes.equals(
                        e.getIncidenceElements(ORM_EntityType.class)
                                .sorted()
                                .collect(Collectors.toList())))) {
            addError("A pair of Entity Types to connect several Subtypings");
        }

        return getValidateStatus();
    }
}

package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramEdge;
import org.vstu.nodelinkdiagram.DiagramElement;
import org.vstu.nodelinkdiagram.DiagramModel;
import org.vstu.nodelinkdiagram.DiagramNode;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Litovkin Dmitry
 */
public class ORM_RoleAssociation extends DiagramEdge {

    // ------------------------------------------------------------------------------------------

    ORM_RoleAssociation(DiagramModel diagram, ORM_ObjectType source, ORM_Role target) {
        super(diagram, source, target);
    }

    // ------------------------------------------------------------------------------------------

    static boolean evaluateConnectivityApproximately(DiagramElement source, DiagramElement target) {
        return source instanceof ORM_ObjectType && target instanceof ORM_Role;
    }

    // ------------------------------------------------------------------------------------------

    public ORM_ObjectType getSource() {
        return getSources(ORM_ObjectType.class).findFirst().get();
    }

    public ORM_Role getTarget() {
        return getTargets(ORM_Role.class).findFirst().get();
    }

    // ------------------------------------------------------------------------------------------

    @Override
    public ValidateStatus validate() {

        setValidateStatus(ValidateStatus.Acceptable);
        clearDefects();

        List<DiagramNode> incidenceNodes = getIncidenceElements(DiagramNode.class).sorted().collect(Collectors.toList());

        if (getDiagramOwner().getElements(ORM_RoleAssociation.class)
                .filter(e -> e != this)
                .anyMatch(e -> incidenceNodes.equals(
                        e.getIncidenceElements(DiagramNode.class)
                                .sorted()
                                .collect(Collectors.toList())))) {
            addError("Object Type and Role are connected by several Role Associations");
        }

        return getValidateStatus();

    }
}

package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramEdge;
import org.vstu.nodelinkdiagram.DiagramElement;
import org.vstu.nodelinkdiagram.DiagramModel;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;

import java.util.stream.Stream;

/**
 * @author Litovkin Dmitry
 */
public class Test_EdgeForConnectRoleSequences extends DiagramEdge {

    protected Test_EdgeForConnectRoleSequences(DiagramModel diagram, ORM_RoleSequence source, ORM_RoleSequence target) {
        super(diagram, source, target);
    }

    // ------------------------------------------------------------------------------------------

    static boolean evaluateConnectivityApproximately(DiagramElement source, DiagramElement target) {
        return source instanceof ORM_RoleSequence && target instanceof ORM_RoleSequence;
    }

    // ------------------------------------------------------------------------------------------

    public Stream<ORM_RoleSequence> getEnds() {
        return getIncidenceElements(ORM_RoleSequence.class);
    }

    // ------------------------------------------------------------------------------------------

    @Override
    public ValidateStatus validate() {
        setValidateStatus(ValidateStatus.Acceptable);
        clearDefects();
        return getValidateStatus();
    }
}

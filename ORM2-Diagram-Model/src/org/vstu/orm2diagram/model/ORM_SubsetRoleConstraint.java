package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramModel;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;

/**
 * @author Kalnov Nikita, Litovkin Dmitry
 */
public class ORM_SubsetRoleConstraint extends ORM_RoleConstraint {

    protected ORM_SubsetRoleConstraint(DiagramModel diagram) {
        super(diagram);
    }

    // TODO: need to perform an audit!!!
    @Override
    public ValidateStatus validate() {
        super.validate();

        long connectedRoleSequencesNumber = getIncidenceElements(ORM_UndirectedRoleConstraintAssociation.class).count();
        if(connectedRoleSequencesNumber > 2)
            addError("Simple Subset Constraint can   be connected to two RoleSequence's only");

        return getValidateStatus();
    }
}

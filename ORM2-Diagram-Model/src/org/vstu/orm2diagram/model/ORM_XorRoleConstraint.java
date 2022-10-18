package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramModel;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kalnov Nikita, Litovkin Dmitry
 */
public class ORM_XorRoleConstraint extends ORM_RoleConstraint {

    protected ORM_XorRoleConstraint(DiagramModel diagram) {
        super(diagram);
    }

    // TODO: need to perform an audit!!!
    @Override
    public ValidateStatus validate() {
        super.validate();

        List<ORM_Role> roles = getIncidenceElements(ORM_UndirectedRoleConstraintAssociation.class)
                .map(association ->
                        association.getIncidenceElements(ORM_SequenceFromOneRole.class).findFirst().get().getItem(0)
                ).collect(Collectors.toList());

        boolean allSequencesAreMandatory = true;
        for(ORM_Role role : roles) {
            if(role.isMandatory())
                addWarning("At least one role already has mandatory constraint");
            allSequencesAreMandatory = allSequencesAreMandatory && role.isMandatory();
        }

        if(allSequencesAreMandatory && roles.size() > 0)
            addError("Exclusive-or constraint is contradicted with roles mandatory constraints");

        return getValidateStatus();
    }
}

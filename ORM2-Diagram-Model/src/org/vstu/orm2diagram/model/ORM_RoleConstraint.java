package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramModel;
import org.vstu.nodelinkdiagram.StandaloneNode;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kalnov Nikita, Litovkin Dmitry
 */
public abstract class ORM_RoleConstraint extends StandaloneNode {

    protected ORM_RoleConstraint(DiagramModel diagram) {
        super(diagram);
    }

    // TODO: need to perform an audit!!!
    @Override
    public ValidateStatus validate() {
        setValidateStatus(ValidateStatus.Acceptable);
        clearDefects();

        List<ORM_RoleSequence> roleSequences = getIncidenceElements(ORM_UndirectedRoleConstraintAssociation.class)
                .map(association -> association.getIncidenceElements(ORM_RoleSequence.class).findFirst().get())
                .collect(Collectors.toList());
        if(roleSequences.size() < 2)
            addWarning("Constraint isn't connected to any role sequence");
        else {
            boolean areRoleSequencesCompatible = true;

            ORM_RoleSequence standardRoleSequence = roleSequences.get(0);
            List<ORM_RoleSequence> otherRoleSequences = roleSequences.subList(1, roleSequences.size());
            for(ORM_RoleSequence roleSequence : otherRoleSequences) {
                areRoleSequencesCompatible = areRoleSequencesCompatible && roleSequence.isCompatibleWith(standardRoleSequence);
            }

            if(!areRoleSequencesCompatible)
                addError("Constraint is connected to incompatible role sequences");
        }

        return getValidateStatus();
    }
}

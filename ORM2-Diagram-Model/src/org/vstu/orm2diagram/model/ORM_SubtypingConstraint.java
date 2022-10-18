package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramModel;
import org.vstu.nodelinkdiagram.StandaloneNode;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;

/**
 * @author Baklan Varvara, Litovkin Dmitry
 */
public abstract class ORM_SubtypingConstraint extends StandaloneNode {

    protected ORM_SubtypingConstraint(DiagramModel diagram) {
        super(diagram);
    }

    @Override
    public ValidateStatus validate() {
        setValidateStatus(ValidateStatus.Acceptable);
        clearDefects();

        if (getDiagramOwner().getElements(ORM_SubtypingConstraintAssociation.class).filter(e -> e.getSource() == this).count() < 2) {
            addWarning("Subtyping Constraint must be connected to at least two Subtyping");
        }

        // TODO: target-дуги должны иметь общий целевой узел, иначе ошибка

        return getValidateStatus();
    }
}
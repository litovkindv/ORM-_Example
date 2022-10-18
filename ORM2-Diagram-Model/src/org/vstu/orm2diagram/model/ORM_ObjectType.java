package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramElement;
import org.vstu.nodelinkdiagram.DiagramModel;
import org.vstu.nodelinkdiagram.StandaloneNode;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;

/**
 * @author Litovkin Dmitry
 */
public abstract class ORM_ObjectType extends StandaloneNode {

    // ------------------------------------------------------------------------------------------

    ORM_ObjectType(DiagramModel diagram) {
        super(diagram);
    }

    // ------------------------------------------------------------------------------------------

    private String _name;

    public String getName() {
        assert_ElementIsNotDestroyed();
        return _name;
    }

    public void setName(String name) {
        beginModify();
        _name = name;
        endModify();
    }

    // ------------------------------------------------------------------------------------------

    @Override
    protected void equateStateWith(DiagramElement prototype) {
        super.equateStateWith(prototype);

        ORM_ObjectType otherObjectType = (ORM_ObjectType) prototype;
        setName(otherObjectType.getName());
    }

    // ------------------------------------------------------------------------------------------

    @Override
    public ValidateStatus validate() {
        setValidateStatus(ValidateStatus.Acceptable);
        clearDefects();

        if (getName().isEmpty()) {
            addError("Name is empty");

        } else if (
                getDiagramOwner().getElements(ORM_ObjectType.class)
                        .filter(e -> e != this)
                        .anyMatch(e -> e.getName().equals(getName()))) {

            addError("Two or more Object Types have the same Name");
        }

        return getValidateStatus();
    }
}

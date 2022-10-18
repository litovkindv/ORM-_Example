package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.CompositeNode;
import org.vstu.nodelinkdiagram.DiagramElement;
import org.vstu.nodelinkdiagram.ItemNode;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;

/**
 * @author Litovkin Dmitry, Kalnov Nikita
 */
public class ORM_Role extends ItemNode {
    // ------------------------------------------------------------------------------------------

    protected ORM_Role(CompositeNode nodeOwner) {
        super(nodeOwner);
    }

    // ------------------------------------------------------------------------------------------

    private String _name;
    private boolean _mandatory = false;
    private boolean _unique = false;

    public String getName() {
        assert_ElementIsNotDestroyed();
        return _name;
    }

    public void setName(String name) {
        beginModify();
        _name = name;
        endModify();
    }

    public boolean isMandatory() {
        return _mandatory;
    }

    public void setMandatory(boolean mandatory) {
        beginModify();
        _mandatory = mandatory;
        endModify();
    }

    public boolean isUnique() {
        return _unique;
    }

    public void setUnique(boolean unique) {
        beginModify();
        _unique = unique;
        endModify();
    }

    // ------------------------------------------------------------------------------------------

    protected void equateStateWith(DiagramElement prototype) {

        ORM_Role otherRole = (ORM_Role) prototype;
        setName(otherRole.getName());
        setMandatory(otherRole.isMandatory());
        setUnique(otherRole.isUnique());
    }

    // ------------------------------------------------------------------------------------------

    @Override
    public ValidateStatus validate() {
        setValidateStatus(ValidateStatus.Acceptable);
        clearDefects();
        return getValidateStatus();
    }
}

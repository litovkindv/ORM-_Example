package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.*;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Litovkin Dmitry, Kalnov Nikita
 */
public abstract class ORM_RoleSequence extends AggregatedNode<ORM_Role> {

    // ------------------------------------------------------------------------------------------

    protected ORM_RoleSequence(DiagramModel diagram) {
        super(diagram);
    }

    // ------------------------------------------------------------------------------------------

    private boolean _unique;

    public boolean isUnique() {
        return _unique;
    }

    public void setUnique(boolean unique) {
        beginModify();
        _unique = unique;
        endModify();
    }

    @Override
    protected void equateStateWith(DiagramElement prototype) {
        ORM_RoleSequence prototypeRoleSequence = (ORM_RoleSequence) prototype;

        assert prototypeRoleSequence.getItemCount() == _itemCountLimit :
                "Two corresponding Role Sequences must have the same length";

        setUnique(prototypeRoleSequence.isUnique());

        if (getItemCount() > 0) {
            _items.clear();
        }

        if (getDiagramOwner() instanceof MainDiagramModel) {
            prototypeRoleSequence._items.forEach(i ->
                    addItem((ORM_Role) i.getCommittedState()));

        } else if (getDiagramOwner() instanceof ClientDiagramModel) {
            ClientDiagramModel myClientDiagram = (ClientDiagramModel) getDiagramOwner();
            prototypeRoleSequence._items.forEach(i ->
                    addItem((ORM_Role) findCorrespondingItemFor(i).get()));

        } else {
            assert false : "It can't happen";
        }
    }

    // TODO: need to perform an audit!!!
    public boolean isCompatibleWith(ORM_RoleSequence other) {
        boolean isArityCompatible = other._itemCountLimit == _itemCountLimit;

        boolean isEntityCompatible = true;
        for(int i = 0; i < _itemCountLimit; i+= 1) {
            ORM_Role role = _items.get(i);
            Optional<ORM_ObjectType> connectedEntity = role
                    .getIncidenceElements(ORM_RoleAssociation.class)
                    .map(ORM_RoleAssociation::getSource)
                    .findFirst();

            if(!connectedEntity.isPresent())
                return false;

            ORM_Role otherRole = other._items.get(i);
            Optional<ORM_ObjectType> otherRoleConnectedEntity = otherRole
                    .getIncidenceElements(ORM_RoleAssociation.class)
                    .map(ORM_RoleAssociation::getSource)
                    .findFirst();

            if(!otherRoleConnectedEntity.isPresent())
                return false;

            isEntityCompatible = isEntityCompatible && connectedEntity.get().equals(otherRoleConnectedEntity.get());
        }

        return isArityCompatible && isEntityCompatible;
    }

    // ------------------------------------------------------------------------------------------

    // TODO: Валидация реализована только последовательностей длиной не более двух ролей
    @Override
    public ValidateStatus validate() {
        setValidateStatus(ValidateStatus.Acceptable);
        clearDefects();

        List<ORM_Role> roles = getItems().sorted().collect(Collectors.toList());

        if (getItemCount() < _itemCountLimit) {
            addError("The Role Sequence is not completed");

        } else if (
                getItems().filter(i -> i == getItem(0)).count() > 1) {
            addError("The Role Sequence contains repeated Roles");

        } else if (
                !getItems().allMatch(i -> i.getNodeOwner() == getItem(0).getNodeOwner())) {
            addError("Roles belong to different Predicates");

        } else if (
                getDiagramOwner().getElements(ORM_RoleSequence.class)
                        .filter(e -> e != this)
                        .anyMatch(e -> roles.equals(
                                e.getItems().sorted().collect(Collectors.toList())))) {
            addError("The Role Sequence is duplicated. The diagram may contain only one Sequence with the Roles");
        }

        // TODO: need to perform an audit!!!
        for(ORM_Role role : roles) {
            if(isUnique() && role.isUnique()) {
                addError("Role uniqueness is implied by Role Sequence uniqueness");
            }
        }

        return getValidateStatus();
    }
}
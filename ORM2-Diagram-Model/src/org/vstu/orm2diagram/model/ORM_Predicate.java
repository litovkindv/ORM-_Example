package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.CompositeNode;
import org.vstu.nodelinkdiagram.DiagramModel;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;

import java.util.Iterator;

/**
 * @author Litovkin Dmitry
 */
public abstract class ORM_Predicate extends CompositeNode<ORM_Role> {

    protected ORM_Predicate(DiagramModel diagram) {
        super(diagram);
    }

    // TODO: Валидация реализована только для унарных и бинарных предикатов
    @Override
    public ValidateStatus validate() {
        setValidateStatus(ValidateStatus.Acceptable);
        clearDefects();

        String firstRoleName = getItem(0).getName();
        long roleCountWithSameName = getItems().filter(i -> i.getName().equals(firstRoleName)).count();

        if (firstRoleName.isEmpty() && roleCountWithSameName == 2) {
            addError("Two roles can not not have empty Name");

        } else if (!firstRoleName.isEmpty() && roleCountWithSameName == 2) {
            addError("Roles can not have the same Name");
        }

        // TODO: Способ валидации совпадает с валидацией ClientDiagramModel - обобщить
        ValidateStatus itemStatus;
        Iterator<ORM_Role> iter = getItems().iterator();
        while (iter.hasNext()) {
            ORM_Role role = iter.next();

            itemStatus = role.validate();
            setValidateStatus(getValidateStatus().combineWith(itemStatus));
        }

        return getValidateStatus();
    }
}

package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramModel;
import org.vstu.nodelinkdiagram.statuses.CommitStatus;

/**
 * @author Litovkin Dmitry
 */
public class ORM_UnaryPredicate extends ORM_Predicate {

    protected ORM_UnaryPredicate(DiagramModel diagram) {
        super(diagram);

        createItem(ORM_Role.class, CommitStatus.Uncommitted);
    }
}

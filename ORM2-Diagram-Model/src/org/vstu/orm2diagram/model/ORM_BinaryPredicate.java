package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramModel;
import org.vstu.nodelinkdiagram.statuses.CommitStatus;

/**
 * @author Litovkin Dmitry
 */
public class ORM_BinaryPredicate extends ORM_Predicate {

    protected ORM_BinaryPredicate(DiagramModel diagram) {
        super(diagram);

        createItem(ORM_Role.class, CommitStatus.Uncommitted);
        createItem(ORM_Role.class, CommitStatus.Uncommitted);
    }
}

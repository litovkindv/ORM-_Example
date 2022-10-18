package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramModel;

/**
 * @author Litovkin Dmitry
 */
public class ORM_SequenceFromTwoRoles extends ORM_RoleSequence {

    protected ORM_SequenceFromTwoRoles(DiagramModel diagram) {
        super(diagram);

        _itemCountLimit = 2;
    }
}

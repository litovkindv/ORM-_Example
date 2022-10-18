package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramModel;

/**
 * @author Litovkin Dmitry
 */
public class ORM_SequenceFromOneRole extends ORM_RoleSequence {

    protected ORM_SequenceFromOneRole(DiagramModel diagram) {
        super(diagram);

        _itemCountLimit = 1;
    }
}

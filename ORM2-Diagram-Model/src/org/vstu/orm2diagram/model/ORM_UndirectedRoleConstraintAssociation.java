package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.DiagramModel;

/**
 * @author Kalnov Nikita, Litovkin Dmitry
 */
public class ORM_UndirectedRoleConstraintAssociation extends ORM_AbstractRoleConstraintAssociation {

    ORM_UndirectedRoleConstraintAssociation(DiagramModel diagram, ORM_RoleConstraint source, ORM_RoleSequence target) {
        super(diagram, source, target);
    }
}

package org.vstu.orm2diagram.model;

import org.vstu.nodelinkdiagram.*;
import org.vstu.nodelinkdiagram.util.Point;

import java.util.Optional;

/**
 * @author Litovkin Dmitry, Baklan Varvara, Kalnov Nikita
 */
public class ORM_DiagramFactory extends DiagramElementFactory {

    @Override
    public <E extends DiagramNode> E createNode(DiagramModel model, Class<E> nodeType) {

        E node = null;

        if (nodeType == ORM_EntityType.class) {
            ORM_EntityType entityType = new ORM_EntityType(model);
            entityType.setPosition(new Point(10, 15));
            entityType.setName("Entity Type");
            node = (E) entityType;

        } else if (nodeType == ORM_ValueType.class) {
            ORM_ValueType valueType = new ORM_ValueType(model);
            valueType.setPosition(new Point(10, 15));
            valueType.setName("Value Type");
            node = (E) valueType;

        } else if (nodeType == ORM_UnaryPredicate.class) {
            ORM_UnaryPredicate predicate = new ORM_UnaryPredicate(model);
            predicate.setPosition(new Point(10, 15));
            node = (E) predicate;

        } else if (nodeType == ORM_BinaryPredicate.class) {
            ORM_BinaryPredicate predicate = new ORM_BinaryPredicate(model);
            predicate.setPosition(new Point(10, 15));
            node = (E) predicate;

        } else if (nodeType == ORM_SequenceFromOneRole.class) {
            ORM_SequenceFromOneRole roleSequence = new ORM_SequenceFromOneRole(model);
            node = (E) roleSequence;

        } else if (nodeType == ORM_SequenceFromTwoRoles.class) {
            ORM_SequenceFromTwoRoles roleSequence = new ORM_SequenceFromTwoRoles(model);
            node = (E) roleSequence;

        } else if (nodeType == ORM_ExclusionSubtypingConstraint.class) {
            ORM_ExclusionSubtypingConstraint constraint = new ORM_ExclusionSubtypingConstraint(model);
            constraint.setPosition(new Point(10, 15));
            node = (E) constraint;

        } else if (nodeType == ORM_InclusiveOrSubtypingConstraint.class) {
            ORM_InclusiveOrSubtypingConstraint constraint = new ORM_InclusiveOrSubtypingConstraint(model);
            constraint.setPosition(new Point(10, 15));
            node = (E) constraint;

        } else if (nodeType == ORM_XorSubtypingConstraint.class) {
            ORM_XorSubtypingConstraint constraint = new ORM_XorSubtypingConstraint(model);
            constraint.setPosition(new Point(10, 15));
            node = (E) constraint;

        } else if (nodeType == ORM_EqualityRoleConstraint.class) {
            ORM_EqualityRoleConstraint constraint = new ORM_EqualityRoleConstraint(model);
            constraint.setPosition(new Point(10, 15));
            node = (E) constraint;

        } else if (nodeType == ORM_ExclusionRoleConstraint.class) {
            ORM_ExclusionRoleConstraint constraint = new ORM_ExclusionRoleConstraint(model);
            constraint.setPosition(new Point(10, 15));
            node = (E) constraint;

        } else if (nodeType == ORM_SubsetRoleConstraint.class) {
            ORM_SubsetRoleConstraint constraint = new ORM_SubsetRoleConstraint(model);
            constraint.setPosition(new Point(10, 15));
            node = (E) constraint;

        } else if (nodeType == ORM_XorRoleConstraint.class) {
            ORM_XorRoleConstraint constraint = new ORM_XorRoleConstraint(model);
            node = (E) constraint;

        } else if (nodeType == ORM_InclusiveOrRoleConstraint.class) {
            ORM_InclusiveOrRoleConstraint constraint = new ORM_InclusiveOrRoleConstraint(model);
            node = (E) constraint;

        } else {
            assert false : "It can't happen";
        }

        return node;
    }

    @Override
    public <I extends ItemNode> I createItem(CompositeNode nodeOwner, Class<I> itemType) {
        I item = null;

        if (itemType == ORM_Role.class) {
            ORM_Role role = new ORM_Role(nodeOwner);
            role.setName("Role");
            item = (I) role;
        } else {
            assert false : "It can't happen";
        }

        return item;
    }

    @Override
    public <E extends DiagramEdge> E connectBy(DiagramModel model, DiagramElement source, DiagramElement target, Class<E> edgeType) {

        E edge = null;

        if (edgeType == ORM_Subtyping.class) {
            edge = (E) new ORM_Subtyping(model, (ORM_EntityType) source, (ORM_EntityType) target);

        } else if (edgeType == ORM_RoleAssociation.class) {
            edge = (E) new ORM_RoleAssociation(model, (ORM_ObjectType) source, (ORM_Role) target);

        } else if (edgeType == Test_EdgeForConnectRoleSequences.class) {
            edge = (E) new Test_EdgeForConnectRoleSequences(model, (ORM_RoleSequence) source, (ORM_RoleSequence) target);

        } else if (edgeType == ORM_SubtypingConstraintAssociation.class) {
            edge = (E) new ORM_SubtypingConstraintAssociation(model, (ORM_SubtypingConstraint) source, (ORM_Subtyping) target);

        // TODO: need to perform an audit!!!
        } else if (edgeType == ORM_UndirectedRoleConstraintAssociation.class || edgeType == ORM_DirectedRoleConstraintAssociation.class) {

            ORM_RoleSequence sequence = null;
            if(target instanceof ORM_RoleSequence) {
                sequence = (ORM_RoleSequence) target;

            } else if (target instanceof ORM_Role) {
                ORM_Role role = (ORM_Role)target;
                Optional<ORM_SequenceFromOneRole> existingSequence = model.getElements(ORM_SequenceFromOneRole.class)
                        .filter(s -> s.getItem(0).equals(role)
                        )
                        .findFirst();
                sequence = existingSequence.get();

            } else if (target instanceof ORM_Predicate) {
                ORM_Predicate predicate = (ORM_Predicate)target;
                int arity = predicate.getItemCount();

                // Внимание! Костыль
                if(arity == 2) {
                    Optional<ORM_SequenceFromTwoRoles> existingSequence = model.getElements(ORM_SequenceFromTwoRoles.class)
                            .filter(s -> s.getItem(0).equals(predicate.getItem(0))
                                    && s.getItem(1).equals(predicate.getItem(1))
                            )
                            .findFirst();
                    sequence = existingSequence.get();
                }
            }

            if (edgeType == ORM_UndirectedRoleConstraintAssociation.class) {
                edge = (E) new ORM_UndirectedRoleConstraintAssociation(model, (ORM_RoleConstraint) source, sequence);
            } else {
                edge = (E) new ORM_DirectedRoleConstraintAssociation(model, (ORM_RoleConstraint) source, sequence);
            }

        } else {
            assert false : "It can't happen";
        }

        return edge;
    }
}

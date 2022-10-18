package org.vstu.orm2diagram.model;

import org.junit.jupiter.api.Test;
import org.vstu.nodelinkdiagram.ClientDiagramModel;
import org.vstu.nodelinkdiagram.DiagramElement;
import org.vstu.nodelinkdiagram.MainDiagramModel;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;
import org.vstu.orm2diagram.model.finalized_entities.Test_DiagramClient;
import org.vstu.orm2diagram.model.test_util.MyAsserts;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Baklan Varvara, Litovkin Dmitry
 */
class ORM_SubtypingConstraintTest {

    // TODO: need to perform an audit!!!

    @Test
    void createORM_SubtypingConstraint() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        ORM_ExclusionSubtypingConstraint exclusionConstraint = clientModel.createNode(ORM_ExclusionSubtypingConstraint.class);
        ORM_XorSubtypingConstraint exclusionOrConstraint = clientModel.createNode(ORM_XorSubtypingConstraint.class);
        ORM_InclusiveOrSubtypingConstraint inclusiveOrConstraint = clientModel.createNode(ORM_InclusiveOrSubtypingConstraint.class);

        clientModel.commit();

        ValidateStatus validateStatus = clientModel.validate();

        assertEquals(ValidateStatus.Intermediate, validateStatus);
        assertEquals(ValidateStatus.Intermediate, exclusionConstraint.getValidateStatus());
        assertEquals(ValidateStatus.Intermediate, exclusionOrConstraint.getValidateStatus());
        assertEquals(ValidateStatus.Intermediate, inclusiveOrConstraint.getValidateStatus());
    }

    @Test
    void deleteElementConstraintInDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        ORM_ExclusionSubtypingConstraint exclusionConstraint = clientModel.createNode(ORM_ExclusionSubtypingConstraint.class);

        clientModel.commit();

        clientModel.beginUpdate();
        List<DiagramElement> deletedElements = clientModel.removeElement(exclusionConstraint).collect(Collectors.toList());
        clientModel.commit();

        assertEquals(List.of(exclusionConstraint), deletedElements);
        MyAsserts.assertStreamEquals(Stream.empty(), clientModel.getElements(DiagramElement.class));
    }

    @Test
    void rollbackCreatedElementsInDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        ORM_ExclusionSubtypingConstraint exclusionConstraint = clientModel.createNode(ORM_ExclusionSubtypingConstraint.class);
        clientModel.rollback();

        MyAsserts.assertStreamEquals(Stream.empty(), clientModel.getElements(DiagramElement.class));
    }

    @Test
    void rollbackDeleteElementsInDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        ORM_ExclusionSubtypingConstraint exclusionConstraint = clientModel.createNode(ORM_ExclusionSubtypingConstraint.class);

        clientModel.commit();

        clientModel.beginUpdate();
        List<DiagramElement> deletedElements = clientModel.removeElement(exclusionConstraint).collect(Collectors.toList());
        clientModel.rollback();

        MyAsserts.assertStreamEquals(Stream.of(exclusionConstraint), clientModel.getElements(DiagramElement.class));
    }
}
package org.vstu.orm2diagram.model;

import org.junit.jupiter.api.Test;
import org.vstu.nodelinkdiagram.ClientDiagramModel;
import org.vstu.nodelinkdiagram.DiagramElement;
import org.vstu.nodelinkdiagram.MainDiagramModel;
import org.vstu.nodelinkdiagram.statuses.CommitStatus;
import org.vstu.nodelinkdiagram.statuses.UpdateStatus;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;
import org.vstu.nodelinkdiagram.statuses.ValidationLevel;
import org.vstu.orm2diagram.model.finalized_entities.Test_DiagramClient;
import org.vstu.orm2diagram.model.test_util.MyAsserts;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Baklan Varvara, Litovkin Dmitry
 */
class ORM_SubtypingConstraintAssociationTest {

    // TODO: need to perform an audit!!!

    // Попытка соединить дугой недопустимый узел-источник
    @Test
    void tryConnectUnacceptableSource() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        ORM_ValueType source = clientModel.createNode(ORM_ValueType.class);

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_Subtyping target = clientModel.connectBy(male, person, ORM_Subtyping.class);

        boolean weakConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_SubtypingConstraintAssociation.class, ValidationLevel.Weak);
        boolean strongConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_SubtypingConstraintAssociation.class, ValidationLevel.Strong);

        assertFalse(weakConnectivityEvaluation);
        assertFalse(strongConnectivityEvaluation);

        MyAsserts.assertStreamEquals(Stream.of(source, target, person, male).sorted(), clientModel.getElements(DiagramElement.class).sorted());
        MyAsserts.assertStreamEquals(Stream.of(source, target, person, male).sorted(), clientModel.getElements(CommitStatus.Uncommitted, DiagramElement.class).sorted());

        assertThrows(ClassCastException.class, () -> clientModel.connectBy(source, target, ORM_SubtypingConstraintAssociation.class));
    }

    // Попытка соединить дугой недопустимый целевой узел
    @Test
    void tryConnectUnacceptableTarget() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_ExclusionSubtypingConstraint source = clientModel.createNode(ORM_ExclusionSubtypingConstraint.class);
        ORM_ValueType target = clientModel.createNode(ORM_ValueType.class);

        boolean weakConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_SubtypingConstraintAssociation.class, ValidationLevel.Weak);
        boolean strongConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_SubtypingConstraintAssociation.class, ValidationLevel.Strong);

        assertFalse(weakConnectivityEvaluation);
        assertFalse(strongConnectivityEvaluation);

        MyAsserts.assertStreamEquals(Stream.of(source, target).sorted(), clientModel.getElements(DiagramElement.class).sorted());
        MyAsserts.assertStreamEquals(Stream.of(source, target).sorted(), clientModel.getElements(CommitStatus.Uncommitted, DiagramElement.class).sorted());

        assertThrows(ClassCastException.class, () -> clientModel.connectBy(source, target, ORM_SubtypingConstraintAssociation.class));
    }

    // Попытка соединить дугой недопустимые узлы
    @Test
    void tryConnectUnacceptableNodes() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_ValueType source = clientModel.createNode(ORM_ValueType.class);
        ORM_ValueType target = clientModel.createNode(ORM_ValueType.class);

        boolean weakConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_SubtypingConstraintAssociation.class, ValidationLevel.Weak);
        boolean strongConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_SubtypingConstraintAssociation.class, ValidationLevel.Strong);

        assertFalse(weakConnectivityEvaluation);
        assertFalse(strongConnectivityEvaluation);

        MyAsserts.assertStreamEquals(Stream.of(source, target).sorted(), clientModel.getElements(DiagramElement.class).sorted());
        MyAsserts.assertStreamEquals(Stream.of(source, target).sorted(), clientModel.getElements(CommitStatus.Uncommitted, DiagramElement.class).sorted());

        assertThrows(ClassCastException.class, () -> clientModel.connectBy(source, target, ORM_SubtypingConstraintAssociation.class));
    }

    // Повторное соединение той же пары узлов
    @Test
    void connectNodesTwice() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_Subtyping target = clientModel.connectBy(male, person, ORM_Subtyping.class);

        ORM_ExclusionSubtypingConstraint source = clientModel.createNode(ORM_ExclusionSubtypingConstraint.class);
        ORM_SubtypingConstraintAssociation edge = clientModel.connectBy(source, target, ORM_SubtypingConstraintAssociation.class);

        boolean weakConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_SubtypingConstraintAssociation.class, ValidationLevel.Weak);
        boolean strongConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_SubtypingConstraintAssociation.class, ValidationLevel.Strong);

        assertTrue(weakConnectivityEvaluation);
        assertFalse(strongConnectivityEvaluation);

        MyAsserts.assertStreamEquals(Stream.of(source, target, edge, person, male).sorted(), clientModel.getElements(DiagramElement.class).sorted());
        MyAsserts.assertStreamEquals(Stream.of(source, target, edge, person, male).sorted(), clientModel.getElements(CommitStatus.Uncommitted, DiagramElement.class).sorted());

        ORM_SubtypingConstraintAssociation duplicateEdge = clientModel.connectBy(source, target, ORM_SubtypingConstraintAssociation.class);
        clientModel.commit();

        assertFalse(duplicateEdge.isDestroy());
        assertEquals(CommitStatus.Uncommitted, duplicateEdge.getCommitStatus());
        assertEquals(UpdateStatus.Created, duplicateEdge.getUpdateStatus());
        assertEquals(ValidateStatus.Invalid, duplicateEdge.getValidateStatus());
        assertSame(source, duplicateEdge.getSource());
    }

    @Test
    void createDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_EntityType female = clientModel.createNode(ORM_EntityType.class);
        male.setName("Female");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);
        ORM_Subtyping femaleIsPerson = clientModel.connectBy(female, person, ORM_Subtyping.class);

        ORM_ExclusionSubtypingConstraint exclusionConstraint = clientModel.createNode(ORM_ExclusionSubtypingConstraint.class);
        ORM_SubtypingConstraintAssociation constraintAssociationMaleIsPerson = clientModel.connectBy(exclusionConstraint, maleIsPerson, ORM_SubtypingConstraintAssociation.class);
        ORM_SubtypingConstraintAssociation constraintAssociationFemaleIsPerson = clientModel.connectBy(exclusionConstraint, femaleIsPerson, ORM_SubtypingConstraintAssociation.class);

        clientModel.commit();

        ValidateStatus validateStatus = clientModel.validate();

        assertEquals(ValidateStatus.Acceptable, validateStatus);
        assertEquals(ValidateStatus.Acceptable, constraintAssociationMaleIsPerson.getValidateStatus());
        assertEquals(ValidateStatus.Acceptable, constraintAssociationFemaleIsPerson.getValidateStatus());
        assertEquals(ValidateStatus.Acceptable, exclusionConstraint.getValidateStatus());
    }

    @Test
    void createInvalidDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);

        ORM_ExclusionSubtypingConstraint exclusionConstraint = clientModel.createNode(ORM_ExclusionSubtypingConstraint.class);
        ORM_SubtypingConstraintAssociation constraintAssociation = clientModel.connectBy(exclusionConstraint, maleIsPerson, ORM_SubtypingConstraintAssociation.class);

        clientModel.commit();

        ValidateStatus validateStatus = clientModel.validate();

        assertEquals(ValidateStatus.Intermediate, validateStatus);
        assertEquals(ValidateStatus.Intermediate, exclusionConstraint.getValidateStatus());
        assertEquals(ValidateStatus.Acceptable, constraintAssociation.getValidateStatus());
    }

    @Test
    void deleteConstraintAssociationInDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_EntityType female = clientModel.createNode(ORM_EntityType.class);
        male.setName("Female");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);
        ORM_Subtyping femaleIsPerson = clientModel.connectBy(female, person, ORM_Subtyping.class);

        ORM_ExclusionSubtypingConstraint exclusionConstraint = clientModel.createNode(ORM_ExclusionSubtypingConstraint.class);
        ORM_SubtypingConstraintAssociation constraintAssociationMaleIsPerson = clientModel.connectBy(exclusionConstraint, maleIsPerson, ORM_SubtypingConstraintAssociation.class);
        ORM_SubtypingConstraintAssociation constraintAssociationFemaleIsPerson = clientModel.connectBy(exclusionConstraint, femaleIsPerson, ORM_SubtypingConstraintAssociation.class);

        clientModel.commit();

        clientModel.beginUpdate();
        List<DiagramElement> deletedElements = clientModel.removeElement(constraintAssociationFemaleIsPerson).collect(Collectors.toList());
        clientModel.commit();

        assertEquals(List.of(constraintAssociationFemaleIsPerson), deletedElements);
        MyAsserts.assertStreamEquals(Stream.of(person, male, female, maleIsPerson, femaleIsPerson, exclusionConstraint, constraintAssociationMaleIsPerson).sorted(), clientModel.getElements(DiagramElement.class).sorted());

    }

    @Test
    void deleteSubtypingConstraintInDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_EntityType female = clientModel.createNode(ORM_EntityType.class);
        male.setName("Female");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);
        ORM_Subtyping femaleIsPerson = clientModel.connectBy(female, person, ORM_Subtyping.class);

        ORM_ExclusionSubtypingConstraint exclusionConstraint = clientModel.createNode(ORM_ExclusionSubtypingConstraint.class);
        ORM_SubtypingConstraintAssociation constraintAssociationMaleIsPerson = clientModel.connectBy(exclusionConstraint, maleIsPerson, ORM_SubtypingConstraintAssociation.class);
        ORM_SubtypingConstraintAssociation constraintAssociationFemaleIsPerson = clientModel.connectBy(exclusionConstraint, femaleIsPerson, ORM_SubtypingConstraintAssociation.class);

        clientModel.commit();

        clientModel.beginUpdate();
        List<DiagramElement> deletedElements = clientModel.removeElement(exclusionConstraint).collect(Collectors.toList());
        clientModel.commit();

        Collections.sort(deletedElements);

        List<DiagramElement> expectedDeletedElements = Arrays.asList(exclusionConstraint, constraintAssociationMaleIsPerson, constraintAssociationFemaleIsPerson);
        Collections.sort(expectedDeletedElements);

        assertEquals(expectedDeletedElements, deletedElements);
        MyAsserts.assertStreamEquals(Stream.of(person, male, female, maleIsPerson, femaleIsPerson).sorted(), clientModel.getElements(DiagramElement.class).sorted());
    }

    @Test
    void deleteElementsInDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_EntityType female = clientModel.createNode(ORM_EntityType.class);
        male.setName("Female");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);
        ORM_Subtyping femaleIsPerson = clientModel.connectBy(female, person, ORM_Subtyping.class);

        ORM_ExclusionSubtypingConstraint exclusionConstraint = clientModel.createNode(ORM_ExclusionSubtypingConstraint.class);
        ORM_SubtypingConstraintAssociation constraintAssociationMaleIsPerson = clientModel.connectBy(exclusionConstraint, maleIsPerson, ORM_SubtypingConstraintAssociation.class);
        ORM_SubtypingConstraintAssociation constraintAssociationFemaleIsPerson = clientModel.connectBy(exclusionConstraint, femaleIsPerson, ORM_SubtypingConstraintAssociation.class);

        clientModel.commit();

        clientModel.beginUpdate();
        List<DiagramElement> deletedElements = clientModel.removeElement(femaleIsPerson).collect(Collectors.toList());
        clientModel.commit();

        Collections.sort(deletedElements);

        List<DiagramElement> expectedDeletedElements = Arrays.asList(femaleIsPerson, constraintAssociationFemaleIsPerson);
        Collections.sort(expectedDeletedElements);

        assertEquals(expectedDeletedElements, deletedElements);
        MyAsserts.assertStreamEquals(Stream.of(person, maleIsPerson, male, female, constraintAssociationMaleIsPerson, exclusionConstraint).sorted(), clientModel.getElements(DiagramElement.class).sorted());
    }

    @Test
    void rollbackCreatedElementsInDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_EntityType female = clientModel.createNode(ORM_EntityType.class);
        male.setName("Female");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);
        ORM_Subtyping femaleIsPerson = clientModel.connectBy(female, person, ORM_Subtyping.class);

        ORM_ExclusionSubtypingConstraint exclusionConstraint = clientModel.createNode(ORM_ExclusionSubtypingConstraint.class);
        ORM_SubtypingConstraintAssociation constraintAssociationMaleIsPerson = clientModel.connectBy(exclusionConstraint, maleIsPerson, ORM_SubtypingConstraintAssociation.class);
        clientModel.commit();

        clientModel.beginUpdate();
        ORM_SubtypingConstraintAssociation constraintAssociationFemaleIsPerson = clientModel.connectBy(exclusionConstraint, femaleIsPerson, ORM_SubtypingConstraintAssociation.class);
        clientModel.rollback();

        MyAsserts.assertStreamEquals(Stream.of(person, male, female, maleIsPerson, femaleIsPerson, exclusionConstraint, constraintAssociationMaleIsPerson).sorted(),
                clientModel.getElements(DiagramElement.class).sorted());
    }

    @Test
    void rollbackDeleteElementsInDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_EntityType female = clientModel.createNode(ORM_EntityType.class);
        male.setName("Female");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);
        ORM_Subtyping femaleIsPerson = clientModel.connectBy(female, person, ORM_Subtyping.class);

        ORM_ExclusionSubtypingConstraint exclusionConstraint = clientModel.createNode(ORM_ExclusionSubtypingConstraint.class);
        ORM_SubtypingConstraintAssociation constraintAssociationMaleIsPerson = clientModel.connectBy(exclusionConstraint, maleIsPerson, ORM_SubtypingConstraintAssociation.class);
        ORM_SubtypingConstraintAssociation constraintAssociationFemaleIsPerson = clientModel.connectBy(exclusionConstraint, femaleIsPerson, ORM_SubtypingConstraintAssociation.class);

        clientModel.commit();

        clientModel.beginUpdate();
        List<DiagramElement> deletedElements = clientModel.removeElement(constraintAssociationFemaleIsPerson).collect(Collectors.toList());
        clientModel.rollback();

        MyAsserts.assertStreamEquals(Stream.of(person, male, female, maleIsPerson, femaleIsPerson, exclusionConstraint, constraintAssociationMaleIsPerson, constraintAssociationFemaleIsPerson).sorted(), clientModel.getElements(DiagramElement.class).sorted());
    }
}
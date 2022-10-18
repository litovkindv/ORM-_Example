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

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Litovkin Dmitry
 */
class ORM_SubtypingTest {

    // Соединение дуги с разными Entity Type
    @Test
    void connectAcceptableNodes() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_EntityType source = clientModel.createNode(ORM_EntityType.class);
        source.setName("A");
        ORM_EntityType target = clientModel.createNode(ORM_EntityType.class);
        target.setName("B");

        boolean weakConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_Subtyping.class, ValidationLevel.Weak);
        boolean strongConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_Subtyping.class, ValidationLevel.Strong);

        assertTrue(weakConnectivityEvaluation);
        assertTrue(strongConnectivityEvaluation);

        MyAsserts.assertStreamEquals(Stream.of(source, target).sorted(), clientModel.getElements(DiagramElement.class).sorted());
        MyAsserts.assertStreamEquals(Stream.of(source, target).sorted(), clientModel.getElements(CommitStatus.Uncommitted, DiagramElement.class).sorted());

        ORM_Subtyping edge = clientModel.connectBy(source, target, ORM_Subtyping.class);
        clientModel.commit();

        assertFalse(edge.isDestroy());
        assertEquals(CommitStatus.Committed, edge.getCommitStatus());
        assertEquals(UpdateStatus.Stable, edge.getUpdateStatus());
        assertEquals(ValidateStatus.Acceptable, edge.getValidateStatus());
        assertSame(source, edge.getSource());
        assertSame(target, edge.getTarget());
    }

    // Попытка соединить дугой недопустимый узел-источник
    @Test
    void tryConnectUnacceptableSource() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_ValueType source = clientModel.createNode(ORM_ValueType.class);
        ORM_EntityType target = clientModel.createNode(ORM_EntityType.class);

        boolean weakConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_Subtyping.class, ValidationLevel.Weak);
        boolean strongConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_Subtyping.class, ValidationLevel.Strong);

        assertFalse(weakConnectivityEvaluation);
        assertFalse(strongConnectivityEvaluation);

        MyAsserts.assertStreamEquals(Stream.of(source, target).sorted(), clientModel.getElements(DiagramElement.class).sorted());
        MyAsserts.assertStreamEquals(Stream.of(source, target).sorted(), clientModel.getElements(CommitStatus.Uncommitted, DiagramElement.class).sorted());

        assertThrows(ClassCastException.class, () -> clientModel.connectBy(source, target, ORM_Subtyping.class));
    }

    // Попытка соединить дугой недопустимый целевой узел
    @Test
    void tryConnectUnacceptableTarget() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_EntityType source = clientModel.createNode(ORM_EntityType.class);
        ORM_ValueType target = clientModel.createNode(ORM_ValueType.class);

        boolean weakConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_Subtyping.class, ValidationLevel.Weak);
        boolean strongConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_Subtyping.class, ValidationLevel.Strong);

        assertFalse(weakConnectivityEvaluation);
        assertFalse(strongConnectivityEvaluation);

        MyAsserts.assertStreamEquals(Stream.of(source, target).sorted(), clientModel.getElements(DiagramElement.class).sorted());
        MyAsserts.assertStreamEquals(Stream.of(source, target).sorted(), clientModel.getElements(CommitStatus.Uncommitted, DiagramElement.class).sorted());

        assertThrows(ClassCastException.class, () -> clientModel.connectBy(source, target, ORM_Subtyping.class));
    }

    // Попытка соединить дугой недопустимые узлы
    @Test
    void tryConnectUnacceptableNodes() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_ValueType source = clientModel.createNode(ORM_ValueType.class);
        ORM_ValueType target = clientModel.createNode(ORM_ValueType.class);

        boolean weakConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_Subtyping.class, ValidationLevel.Weak);
        boolean strongConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_Subtyping.class, ValidationLevel.Strong);

        assertFalse(weakConnectivityEvaluation);
        assertFalse(strongConnectivityEvaluation);

        MyAsserts.assertStreamEquals(Stream.of(source, target).sorted(), clientModel.getElements(DiagramElement.class).sorted());
        MyAsserts.assertStreamEquals(Stream.of(source, target).sorted(), clientModel.getElements(CommitStatus.Uncommitted, DiagramElement.class).sorted());

        assertThrows(ClassCastException.class, () -> clientModel.connectBy(source, target, ORM_Subtyping.class));
    }

    // Создание петли
    @Test
    void connectNodeByLoop() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_EntityType node = clientModel.createNode(ORM_EntityType.class);

        boolean weakConnectivityEvaluation = clientModel.canConnectBy(node, node, ORM_Subtyping.class, ValidationLevel.Weak);
        boolean strongConnectivityEvaluation = clientModel.canConnectBy(node, node, ORM_Subtyping.class, ValidationLevel.Strong);

        assertTrue(weakConnectivityEvaluation);
        assertFalse(strongConnectivityEvaluation);

        MyAsserts.assertStreamEquals(Stream.of(node), clientModel.getElements(DiagramElement.class));

        ORM_Subtyping edge = clientModel.connectBy(node, node, ORM_Subtyping.class);
        clientModel.commit();

        assertFalse(edge.isDestroy());
        assertEquals(CommitStatus.Uncommitted, edge.getCommitStatus());
        assertEquals(UpdateStatus.Created, edge.getUpdateStatus());
        assertEquals(ValidateStatus.Invalid, edge.getValidateStatus());
        assertSame(node, edge.getSource());
        assertSame(node, edge.getTarget());
    }

    // Повторное соединение той же пары узлов
    @Test
    void connectNodesTwice() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_EntityType source = clientModel.createNode(ORM_EntityType.class);
        source.setName("A");
        ORM_EntityType target = clientModel.createNode(ORM_EntityType.class);
        target.setName("B");

        ORM_Subtyping edge = clientModel.connectBy(source, target, ORM_Subtyping.class);

        boolean weakConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_Subtyping.class, ValidationLevel.Weak);
        boolean strongConnectivityEvaluation = clientModel.canConnectBy(source, target, ORM_Subtyping.class, ValidationLevel.Strong);

        assertTrue(weakConnectivityEvaluation);
        assertFalse(strongConnectivityEvaluation);

        MyAsserts.assertStreamEquals(Stream.of(source, target, edge).sorted(), clientModel.getElements(DiagramElement.class).sorted());
        MyAsserts.assertStreamEquals(Stream.of(source, target, edge).sorted(), clientModel.getElements(CommitStatus.Uncommitted, DiagramElement.class).sorted());

        ORM_Subtyping duplicateEdge = clientModel.connectBy(source, target, ORM_Subtyping.class);
        clientModel.commit();

        assertFalse(duplicateEdge.isDestroy());
        assertEquals(CommitStatus.Uncommitted, duplicateEdge.getCommitStatus());
        assertEquals(UpdateStatus.Created, duplicateEdge.getUpdateStatus());
        assertEquals(ValidateStatus.Invalid, duplicateEdge.getValidateStatus());
        assertSame(source, duplicateEdge.getSource());
        assertSame(target, duplicateEdge.getTarget());
    }

    // Повторное соединение той же пары узлов, но в противоположном направлении
    @Test
    void forwardAndInverseConnections() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_EntityType source = clientModel.createNode(ORM_EntityType.class);
        source.setName("A");
        ORM_EntityType target = clientModel.createNode(ORM_EntityType.class);
        target.setName("B");

        ORM_Subtyping edge = clientModel.connectBy(source, target, ORM_Subtyping.class);

        boolean weakConnectivityEvaluation = clientModel.canConnectBy(target, source, ORM_Subtyping.class, ValidationLevel.Weak);
        boolean strongConnectivityEvaluation = clientModel.canConnectBy(target, source, ORM_Subtyping.class, ValidationLevel.Strong);

        assertTrue(weakConnectivityEvaluation);
        assertFalse(strongConnectivityEvaluation);

        MyAsserts.assertStreamEquals(Stream.of(source, target, edge).sorted(), clientModel.getElements(DiagramElement.class).sorted());
        MyAsserts.assertStreamEquals(Stream.of(source, target, edge).sorted(), clientModel.getElements(CommitStatus.Uncommitted, DiagramElement.class).sorted());

        ORM_Subtyping reverseEdge = clientModel.connectBy(target, source, ORM_Subtyping.class);
        clientModel.commit();

        assertFalse(reverseEdge.isDestroy());
        assertEquals(CommitStatus.Uncommitted, reverseEdge.getCommitStatus());
        assertEquals(UpdateStatus.Created, reverseEdge.getUpdateStatus());
        assertEquals(ValidateStatus.Invalid, reverseEdge.getValidateStatus());
        assertSame(target, reverseEdge.getSource());
        assertSame(source, reverseEdge.getTarget());
    }

    @Test
    void moreThanTwoSubtyping() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        ORM_EntityType person_EntityType = clientModel.createNode(ORM_EntityType.class);
        person_EntityType.setName("Person");
        ORM_EntityType female_EntityType = clientModel.createNode(ORM_EntityType.class);
        female_EntityType.setName("Female");
        ORM_Subtyping subtyping_Person_Female = clientModel.connectBy(female_EntityType, person_EntityType, ORM_Subtyping.class);
        ORM_EntityType male_EntityType = clientModel.createNode(ORM_EntityType.class);
        male_EntityType.setName("Male");
        ORM_Subtyping subtyping_Person_Male = clientModel.connectBy(male_EntityType, person_EntityType, ORM_Subtyping.class);
        ORM_EntityType mother_EntityType = clientModel.createNode(ORM_EntityType.class);
        mother_EntityType.setName("Mother");
        ORM_Subtyping subtyping_Female_Mother = clientModel.connectBy(mother_EntityType, female_EntityType, ORM_Subtyping.class);

        clientModel.commit();
    }
//
//    private class ElementsPresenter implements ClientDiagramModelListener {
//
//        @Override
//        public void isUpdated(ModelUpdateEvent e) {
//            if (e.getSuccessfulUpdateStatus() == ModelUpdateEvent.SuccessfulUpdateStatus.SuccessCommit &&
//                    e.getUpdateInitiator() == ModelUpdateEvent.UpdateInitiator.AnotherOne) {
//
//                ClientDiagramModel clientModel = (ClientDiagramModel) e.getSource();
//
//                List<DiagramElement> notPresentedElements =
//                        clientModel.getElements(CommitStatus.NotPresented, DiagramElement.class).collect(Collectors.toList());
//                notPresentedElements.forEach(clientModel::markElementAsPresented);
//            }
//        }
//    }
}
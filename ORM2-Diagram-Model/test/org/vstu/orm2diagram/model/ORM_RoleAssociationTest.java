package org.vstu.orm2diagram.model;

import org.junit.jupiter.api.Test;
import org.vstu.nodelinkdiagram.*;
import org.vstu.nodelinkdiagram.statuses.CommitStatus;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;
import org.vstu.nodelinkdiagram.util.Point;
import org.vstu.orm2diagram.model.finalized_entities.Test_DiagramClient;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ORM_RoleAssociationTest {

    private class ElementsPresenter implements ClientDiagramModelListener {

        @Override
        public void isUpdated(ModelUpdateEvent e) {
            if (e.getSuccessfulUpdateStatus() == ModelUpdateEvent.SuccessfulUpdateStatus.SuccessCommit &&
                    e.getUpdateInitiator() == ModelUpdateEvent.UpdateInitiator.AnotherOne) {

                ClientDiagramModel clientModel = (ClientDiagramModel) e.getSource();

                // ВНИМАНИЕ!!!! Предварительно модель НУЖНО проверить на валидность
                clientModel.validate();

                List<DiagramElement> notPresentedElements =
                        clientModel.getElements(CommitStatus.NotPresented, DiagramElement.class).collect(Collectors.toList());
                notPresentedElements.forEach(clientModel::markElementAsPresented);
            }
        }
    }

    @Test
    void createDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        anotherClientModel.addListener(new ElementsPresenter());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);

        ORM_ValueType name = clientModel.createNode(ORM_ValueType.class);
        name.setName("Name");

        ORM_BinaryPredicate has_identify = clientModel.createNode(ORM_BinaryPredicate.class);
        has_identify.getItem(0).setName("has");
        has_identify.getItem(1).setName("identify");

        ORM_RoleAssociation personHas = clientModel.connectBy(person, has_identify.getItem(0), ORM_RoleAssociation.class);
        ORM_RoleAssociation nameIdentify = clientModel.connectBy(name, has_identify.getItem(1), ORM_RoleAssociation.class);

        ORM_UnaryPredicate isSmoke = clientModel.createNode(ORM_UnaryPredicate.class);
        isSmoke.getItem(0).setName("is smoke");

        ORM_RoleAssociation personSmoke = clientModel.connectBy(person, isSmoke.getItem(0), ORM_RoleAssociation.class);

        // { Начинается самое замороченное !!!
        ORM_RoleSequence isSmokeSequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        isSmokeSequence.addItem(isSmoke.getItem(0));

        ORM_RoleSequence identifyHasSequence = clientModel.createNode(ORM_SequenceFromTwoRoles.class);
        identifyHasSequence.addItem(has_identify.getItem(1));
        identifyHasSequence.addItem(has_identify.getItem(0));

        Test_EdgeForConnectRoleSequences edge = clientModel.connectBy(isSmokeSequence, identifyHasSequence, Test_EdgeForConnectRoleSequences.class);
        // !!!}

        clientModel.commit();

        List<DiagramElement> elementsInMain = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClient = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClient = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());


        int a = 0;
    }

    @Test
    void deleteElementsInDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        anotherClientModel.addListener(new ElementsPresenter());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);

        ORM_ValueType name = clientModel.createNode(ORM_ValueType.class);
        name.setName("Name");

        ORM_BinaryPredicate has_identify = clientModel.createNode(ORM_BinaryPredicate.class);
        has_identify.getItem(0).setName("has");
        has_identify.getItem(1).setName("identify");

        ORM_RoleAssociation personHas = clientModel.connectBy(person, has_identify.getItem(0), ORM_RoleAssociation.class);
        ORM_RoleAssociation nameIdentify = clientModel.connectBy(name, has_identify.getItem(1), ORM_RoleAssociation.class);

        ORM_UnaryPredicate isSmoke = clientModel.createNode(ORM_UnaryPredicate.class);
        isSmoke.getItem(0).setName("is smoke");

        ORM_RoleAssociation personSmoke = clientModel.connectBy(person, isSmoke.getItem(0), ORM_RoleAssociation.class);

        // { Начинается самое замороченное !!!
        ORM_RoleSequence isSmokeSequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        isSmokeSequence.addItem(isSmoke.getItem(0));

        ORM_RoleSequence identifyHasSequence = clientModel.createNode(ORM_SequenceFromTwoRoles.class);
        identifyHasSequence.addItem(has_identify.getItem(1));
        identifyHasSequence.addItem(has_identify.getItem(0));

        Test_EdgeForConnectRoleSequences edge = clientModel.connectBy(isSmokeSequence, identifyHasSequence, Test_EdgeForConnectRoleSequences.class);
        // !!!}

        clientModel.commit();

        List<DiagramElement> elementsInMain = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClient = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClient = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());


        // Первое удаление

        clientModel.beginUpdate();
        List<DiagramElement> deletedElementsFirst = clientModel.removeElement(person).collect(Collectors.toList());
        clientModel.commit();

        List<DiagramElement> elementsInMainAfterFirstDelete = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClientAfterFirstDelete = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClientAfterFirstDelete = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());

        // Второе удаление

        clientModel.beginUpdate();
        List<DiagramElement> deletedElementsSecond = clientModel.removeElement(has_identify).collect(Collectors.toList());
        clientModel.commit();

        List<DiagramElement> elementsInMainAfterSecondDelete = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClientAfterSecondDelete = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClientAfterSecondDelete = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());

        int a = 0;
    }

    @Test
    void modifyElementsInDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        anotherClientModel.addListener(new ElementsPresenter());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);

        ORM_ValueType name = clientModel.createNode(ORM_ValueType.class);
        name.setName("Name");

        ORM_BinaryPredicate has_identify = clientModel.createNode(ORM_BinaryPredicate.class);
        has_identify.getItem(0).setName("has");
        has_identify.getItem(1).setName("identify");

        ORM_RoleAssociation personHas = clientModel.connectBy(person, has_identify.getItem(0), ORM_RoleAssociation.class);
        ORM_RoleAssociation nameIdentify = clientModel.connectBy(name, has_identify.getItem(1), ORM_RoleAssociation.class);

        ORM_UnaryPredicate isSmoke = clientModel.createNode(ORM_UnaryPredicate.class);
        isSmoke.getItem(0).setName("is smoke");

        ORM_RoleAssociation personSmoke = clientModel.connectBy(person, isSmoke.getItem(0), ORM_RoleAssociation.class);

        // { Начинается самое замороченное !!!
        ORM_RoleSequence isSmokeSequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        isSmokeSequence.addItem(isSmoke.getItem(0));

        ORM_RoleSequence identifyHasSequence = clientModel.createNode(ORM_SequenceFromTwoRoles.class);
        identifyHasSequence.addItem(has_identify.getItem(1));
        identifyHasSequence.addItem(has_identify.getItem(0));

        Test_EdgeForConnectRoleSequences edge = clientModel.connectBy(isSmokeSequence, identifyHasSequence, Test_EdgeForConnectRoleSequences.class);
        // !!!}

        clientModel.commit();

        List<DiagramElement> elementsInMain = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClient = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClient = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());

        // Модификация

        clientModel.beginUpdate();
        male.setName("Female");
        isSmoke.getItem(0).setName("smoke");
        has_identify.setPosition(new Point(1, 1));
        clientModel.commit();

        List<DiagramElement> elementsInMainAfterModify = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClientAfterModify = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClientAfterModify = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());


        int a = 0;
    }

    @Test
    void stepByStepUpdateDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        anotherClientModel.addListener(new ElementsPresenter());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);

        ORM_ValueType name = clientModel.createNode(ORM_ValueType.class);
        name.setName("Name");

        ORM_BinaryPredicate has_identify = clientModel.createNode(ORM_BinaryPredicate.class);
        has_identify.getItem(0).setName("has");
        has_identify.getItem(1).setName("identify");

        ORM_RoleAssociation personHas = clientModel.connectBy(person, has_identify.getItem(0), ORM_RoleAssociation.class);
        ORM_RoleAssociation nameIdentify = clientModel.connectBy(name, has_identify.getItem(1), ORM_RoleAssociation.class);

        ORM_UnaryPredicate isSmoke = clientModel.createNode(ORM_UnaryPredicate.class);
        isSmoke.getItem(0).setName("is smoke");

        ORM_RoleAssociation personSmoke = clientModel.connectBy(person, isSmoke.getItem(0), ORM_RoleAssociation.class);

        // { Начинается самое замороченное !!!
        ORM_RoleSequence isSmokeSequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        isSmokeSequence.addItem(isSmoke.getItem(0));

        ORM_RoleSequence identifyHasSequence = clientModel.createNode(ORM_SequenceFromTwoRoles.class);
        identifyHasSequence.addItem(has_identify.getItem(1));
        identifyHasSequence.addItem(has_identify.getItem(0));

        Test_EdgeForConnectRoleSequences edge = clientModel.connectBy(isSmokeSequence, identifyHasSequence, Test_EdgeForConnectRoleSequences.class);
        // !!!}

        clientModel.commit();

        List<DiagramElement> elementsInMain = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClient = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClient = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());

        // МОДИФИКАЦИЯ:
        // 1) Удаляем унарную роль (дополнительно удаляются: а) последовательность из одной роли, б) дуга между последовательностью ролей )
        // 2) Создаем ДВЕ последовательности из одной роли. Берем разные роли бинарного предиката
        // 3) Создаем дугу между созданными последовательностями ролей

        clientModel.beginUpdate();

        List<DiagramElement> deletedElementsFirst = clientModel.removeElement(isSmoke).collect(Collectors.toList());

        ORM_SequenceFromOneRole hasSequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        hasSequence.addItem(has_identify.getItem(0));

        ORM_SequenceFromOneRole identifySequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        identifySequence.addItem(has_identify.getItem(1));

        Test_EdgeForConnectRoleSequences edgeEdge = clientModel.connectBy(hasSequence, identifySequence, Test_EdgeForConnectRoleSequences.class);

        clientModel.commit();

        List<DiagramElement> elementsInMainAfterUpdate = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClientAfterUpdate = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClientAfterUpdate = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());

        int a = 0;
    }

    @Test
    void oneTimeUpdateDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        anotherClientModel.addListener(new ElementsPresenter());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);

        ORM_ValueType name = clientModel.createNode(ORM_ValueType.class);
        name.setName("Name");

        ORM_BinaryPredicate has_identify = clientModel.createNode(ORM_BinaryPredicate.class);
        has_identify.getItem(0).setName("has");
        has_identify.getItem(1).setName("identify");

        ORM_RoleAssociation personHas = clientModel.connectBy(person, has_identify.getItem(0), ORM_RoleAssociation.class);
        ORM_RoleAssociation nameIdentify = clientModel.connectBy(name, has_identify.getItem(1), ORM_RoleAssociation.class);

        ORM_UnaryPredicate isSmoke = clientModel.createNode(ORM_UnaryPredicate.class);
        isSmoke.getItem(0).setName("is smoke");

        ORM_RoleAssociation personSmoke = clientModel.connectBy(person, isSmoke.getItem(0), ORM_RoleAssociation.class);

        // { Начинается самое замороченное !!!
        ORM_RoleSequence isSmokeSequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        isSmokeSequence.addItem(isSmoke.getItem(0));

        ORM_RoleSequence identifyHasSequence = clientModel.createNode(ORM_SequenceFromTwoRoles.class);
        identifyHasSequence.addItem(has_identify.getItem(1));
        identifyHasSequence.addItem(has_identify.getItem(0));

        Test_EdgeForConnectRoleSequences edge = clientModel.connectBy(isSmokeSequence, identifyHasSequence, Test_EdgeForConnectRoleSequences.class);
        // !!!}


        // МОДИФИКАЦИЯ:
        // 1) Удаляем унарную роль (дополнительно удаляются: а) последовательность из одной роли, б) дуга между последовательностью ролей )
        // 2) Создаем ДВЕ последовательности из одной роли. Берем разные роли бинарного предиката
        // 3) Создаем дугу между созданными последовательностями ролей

        List<DiagramElement> deletedElementsFirst = clientModel.removeElement(isSmoke).collect(Collectors.toList());

        ORM_SequenceFromOneRole hasSequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        hasSequence.addItem(has_identify.getItem(0));

        ORM_SequenceFromOneRole identifySequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        identifySequence.addItem(has_identify.getItem(1));

        Test_EdgeForConnectRoleSequences edgeEdge = clientModel.connectBy(hasSequence, identifySequence, Test_EdgeForConnectRoleSequences.class);

        clientModel.commit();

        List<DiagramElement> elementsInMainAfterUpdate = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClientAfterUpdate = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClientAfterUpdate = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());

        int a = 0;
    }

    @Test
    void rollbackCreatedElementsInDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        anotherClientModel.addListener(new ElementsPresenter());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);

        ORM_ValueType name = clientModel.createNode(ORM_ValueType.class);
        name.setName("Name");

        ORM_BinaryPredicate has_identify = clientModel.createNode(ORM_BinaryPredicate.class);
        has_identify.getItem(0).setName("has");
        has_identify.getItem(1).setName("identify");

        ORM_RoleAssociation personHas = clientModel.connectBy(person, has_identify.getItem(0), ORM_RoleAssociation.class);
        ORM_RoleAssociation nameIdentify = clientModel.connectBy(name, has_identify.getItem(1), ORM_RoleAssociation.class);

        ORM_UnaryPredicate isSmoke = clientModel.createNode(ORM_UnaryPredicate.class);
        isSmoke.getItem(0).setName("is smoke");

        ORM_RoleAssociation personSmoke = clientModel.connectBy(person, isSmoke.getItem(0), ORM_RoleAssociation.class);

        // { Начинается самое замороченное !!!
        ORM_RoleSequence isSmokeSequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        isSmokeSequence.addItem(isSmoke.getItem(0));

        ORM_RoleSequence identifyHasSequence = clientModel.createNode(ORM_SequenceFromTwoRoles.class);
        identifyHasSequence.addItem(has_identify.getItem(1));
        identifyHasSequence.addItem(has_identify.getItem(0));

        Test_EdgeForConnectRoleSequences edge = clientModel.connectBy(isSmokeSequence, identifyHasSequence, Test_EdgeForConnectRoleSequences.class);
        // !!!}

        clientModel.rollback();

        List<DiagramElement> elementsInMain = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClient = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClient = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());


        int a = 0;
    }

    @Test
    void rollbackDeleteElementsInDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        anotherClientModel.addListener(new ElementsPresenter());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);

        ORM_ValueType name = clientModel.createNode(ORM_ValueType.class);
        name.setName("Name");

        ORM_BinaryPredicate has_identify = clientModel.createNode(ORM_BinaryPredicate.class);
        has_identify.getItem(0).setName("has");
        has_identify.getItem(1).setName("identify");

        ORM_RoleAssociation personHas = clientModel.connectBy(person, has_identify.getItem(0), ORM_RoleAssociation.class);
        ORM_RoleAssociation nameIdentify = clientModel.connectBy(name, has_identify.getItem(1), ORM_RoleAssociation.class);

        ORM_UnaryPredicate isSmoke = clientModel.createNode(ORM_UnaryPredicate.class);
        isSmoke.getItem(0).setName("is smoke");

        ORM_RoleAssociation personSmoke = clientModel.connectBy(person, isSmoke.getItem(0), ORM_RoleAssociation.class);

        // { Начинается самое замороченное !!!
        ORM_RoleSequence isSmokeSequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        isSmokeSequence.addItem(isSmoke.getItem(0));

        ORM_RoleSequence identifyHasSequence = clientModel.createNode(ORM_SequenceFromTwoRoles.class);
        identifyHasSequence.addItem(has_identify.getItem(1));
        identifyHasSequence.addItem(has_identify.getItem(0));

        Test_EdgeForConnectRoleSequences edge = clientModel.connectBy(isSmokeSequence, identifyHasSequence, Test_EdgeForConnectRoleSequences.class);
        // !!!}

        clientModel.commit();

        List<DiagramElement> elementsInMain = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClient = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClient = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());


        // Первое удаление

        clientModel.beginUpdate();
        List<DiagramElement> deletedElementsFirst = clientModel.removeElement(person).collect(Collectors.toList());
        clientModel.rollback();

        List<DiagramElement> elementsInMainAfterFirstDelete = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClientAfterFirstDelete = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClientAfterFirstDelete = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());

        // Второе удаление

        clientModel.beginUpdate();
        List<DiagramElement> deletedElementsSecond = clientModel.removeElement(has_identify).collect(Collectors.toList());
        clientModel.rollback();

        List<DiagramElement> elementsInMainAfterSecondDelete = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClientAfterSecondDelete = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClientAfterSecondDelete = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());

        int a = 0;
    }

    @Test
    void rollbackModifiedElementsInDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        anotherClientModel.addListener(new ElementsPresenter());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);

        ORM_ValueType name = clientModel.createNode(ORM_ValueType.class);
        name.setName("Name");

        ORM_BinaryPredicate has_identify = clientModel.createNode(ORM_BinaryPredicate.class);
        has_identify.getItem(0).setName("has");
        has_identify.getItem(1).setName("identify");

        ORM_RoleAssociation personHas = clientModel.connectBy(person, has_identify.getItem(0), ORM_RoleAssociation.class);
        ORM_RoleAssociation nameIdentify = clientModel.connectBy(name, has_identify.getItem(1), ORM_RoleAssociation.class);

        ORM_UnaryPredicate isSmoke = clientModel.createNode(ORM_UnaryPredicate.class);
        isSmoke.getItem(0).setName("is smoke");

        ORM_RoleAssociation personSmoke = clientModel.connectBy(person, isSmoke.getItem(0), ORM_RoleAssociation.class);

        // { Начинается самое замороченное !!!
        ORM_RoleSequence isSmokeSequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        isSmokeSequence.addItem(isSmoke.getItem(0));

        ORM_RoleSequence identifyHasSequence = clientModel.createNode(ORM_SequenceFromTwoRoles.class);
        identifyHasSequence.addItem(has_identify.getItem(1));
        identifyHasSequence.addItem(has_identify.getItem(0));

        Test_EdgeForConnectRoleSequences edge = clientModel.connectBy(isSmokeSequence, identifyHasSequence, Test_EdgeForConnectRoleSequences.class);
        // !!!}

        clientModel.commit();

        List<DiagramElement> elementsInMain = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClient = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClient = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());

        // Модификация

        clientModel.beginUpdate();
        male.setName("Female");
        isSmoke.getItem(0).setName("smoke");
        has_identify.setPosition(new Point(1, 1));
        clientModel.rollback();

        List<DiagramElement> elementsInMainAfterModify = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClientAfterModify = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClientAfterModify = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());


        int a = 0;
    }

    @Test
    void rollbackUpdateDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        anotherClientModel.addListener(new ElementsPresenter());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);
        person.setName("Person");

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);
        male.setName("Male");

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);

        ORM_ValueType name = clientModel.createNode(ORM_ValueType.class);
        name.setName("Name");

        ORM_BinaryPredicate has_identify = clientModel.createNode(ORM_BinaryPredicate.class);
        has_identify.getItem(0).setName("has");
        has_identify.getItem(1).setName("identify");

        ORM_RoleAssociation personHas = clientModel.connectBy(person, has_identify.getItem(0), ORM_RoleAssociation.class);
        ORM_RoleAssociation nameIdentify = clientModel.connectBy(name, has_identify.getItem(1), ORM_RoleAssociation.class);

        ORM_UnaryPredicate isSmoke = clientModel.createNode(ORM_UnaryPredicate.class);
        isSmoke.getItem(0).setName("is smoke");

        ORM_RoleAssociation personSmoke = clientModel.connectBy(person, isSmoke.getItem(0), ORM_RoleAssociation.class);

        // { Начинается самое замороченное !!!
        ORM_RoleSequence isSmokeSequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        isSmokeSequence.addItem(isSmoke.getItem(0));

        ORM_RoleSequence identifyHasSequence = clientModel.createNode(ORM_SequenceFromTwoRoles.class);
        identifyHasSequence.addItem(has_identify.getItem(1));
        identifyHasSequence.addItem(has_identify.getItem(0));

        Test_EdgeForConnectRoleSequences edge = clientModel.connectBy(isSmokeSequence, identifyHasSequence, Test_EdgeForConnectRoleSequences.class);
        // !!!}

        clientModel.commit();

        List<DiagramElement> elementsInMain = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClient = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClient = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());

        // МОДИФИКАЦИЯ:
        // 1) Удаляем унарную роль (дополнительно удаляются: а) последовательность из одной роли, б) дуга между последовательностью ролей )
        // 2) Создаем ДВЕ последовательности из одной роли. Берем разные роли бинарного предиката
        // 3) Создаем дугу между созданными последовательностями ролей

        clientModel.beginUpdate();

        List<DiagramElement> deletedElementsFirst = clientModel.removeElement(isSmoke).collect(Collectors.toList());

        ORM_SequenceFromOneRole hasSequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        hasSequence.addItem(has_identify.getItem(0));

        ORM_SequenceFromOneRole identifySequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        identifySequence.addItem(has_identify.getItem(1));

        Test_EdgeForConnectRoleSequences edgeEdge = clientModel.connectBy(hasSequence, identifySequence, Test_EdgeForConnectRoleSequences.class);

        clientModel.rollback();

        List<DiagramElement> elementsInMainAfterUpdate = mainModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInClientAfterUpdate = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());
        List<DiagramElement> elementsInAnotherClientAfterUpdate = anotherClientModel.getElements(DiagramElement.class).collect(Collectors.toList());

        int a = 0;
    }

    @Test
    void createInvalidDiagram() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        anotherClientModel.addListener(new ElementsPresenter());

        clientModel.beginUpdate();

        ORM_EntityType person = clientModel.createNode(ORM_EntityType.class);

        ORM_EntityType male = clientModel.createNode(ORM_EntityType.class);

        ORM_Subtyping maleIsPerson = clientModel.connectBy(male, person, ORM_Subtyping.class);

        ORM_ValueType name = clientModel.createNode(ORM_ValueType.class);

        ORM_BinaryPredicate has_identify = clientModel.createNode(ORM_BinaryPredicate.class);

        ORM_RoleAssociation personHas = clientModel.connectBy(person, has_identify.getItem(0), ORM_RoleAssociation.class);
        ORM_RoleAssociation nameIdentify = clientModel.connectBy(name, has_identify.getItem(1), ORM_RoleAssociation.class);

        ORM_UnaryPredicate isSmoke = clientModel.createNode(ORM_UnaryPredicate.class);

        ORM_RoleAssociation personSmoke = clientModel.connectBy(person, isSmoke.getItem(0), ORM_RoleAssociation.class);

        // { Начинается самое замороченное !!!
        ORM_RoleSequence isSmokeSequence = clientModel.createNode(ORM_SequenceFromOneRole.class);
        isSmokeSequence.addItem(isSmoke.getItem(0));

        ORM_RoleSequence identifyHasSequence = clientModel.createNode(ORM_SequenceFromTwoRoles.class);
        identifyHasSequence.addItem(has_identify.getItem(1));
        identifyHasSequence.addItem(has_identify.getItem(0));

        Test_EdgeForConnectRoleSequences edge = clientModel.connectBy(isSmokeSequence, identifyHasSequence, Test_EdgeForConnectRoleSequences.class);
        // !!!}

        clientModel.validate();

        assertEquals(ValidateStatus.Invalid, clientModel.getValidateStatus());

        List<DiagramElement> invalidElements = clientModel.getElements(DiagramElement.class)
                .filter(e -> e.getValidateStatus() == ValidateStatus.Invalid).collect(Collectors.toList());

        List<DiagramElement> intermediateElements = clientModel.getElements(DiagramElement.class)
                .filter(e -> e.getValidateStatus() == ValidateStatus.Intermediate).collect(Collectors.toList());

        int a = 0;
    }
}
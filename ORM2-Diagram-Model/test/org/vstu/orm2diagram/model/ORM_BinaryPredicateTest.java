package org.vstu.orm2diagram.model;

import org.junit.jupiter.api.Test;
import org.vstu.nodelinkdiagram.*;
import org.vstu.nodelinkdiagram.statuses.CommitStatus;
import org.vstu.orm2diagram.model.finalized_entities.Test_DiagramClient;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Litovkin Dmitry
 */
class ORM_BinaryPredicateTest {

   // TODO: rename test methods

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
    void xxxxxxxxxx() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_BinaryPredicate binaryPredicate = clientModel.createNode(ORM_BinaryPredicate.class);
        binaryPredicate.getItem(0).setName("role A");
        binaryPredicate.getItem(1).setName("role B");

        assertEquals(0, mainModel.getElements(DiagramElement.class).count());
        assertEquals(1, clientModel.getElements(DiagramElement.class).count());

        long a = 0;
    }

    @Test
    void yyyyyyyy() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_BinaryPredicate binaryPredicate = clientModel.createNode(ORM_BinaryPredicate.class);
        binaryPredicate.getItem(0).setName("role A");
        binaryPredicate.getItem(1).setName("role B");
        clientModel.commit();

        ORM_BinaryPredicate binaryPredicateInMain = (ORM_BinaryPredicate) binaryPredicate.getCommittedState();

        assertEquals(1, mainModel.getElements(DiagramElement.class).count());
        assertEquals(1, clientModel.getElements(DiagramElement.class).count());

        int a = 0;
    }

    @Test
    void rrrrrr() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_BinaryPredicate binaryPredicate = clientModel.createNode(ORM_BinaryPredicate.class);
        binaryPredicate.getItem(0).setName("role A");
        binaryPredicate.getItem(1).setName("role B");
        clientModel.rollback();

        ORM_BinaryPredicate binaryPredicateInMain = (ORM_BinaryPredicate) binaryPredicate.getCommittedState();

        assertEquals(0, mainModel.getElements(DiagramElement.class).count());
        assertEquals(0, clientModel.getElements(DiagramElement.class).count());

        int a = 0;
    }

    @Test
    void wwwwww() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        //anotherClientModel.addListener(new ElementsPresenter() );

        clientModel.beginUpdate();
        ORM_BinaryPredicate binaryPredicate = clientModel.createNode(ORM_BinaryPredicate.class);
        binaryPredicate.getItem(0).setName("role A");
        binaryPredicate.getItem(1).setName("role B");
        clientModel.commit();

        ORM_BinaryPredicate binaryPredicateInMain = (ORM_BinaryPredicate) binaryPredicate.getCommittedState();
        ORM_BinaryPredicate binaryPredicateInAnotherClient = anotherClientModel.getElements(ORM_BinaryPredicate.class).findFirst().get();

        assertEquals(1, mainModel.getElements(DiagramElement.class).count());
        assertEquals(1, clientModel.getElements(DiagramElement.class).count());
        assertEquals(1, anotherClientModel.getElements(DiagramElement.class).count());

        int a = 0;
    }

    @Test
    void zzzzzzz() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        anotherClientModel.addListener(new ElementsPresenter());

        clientModel.beginUpdate();
        ORM_BinaryPredicate binaryPredicate = clientModel.createNode(ORM_BinaryPredicate.class);
        binaryPredicate.getItem(0).setName("role A");
        binaryPredicate.getItem(1).setName("role B");
        clientModel.commit();

        ORM_BinaryPredicate binaryPredicateInMain = (ORM_BinaryPredicate) binaryPredicate.getCommittedState();
        ORM_BinaryPredicate binaryPredicateInAnotherClient = anotherClientModel.getElements(ORM_BinaryPredicate.class).findFirst().get();

        assertEquals(1, mainModel.getElements(DiagramElement.class).count());
        assertEquals(1, clientModel.getElements(DiagramElement.class).count());
        assertEquals(1, anotherClientModel.getElements(DiagramElement.class).count());

        int a = 0;
    }


    @Test
    void CCCCCCCCCCCCCCCCCC() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        ElementsPresenter elementsPresenter = new ElementsPresenter();
        anotherClientModel.addListener(elementsPresenter);

        clientModel.beginUpdate();

        ORM_BinaryPredicate first_BinaryPredicate = clientModel.createNode(ORM_BinaryPredicate.class);
        first_BinaryPredicate.getItem(0).setName("first predicate: left role");
        first_BinaryPredicate.getItem(1).setName("first predicate: right role");

        ORM_BinaryPredicate second_BinaryPredicate = clientModel.createNode(ORM_BinaryPredicate.class);
        second_BinaryPredicate.getItem(0).setName("second predicate: left role");
        second_BinaryPredicate.getItem(1).setName("second predicate: right role");

        ORM_BinaryPredicate third_BinaryPredicate = clientModel.createNode(ORM_BinaryPredicate.class);
        third_BinaryPredicate.getItem(0).setName("third predicate: left role");
        third_BinaryPredicate.getItem(1).setName("third predicate: right role");

        clientModel.commit();

        ORM_BinaryPredicate first_BinaryPredicateInMain = (ORM_BinaryPredicate) first_BinaryPredicate.getCommittedState();
        ORM_BinaryPredicate second_BinaryPredicateInMain = (ORM_BinaryPredicate) second_BinaryPredicate.getCommittedState();
        ORM_BinaryPredicate third_BinaryPredicateInMain = (ORM_BinaryPredicate) third_BinaryPredicate.getCommittedState();

        List<ORM_BinaryPredicate> binaryPredicatesInAnotherClient =
                anotherClientModel.getElements(ORM_BinaryPredicate.class).collect(Collectors.toList());

        assertEquals(3, mainModel.getElements(DiagramElement.class).count());
        assertEquals(3, clientModel.getElements(DiagramElement.class).count());
        assertEquals(3, anotherClientModel.getElements(DiagramElement.class).count());

        //anotherClientModel.removeListener(elementsPresenter);

        clientModel.beginUpdate();

        ORM_BinaryPredicate forth_BinaryPredicate = clientModel.createNode(ORM_BinaryPredicate.class);
        forth_BinaryPredicate.getItem(0).setName("forth predicate: left role");
        forth_BinaryPredicate.getItem(1).setName("forth predicate: right role");

        List<DiagramElement> deletedElements = clientModel.removeElement(first_BinaryPredicate).collect(Collectors.toList());

        second_BinaryPredicate.getItem(0).setName("XXXXX");

        clientModel.commit();

        ORM_BinaryPredicate forth_BinaryPredicateInMain = (ORM_BinaryPredicate) forth_BinaryPredicate.getCommittedState();

        List<ORM_BinaryPredicate> binaryPredicatesInAnotherClient_NextCommit =
                anotherClientModel.getElements(ORM_BinaryPredicate.class).collect(Collectors.toList());

        int a = 0;
    }

}
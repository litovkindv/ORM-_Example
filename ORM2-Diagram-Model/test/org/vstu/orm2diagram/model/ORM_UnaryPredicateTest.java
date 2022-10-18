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
class ORM_UnaryPredicateTest {

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
        ORM_UnaryPredicate unaryPredicate = clientModel.createNode(ORM_UnaryPredicate.class);
        unaryPredicate.getItem(0).setName("role A");

        assertEquals(0, mainModel.getElements(DiagramElement.class).count());
        assertEquals(1, clientModel.getElements(DiagramElement.class).count());

        long a = 0;
    }

    @Test
    void yyyyyyyy() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_UnaryPredicate unaryPredicate = clientModel.createNode(ORM_UnaryPredicate.class);
        unaryPredicate.getItem(0).setName("role A");
        clientModel.commit();

        ORM_UnaryPredicate unaryPredicateInMain = (ORM_UnaryPredicate) unaryPredicate.getCommittedState();

        assertEquals(1, mainModel.getElements(DiagramElement.class).count());
        assertEquals(1, clientModel.getElements(DiagramElement.class).count());

        int a = 0;
    }

    @Test
    void wwwwww() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());
        ClientDiagramModel anotherClientModel = mainModel.registerClient(new Test_DiagramClient());

        //anotherClientModel.addListener(new ElementsPresenter() );

        clientModel.beginUpdate();
        ORM_UnaryPredicate unaryPredicate = clientModel.createNode(ORM_UnaryPredicate.class);
        unaryPredicate.getItem(0).setName("role A");
        clientModel.commit();

        ORM_UnaryPredicate unaryPredicateInMain = (ORM_UnaryPredicate) unaryPredicate.getCommittedState();
        ORM_UnaryPredicate unaryPredicateInAnotherClient = anotherClientModel.getElements(ORM_UnaryPredicate.class).findFirst().get();

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
        ORM_UnaryPredicate unaryPredicate = clientModel.createNode(ORM_UnaryPredicate.class);
        unaryPredicate.getItem(0).setName("role A");
        clientModel.commit();

        ORM_UnaryPredicate unaryPredicateInMain = (ORM_UnaryPredicate) unaryPredicate.getCommittedState();
        ORM_UnaryPredicate unaryPredicateInAnotherClient = anotherClientModel.getElements(ORM_UnaryPredicate.class).findFirst().get();

        assertEquals(1, mainModel.getElements(DiagramElement.class).count());
        assertEquals(1, clientModel.getElements(DiagramElement.class).count());
        assertEquals(1, anotherClientModel.getElements(DiagramElement.class).count());

        int a = 0;
    }
}
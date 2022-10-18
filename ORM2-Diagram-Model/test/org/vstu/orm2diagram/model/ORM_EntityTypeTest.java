package org.vstu.orm2diagram.model;

import org.junit.jupiter.api.Test;
import org.vstu.nodelinkdiagram.ClientDiagramModel;
import org.vstu.nodelinkdiagram.MainDiagramModel;
import org.vstu.nodelinkdiagram.statuses.ValidateStatus;
import org.vstu.orm2diagram.model.finalized_entities.Test_DiagramClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Litovkin Dmitry
 */
class ORM_EntityTypeTest {

    // В модели два Entity Type с разными именами
    @Test
    void TwoEntityTypesWithDifferentName() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_EntityType male_EntityType = clientModel.createNode(ORM_EntityType.class);
        male_EntityType.setName("Male");

        ORM_EntityType female_EntityType = clientModel.createNode(ORM_EntityType.class);
        female_EntityType.setName("Female");

        ValidateStatus validateStatus = clientModel.validate();

        assertEquals(ValidateStatus.Acceptable, validateStatus);
        assertEquals(ValidateStatus.Acceptable, male_EntityType.getValidateStatus());
        assertEquals(ValidateStatus.Acceptable, female_EntityType.getValidateStatus());
    }

    // В модели два Entity Type с одинаковыми именами
    @Test
    void TwoEntityTypesWithSameName() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_EntityType male_EntityType = clientModel.createNode(ORM_EntityType.class);
        male_EntityType.setName("Male");

        ORM_EntityType female_EntityType = clientModel.createNode(ORM_EntityType.class);
        female_EntityType.setName("Male");

        ValidateStatus validateStatus = clientModel.validate();

        assertEquals(ValidateStatus.Invalid, validateStatus);
        assertEquals(ValidateStatus.Invalid, male_EntityType.getValidateStatus());
        assertEquals(ValidateStatus.Invalid, female_EntityType.getValidateStatus());
    }

    // В модели Entity Type с пустым именем
    @Test
    void EntityTypeWithEmptyName() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();
        ORM_EntityType entityType = clientModel.createNode(ORM_EntityType.class);
        entityType.setName("");

        ValidateStatus validateStatus = clientModel.validate();

        assertEquals(ValidateStatus.Invalid, validateStatus);
        assertEquals(ValidateStatus.Invalid, entityType.getValidateStatus());
    }
}
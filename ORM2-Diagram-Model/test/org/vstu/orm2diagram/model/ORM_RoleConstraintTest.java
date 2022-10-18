package org.vstu.orm2diagram.model;

import org.junit.jupiter.api.Test;
import org.vstu.nodelinkdiagram.ClientDiagramModel;
import org.vstu.nodelinkdiagram.DiagramElement;
import org.vstu.nodelinkdiagram.MainDiagramModel;
import org.vstu.orm2diagram.model.finalized_entities.Test_DiagramClient;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Litovkin Dmitry
 */
public class ORM_RoleConstraintTest {


    // см. стр. 91
    @Test
    void xxxxxxxxxx() {
        MainDiagramModel mainModel = new MainDiagramModel(new ORM_DiagramFactory());
        ClientDiagramModel clientModel = mainModel.registerClient(new Test_DiagramClient());

        clientModel.beginUpdate();

        // ------------------------------- Object type -----------------------------

        ORM_EntityType et_Politician = clientModel.createNode(ORM_EntityType.class);
        et_Politician.setName("Politician");

        ORM_EntityType et_Country = clientModel.createNode(ORM_EntityType.class);
        et_Country.setName("Country");

        ORM_ValueType vt_CountryName = clientModel.createNode(ORM_ValueType.class);
        vt_CountryName.setName("CountryName");

        // ------------------------------- Binary predicate -----------------------------

        ORM_BinaryPredicate bp_IsPresidentOf = clientModel.createNode(ORM_BinaryPredicate.class);
        bp_IsPresidentOf.getItem(0).setName("is president of");
        bp_IsPresidentOf.getItem(1).setName("");

        ORM_RoleAssociation ra_PoliticianIsPresidentOf = clientModel.connectBy(et_Politician, bp_IsPresidentOf.getItem(0), ORM_RoleAssociation.class);
        ORM_RoleAssociation ra_revPoliticianIsPresidentOf = clientModel.connectBy(et_Country, bp_IsPresidentOf.getItem(1), ORM_RoleAssociation.class);


        ORM_BinaryPredicate bp_IsVicePresidentOf = clientModel.createNode(ORM_BinaryPredicate.class);
        bp_IsVicePresidentOf.getItem(0).setName("is vice-president of");
        bp_IsVicePresidentOf.getItem(1).setName("");

        ORM_RoleAssociation ra_PoliticianIsVicePresidentOf = clientModel.connectBy(et_Politician, bp_IsVicePresidentOf.getItem(0), ORM_RoleAssociation.class);
        ORM_RoleAssociation ra_revPoliticianIsVicePresidentOf = clientModel.connectBy(et_Country, bp_IsVicePresidentOf.getItem(1), ORM_RoleAssociation.class);


        ORM_BinaryPredicate bp_Has = clientModel.createNode(ORM_BinaryPredicate.class);
        bp_Has.getItem(0).setName("has");
        bp_Has.getItem(1).setName("is of");

        ORM_RoleAssociation ra_CountryHas = clientModel.connectBy(et_Country, bp_Has.getItem(0), ORM_RoleAssociation.class);
        ORM_RoleAssociation ra_revCountryHas = clientModel.connectBy(et_Country, bp_Has.getItem(1), ORM_RoleAssociation.class);


        // -------------------------- Mandatory constraint -------------------------------------

        bp_Has.getItem(0).setMandatory(true);


        // ----------------------------- Unique constraint -------------------------------------

        bp_IsPresidentOf.getItem(0).setUnique(true);
        bp_IsPresidentOf.getItem(1).setUnique(true);

        bp_IsVicePresidentOf.getItem(0).setUnique(true);
        bp_IsVicePresidentOf.getItem(1).setUnique(true);

        bp_Has.getItem(0).setUnique(true);
        bp_Has.getItem(1).setUnique(true);


        // ----------------------------- Role constraint -------------------------------------

        ORM_SequenceFromOneRole sfor_IsPresidentOf = clientModel.createNode(ORM_SequenceFromOneRole.class);
        sfor_IsPresidentOf.addItem(bp_IsPresidentOf.getItem(0));

        ORM_SequenceFromOneRole sfor_revIsPresidentOf = clientModel.createNode(ORM_SequenceFromOneRole.class);
        sfor_revIsPresidentOf.addItem(bp_IsPresidentOf.getItem(1));

        ORM_SequenceFromOneRole sfor_IsVicePresidentOf = clientModel.createNode(ORM_SequenceFromOneRole.class);
        sfor_IsVicePresidentOf.addItem(bp_IsVicePresidentOf.getItem(0));

        ORM_SequenceFromOneRole sfor_revIsVicePresidentOf = clientModel.createNode(ORM_SequenceFromOneRole.class);
        sfor_revIsVicePresidentOf.addItem(bp_IsVicePresidentOf.getItem(1));

        ORM_XorRoleConstraint rc_Xor = clientModel.createNode(ORM_XorRoleConstraint.class);
        ORM_UndirectedRoleConstraintAssociation rca_Xor_IsPresidentOf = clientModel.connectBy(rc_Xor, sfor_IsPresidentOf, ORM_UndirectedRoleConstraintAssociation.class);
        ORM_UndirectedRoleConstraintAssociation rca_Xor_IsVicePresidentOf = clientModel.connectBy(rc_Xor, sfor_IsVicePresidentOf, ORM_UndirectedRoleConstraintAssociation.class);

        ORM_SubsetRoleConstraint rc_Subset = clientModel.createNode(ORM_SubsetRoleConstraint.class);
        ORM_DirectedRoleConstraintAssociation rca_Subset_revIsPresidentOf = clientModel.connectBy(rc_Subset, sfor_revIsPresidentOf, ORM_DirectedRoleConstraintAssociation.class);
        ORM_UndirectedRoleConstraintAssociation rca_Subset_revIsVicePresidentOf = clientModel.connectBy(rc_Subset, sfor_revIsVicePresidentOf, ORM_UndirectedRoleConstraintAssociation.class);

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        ORM_SequenceFromTwoRoles rs_IsPresidentOf = clientModel.createNode(ORM_SequenceFromTwoRoles.class);
        rs_IsPresidentOf.addItem(bp_IsPresidentOf.getItem(0));
        rs_IsPresidentOf.addItem(bp_IsPresidentOf.getItem(1));

        ORM_SequenceFromTwoRoles rs_IsVicePresidentOf = clientModel.createNode(ORM_SequenceFromTwoRoles.class);
        rs_IsVicePresidentOf.addItem(bp_IsVicePresidentOf.getItem(0));
        rs_IsVicePresidentOf.addItem(bp_IsVicePresidentOf.getItem(1));

        ORM_ExclusionRoleConstraint rc_Exclusion = clientModel.createNode(ORM_ExclusionRoleConstraint.class);
        ORM_UndirectedRoleConstraintAssociation rca_Exclusion_IsPresidentOf = clientModel.connectBy(rc_Xor, rs_IsPresidentOf, ORM_UndirectedRoleConstraintAssociation.class);
        ORM_UndirectedRoleConstraintAssociation rca_Exclusion_IsVicePresidentOf = clientModel.connectBy(rc_Xor, rs_IsVicePresidentOf, ORM_UndirectedRoleConstraintAssociation.class);

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        List<DiagramElement> elements = clientModel.getElements(DiagramElement.class).collect(Collectors.toList());

        long a = 0;
    }
}

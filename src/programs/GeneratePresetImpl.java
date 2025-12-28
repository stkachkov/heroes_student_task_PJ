package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        unitList.sort((u1, u2) -> {
            double ratio1_u1 = (double) u1.getBaseAttack() / u1.getCost();
            double ratio1_u2 = (double) u2.getBaseAttack() / u2.getCost();
            if (ratio1_u1 != ratio1_u2) {
                return Double.compare(ratio1_u2, ratio1_u1);
            }
            double ratio2_u1 = (double) u1.getHealth() / u1.getCost();
            double ratio2_u2 = (double) u2.getHealth() / u2.getCost();
            return Double.compare(ratio2_u2, ratio2_u1);
        });

        List<Unit> armyUnits = new ArrayList<>();
        Map<String, Integer> unitCounts = new HashMap<>();
        int currentPoints = 0;

        int placementX = 0;
        int placementY = 0;
        final int MAX_Y = 20;
        final int MAX_X = 2;

        for (Unit unitType : unitList) {
            String unitTypeName = unitType.getUnitType();
            int unitCost = unitType.getCost();

            while (true) {
                int currentCount = unitCounts.getOrDefault(unitTypeName, 0);

                if (currentPoints + unitCost > maxPoints || currentCount >= 11 || placementX > MAX_X) {
                    break;
                }

                String uniqueName = unitType.getName() + " " + (currentCount + 1);
                Unit newUnit = new Unit(
                        uniqueName,
                        unitType.getUnitType(),
                        unitType.getHealth(),
                        unitType.getBaseAttack(),
                        unitType.getCost(),
                        unitType.getAttackType(),
                        unitType.getAttackBonuses(),
                        unitType.getDefenceBonuses(),
                        placementX,
                        placementY
                );
                
                armyUnits.add(newUnit);
                currentPoints += unitCost;
                unitCounts.put(unitTypeName, currentCount + 1);

                placementY++;
                if (placementY > MAX_Y) {
                    placementY = 0;
                    placementX++;
                }
            }
        }
        
        return new Army(armyUnits);
    }
}
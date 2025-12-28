package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.*;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();
        
        for (List<Unit> column : unitsByRow) {
            Set<Integer> occupiedYCoordsInColumn = new HashSet<>();
            for (Unit unit : column) {
                if(unit.isAlive()) {
                    occupiedYCoordsInColumn.add(unit.getyCoordinate());
                }
            }

            for (Unit unit : column) {
                if (!unit.isAlive()) {
                    continue;
                }

                if (isLeftArmyTarget) { 
                    
                    if (!occupiedYCoordsInColumn.contains(unit.getyCoordinate() + 1)) {
                        suitableUnits.add(unit);
                    }
                } else { 
                    
                    if (!occupiedYCoordsInColumn.contains(unit.getyCoordinate() - 1)) {
                        suitableUnits.add(unit);
                    }
                }
            }
        }
        return suitableUnits;
    }
}
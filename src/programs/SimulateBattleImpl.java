package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        if (this.printBattleLog == null) {
            this.printBattleLog = (attackingUnit, target) -> {};
        }

        while (hasLivingUnits(playerArmy) && hasLivingUnits(computerArmy)) {
            List<Unit> alivePlayerUnits = playerArmy.getUnits().stream().filter(Unit::isAlive).toList();
            List<Unit> aliveComputerUnits = computerArmy.getUnits().stream().filter(Unit::isAlive).toList();

            if (alivePlayerUnits.isEmpty() || aliveComputerUnits.isEmpty()) {
                break;
            }

            List<Unit> allUnits = new ArrayList<>();
            allUnits.addAll(alivePlayerUnits);
            allUnits.addAll(aliveComputerUnits);

            allUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            List<Unit> killedThisRound = new ArrayList<>();
            for (Unit attacker : allUnits) {
                
                if (!attacker.isAlive() || killedThisRound.contains(attacker)) {
                    continue;
                }

                Unit target = attacker.getProgram().attack();
                this.printBattleLog.printBattleLog(attacker, target);
                
                if (target != null && target.getHealth() <= 0) {
                    if (target.isAlive()) { 
                        target.setAlive(false);
                        killedThisRound.add(target);
                    }
                }

                if (!hasLivingUnits(playerArmy) || !hasLivingUnits(computerArmy)) {
                    break;
                }
            }
        }
    }

    private boolean hasLivingUnits(Army army) {
        if (army == null || army.getUnits() == null) {
            return false;
        }
        return army.getUnits().stream().anyMatch(Unit::isAlive);
    }
}
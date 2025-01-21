package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }
    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        if (playerArmy == null || computerArmy == null || playerArmy.getUnits().isEmpty() || computerArmy.getUnits().isEmpty()) {
            return;
        }

        while(true){
            // Вывести всех живых юнитов
            List<Unit> playerUnits = playerArmy.getUnits().stream().filter(Unit::isAlive).toList();
            List<Unit> computerUnits = computerArmy.getUnits().stream().filter(Unit::isAlive).toList();

            // Проверка на то, есть ли живые юниты в армиях
            if (playerUnits.isEmpty() || computerUnits.isEmpty()) {
                break;
            }

            // Sort units by attack in decending order
            playerUnits.sort(Comparator.comparingInt(unit -> -unit.getBaseAttack()));
            computerUnits.sort(Comparator.comparingInt(unit -> -unit.getBaseAttack()));


            for(Unit unit: playerUnits){
                if(!unit.isAlive()){
                    continue;
                }

                Unit target = unit.getProgram().attack();
                if(target != null){
                    printBattleLog.printBattleLog(unit, target);

                    if(!target.isAlive()){
                        computerArmy.getUnits().remove(target);
                    }
                }
            }

            for(Unit unit: computerUnits){
                if(!unit.isAlive()){
                    continue;
                }

                Unit target = unit.getProgram().attack();
                if(target != null){
                    printBattleLog.printBattleLog(unit, target);

                    if(!target.isAlive()){
                        playerArmy.getUnits().remove(target);
                    }
                }
            }

            // Проверка на то, остались ли живые юниты
            if (playerArmy.getUnits().stream().noneMatch(Unit::isAlive) ||
                    computerArmy.getUnits().stream().noneMatch(Unit::isAlive)) {
                break;
            }

        }
    }
}









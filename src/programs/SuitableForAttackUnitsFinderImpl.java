package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        if(unitsByRow == null || unitsByRow.isEmpty())
            return new ArrayList<>();

        List<Unit> suitableUnits = new ArrayList<>();
        if(isLeftArmyTarget){ // Армия компьютера атакует армию игрока
            for(List<Unit> row: unitsByRow){
                if(row.isEmpty())
                    continue;
                suitableUnits.add(row.get(0));
            }
        } else { // Армия игрока атакует армию компьютера
            for(List<Unit> row: unitsByRow){
                if(row.isEmpty())
                    continue;
                suitableUnits.add(row.get(row.size() - 1));
            }
        }

        return suitableUnits;
    }
}



package com.battle.heroes.army.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        // Проверка входных данных
        if (unitList == null || unitList.isEmpty() || maxPoints <= 0) {
            return new Army(new ArrayList<>()); // Возвращаем пустую армию
        }

        List<Unit> armyUnits = new ArrayList<>();
        int currentPoints = 0;
        int maxUnitsPerType = 11;

        // Вспомогательный класс для оценки эффективности юнитов
        class UnitValue {
            Unit unit;
            float totalValue;

            UnitValue(Unit unit, float totalValue) {
                this.unit = unit;
                this.totalValue = totalValue;
            }
        }

        // Создаем список юнитов с рассчитанной эффективностью
        List<UnitValue> unitValues = new ArrayList<>();
        for (Unit unit : unitList) {
            float attackValue = (float) unit.getBaseAttack() / unit.getCost();
            float healthValue = (float) unit.getHealth() / unit.getCost();
            float totalValue = (attackValue + healthValue) / 2;
            unitValues.add(new UnitValue(unit, totalValue));
        }

        // Сортируем юнитов по убыванию эффективности
        unitValues.sort((uv1, uv2) -> Float.compare(uv2.totalValue, uv1.totalValue));

        // Инициализируем координаты
        int xCoordinate = 0;
        int yCoordinate = 0;

        // Формируем армию
        while (currentPoints < maxPoints) {
            boolean unitAdded = false;

            for (UnitValue uv : unitValues) {
                Unit unit = uv.unit;

                // Считаем количество юнитов одного типа в текущей армии
                long unitCount = armyUnits.stream()
                        .filter(u -> u.getUnitType().equals(unit.getUnitType()))
                        .count();

                // Проверяем ограничения: максимум 11 юнитов одного типа и доступные очки
                if (unitCount < maxUnitsPerType && currentPoints + unit.getCost() <= maxPoints) {
                    // Создаем карту атакующих бонусов
                    Map<String, Double> attackBonuses = new HashMap<>();
                    attackBonuses.put("attack", (double) unit.getBaseAttack());

                    // Создаем карту защитных бонусов
                    Map<String, Double> defenceBonuses = new HashMap<>();
                    defenceBonuses.put("defense", 10.0); // Примерное значение

                    // Создаем юнита и добавляем в армию
                    armyUnits.add(new Unit(
                            unit.getName(),
                            unit.getUnitType(),
                            unit.getHealth(),
                            unit.getBaseAttack(),
                            unit.getCost(),
                            unit.getAttackType(),
                            attackBonuses,
                            defenceBonuses,
                            xCoordinate,
                            yCoordinate
                    ));

                    currentPoints += unit.getCost();
                    unitAdded = true;

                    // Обновляем координаты
                    xCoordinate += 1;
                    if (xCoordinate >= 10) {
                        xCoordinate = 0;
                        yCoordinate += 1;
                    }

                    break;
                }
            }

            // Если больше невозможно добавить юнитов, выходим из цикла
            if (!unitAdded) {
                break;
            }
        }

        // Возвращаем сформированную армию
        return new Army(armyUnits);
    }
}

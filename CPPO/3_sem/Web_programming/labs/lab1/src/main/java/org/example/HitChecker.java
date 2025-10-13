package org.example;

import java.math.BigDecimal;

public class HitChecker implements PointValidator {
  @Override
  public void validate(BigDecimal x, BigDecimal y, BigDecimal r) {
    // Пустая реализация, так как валидация выполняется в Validator
    throw new UnsupportedOperationException("Проверка не поддерживается HitChecker");
  }

  @Override
  public boolean isHit(BigDecimal x, BigDecimal y, BigDecimal r) {
    return (checkTriangle1(x, y, r) || checkCircle(x, y, r) || checkRectangle4(x, y, r));
  }

  // Треугольник в 1 квадранте с вершинами: (0, R/2), (R/2, R), (0, R)
  private boolean checkTriangle1(BigDecimal x, BigDecimal y, BigDecimal r) {
    if (x.compareTo(BigDecimal.ZERO) < 0 || y.compareTo(BigDecimal.ZERO) < 0) {
      return false;
    }

    BigDecimal halfR = r.divide(new BigDecimal("2"));

    // Проверяем, что точка внутри треугольника
    // Условия: x >= 0, y >= R/2, y <= R, y <= R + x (линия от (0, R) до (R/2, R))
    // и y >= R/2 + x (линия от (0, R/2) до (R/2, R))

    // Точка должна быть выше линии y = R/2 + x (от (0, R/2) до (R/2, R))
    BigDecimal lineY = halfR.add(x);

    return (x.compareTo(halfR) <= 0 &&
        y.compareTo(halfR) >= 0 &&
        y.compareTo(r) <= 0 &&
        y.compareTo(lineY) >= 0);
  }

  // Четверть круга в 3 квадранте: x <= 0, y <= 0, радиус R/2
  private boolean checkCircle(BigDecimal x, BigDecimal y, BigDecimal r) {
    if (x.compareTo(BigDecimal.ZERO) > 0 || y.compareTo(BigDecimal.ZERO) > 0) {
      return false;
    }

    BigDecimal halfR = r.divide(new BigDecimal("2"));
    BigDecimal radiusSquared = halfR.multiply(halfR);
    BigDecimal distanceSquared = x.multiply(x).add(y.multiply(y));

    return distanceSquared.compareTo(radiusSquared) <= 0;
  }

  // Прямоугольник в 4 квадранте: 0 <= x <= R/2, -R <= y <= -R/2
  private boolean checkRectangle4(BigDecimal x, BigDecimal y, BigDecimal r) {
    BigDecimal halfR = r.divide(new BigDecimal("2"));
    return (x.compareTo(BigDecimal.ZERO) >= 0 &&
        x.compareTo(halfR) <= 0 &&
        y.compareTo(r.negate()) >= 0 &&
        y.compareTo(halfR.negate()) <= 0);
  }
}
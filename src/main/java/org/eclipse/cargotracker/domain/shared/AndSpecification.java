package org.eclipse.cargotracker.domain.shared;

/**
 * AND specification, used to create a new specification that is the AND of two other
 * specifications.
 */
public class AndSpecification<T> extends AbstractSpecification<T> {

  private final Specification<T> spec1;
  private final Specification<T> spec2;

  /**
   * Create a new AND specification based on two other spec.
   *
   * @param spec1 Specification one.
   * @param spec2 Specification two.
   */
  public AndSpecification(Specification<T> spec1, Specification<T> spec2) {
    this.spec1 = spec1;
    this.spec2 = spec2;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSatisfiedBy(T t) {
    return spec1.isSatisfiedBy(t) && spec2.isSatisfiedBy(t);
  }
}

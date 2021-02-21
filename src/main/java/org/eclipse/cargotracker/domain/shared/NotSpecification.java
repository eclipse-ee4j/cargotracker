package org.eclipse.cargotracker.domain.shared;

/** NOT decorator, used to create a new specifcation that is the inverse (NOT) of the given spec. */
public class NotSpecification<T> extends AbstractSpecification<T> {

  private final Specification<T> spec1;

  /**
   * Create a new NOT specification based on another spec.
   *
   * @param spec1 Specification instance to not.
   */
  public NotSpecification(Specification<T> spec1) {
    this.spec1 = spec1;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSatisfiedBy(T t) {
    return !spec1.isSatisfiedBy(t);
  }
}

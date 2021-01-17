package org.eclipse.cargotracker.domain.shared;

/**
 * Abstract base implementation of composite {@link Specification} with default implementations for
 * {@code and}, {@code or} and {@code not}.
 */
public abstract class AbstractSpecification<T> implements Specification<T> {

  /** {@inheritDoc} */
  @Override
  public abstract boolean isSatisfiedBy(T t);

  /** {@inheritDoc} */
  @Override
  public Specification<T> and(Specification<T> specification) {
    return new AndSpecification<>(this, specification);
  }

  /** {@inheritDoc} */
  @Override
  public Specification<T> or(Specification<T> specification) {
    return new OrSpecification<>(this, specification);
  }

  /** {@inheritDoc} */
  @Override
  public Specification<T> not(Specification<T> specification) {
    return new NotSpecification<>(specification);
  }
}

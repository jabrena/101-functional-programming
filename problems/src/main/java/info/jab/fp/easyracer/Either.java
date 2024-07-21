package info.jab.fp.easyracer;

import jakarta.annotation.Nonnull;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A generic sealed interface representing a value of one of two possible types (a disjoint union).
 * Instances of Either are either an instance of Left or Right.
 *
 * Inspired by the implemenation from Either (Scala) and Either (ArrowKt)
 *
 * @param <L> the type of the Left value
 * @param <R> the type of the Right value
 *
 * @author Juan Antonio Breña Moral
 * @author ChatGPT-40
 */
public sealed interface Either<L, R> permits Either.Left, Either.Right {
    /**
     * Applies either the leftMapper function to the value if this is a Left, or the rightMapper function if this is a Right.
     *
     * @param leftMapper the function to apply if this is a Left
     * @param rightMapper the function to apply if this is a Right
     * @param <T> the type of the result
     * @return the result of applying the appropriate function
     */
    <T> T fold(Function<? super L, ? extends T> leftMapper, Function<? super R, ? extends T> rightMapper);

    /**
     * Checks if this is an instance of Left.
     *
     * @return true if this is a Left, false otherwise
     */
    boolean isLeft();

    /**
     * Checks if this is an instance of Right.
     *
     * @return true if this is a Right, false otherwise
     */
    boolean isRight();

    /**
     * Creates an instance of Left.
     *
     * @param value the value to be wrapped in Left
     * @param <L> the type of the Left value
     * @param <R> the type of the Right value
     * @return a Left containing the given value
     */
    static <L, R> Either<L, R> left(@Nonnull L value) {
        Objects.requireNonNull(value, "Left value cannot be null");
        return new Left<>(value);
    }

    /**
     * Creates an instance of Right.
     *
     * @param value the value to be wrapped in Right
     * @param <L> the type of the Left value
     * @param <R> the type of the Right value
     * @return a Right containing the given value
     */
    static <L, R> Either<L, R> right(@Nonnull R value) {
        Objects.requireNonNull(value, "Right value cannot be null");
        return new Right<>(value);
    }

    /**
     * Transforms the value in Right using the given function if this is a Right, otherwise returns the current Left.
     *
     * @param mapper the function to apply to the Right value
     * @param <U> the type of the new Right value
     * @return a new Either instance
     */
    default <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
        if (isRight()) {
            return Either.right(mapper.apply(((Right<L, R>) this).value()));
        } else {
            return Either.left(((Left<L, R>) this).value());
        }
    }

    /**
     * Transforms the value in Right using the given function that returns another Either, if this is a Right.
     * Otherwise, returns the current Left.
     *
     * @param mapper the function to apply to the Right value
     * @param <U> the type of the new Right value
     * @return a new Either instance
     */
    default <U> Either<L, U> flatMap(Function<? super R, ? extends Either<L, U>> mapper) {
        if (isRight()) {
            return mapper.apply(((Right<L, R>) this).value());
        } else {
            return Either.left(((Left<L, R>) this).value());
        }
    }

    /**
     * Swaps the Left and Right types. Converts a Left to a Right and vice versa.
     *
     * @return a new Either instance with the Left and Right types swapped
     */
    default Either<R, L> swap() {
        if (isRight()) {
            return Either.left(((Right<L, R>) this).value());
        } else {
            return Either.right(((Left<L, R>) this).value());
        }
    }

    /**
     * Returns the Right value if this is a Right, otherwise returns the result of the given Supplier.
     *
     * @param other a Supplier whose result is returned if this is a Left
     * @return the Right value or the result of the Supplier
     */
    default R getOrElse(Supplier<? extends R> other) {
        if (isRight()) {
            return ((Right<L, R>) this).value();
        } else {
            return other.get();
        }
    }

    /**
     * Returns this Either if it is a Right, otherwise returns the result of the given Supplier.
     *
     * @param other a Supplier whose result is returned if this is a Left
     * @return this Either or the result of the Supplier
     */
    default Either<L, R> orElse(Supplier<? extends Either<L, R>> other) {
        if (isRight()) {
            return this;
        } else {
            return other.get();
        }
    }

    /**
     * Combines two Either instances using the given combiner function if both are Right.
     * Otherwise, returns the first Right instance encountered, or the last Left if both are Left.
     *
     * @param other another Either instance to combine with
     * @param combiner a BiFunction to combine the Right values
     * @return a new Either instance resulting from the combination
     */
    default Either<L, R> combine(Either<L, R> other, BiFunction<? super R, ? super R, ? extends R> combiner) {
        if (this.isRight() && other.isRight()) {
            return Either.right(combiner.apply(((Right<L, R>) this).value(), ((Right<L, R>) other).value()));
        } else if (this.isRight()) {
            return this;
        } else {
            return other;
        }
    }

    /**
     * Converts this Either to an Optional.
     * If this is a Right, the Optional will contain the value.
     * If this is a Left, the Optional will be empty.
     *
     * @return an Optional containing the Right value if present, otherwise an empty Optional
     */
    default Optional<R> toOptional() {
        if (isRight()) {
            return Optional.of(((Right<L, R>) this).value());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns the value if this is a Right, otherwise throws an exception.
     *
     * @return the Right value
     * @throws NoSuchElementException if this is a Left
     */
    R get();

    /**
     * A record representing the Left variant of an Either.
     *
     * @param <L> the type of the Left value
     * @param <R> the type of the Right value
     * @param value the value
     */
    record Left<L, R>(@Nonnull L value) implements Either<L, R> {
        /**
         * Constructs a new {@code Left} with the given value.
         *
         * @param value the value to be contained in this {@code Left}
         * @throws NullPointerException if {@code value} is {@code null}
         */
        public Left {
            Objects.requireNonNull(value, "Left value cannot be null");
        }

        @Override
        public <T> T fold(Function<? super L, ? extends T> leftMapper, Function<? super R, ? extends T> rightMapper) {
            return leftMapper.apply(value);
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public R get() {
            throw new NoSuchElementException("No value present in Left");
        }
    }

    /**
     * A record representing the Right variant of an Either.
     *
     * @param <L> the type of the Left value
     * @param <R> the type of the Right value
     * @param value the value
     */
    record Right<L, R>(@Nonnull R value) implements Either<L, R> {
        /**
         * Constructs a new {@code Right} with the given value.
         *
         * @param value the value to be contained in this {@code Right}
         * @throws NullPointerException if {@code value} is {@code null}
         */
        public Right {
            Objects.requireNonNull(value, "Right value cannot be null");
        }

        @Override
        public <T> T fold(Function<? super L, ? extends T> leftMapper, Function<? super R, ? extends T> rightMapper) {
            return rightMapper.apply(value);
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public R get() {
            return value;
        }
    }
}

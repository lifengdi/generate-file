package com.lifengdi.commons.utils;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author 李锋镝
 * @date Create at 14:48 2019/9/5
 */
public abstract class Either<L, R> implements Serializable {

    public static <L, R> Either<L, R> either(Supplier<L> leftSupplier, Supplier<R> rightSupplier) {
        R rightValue = rightSupplier.get();
        if (rightValue != null) {
            return Either.right(rightValue);
        } else {
            return Either.left(leftSupplier.get());
        }
    }

    public static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    public static <L, R> Either<L, R> right(R right) {
        Objects.requireNonNull(right);
        return new Right<>(right);
    }

    public abstract L getLeft();

    public abstract R getRight();

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract <T> T fold(Function<L, T> transformLeft, Function<R, T> transformRight);

    public Either<R, L> swap() {
        return either(this::getRight, this::getLeft);
    }

    public abstract <T, U> Either<T, U> map(Function<L, T> transformLeft, Function<R, U> transformRight);

    public abstract <T> Either<L, T> mapRight(Function<? super R, ? extends T> rFunc);

    public abstract Either<L, R> peekRight(Consumer<? super R> action);


    public abstract void run(Consumer<L> runLeft, Consumer<R> runRight);

    public abstract Optional<R> toOptional();

    public static class Left<L, R> extends Either<L, R> {

        L leftValue;

        private Left(L left) {
            this.leftValue = left;
        }

        @Override
        public L getLeft() {
            return this.leftValue;
        }

        @Override
        public R getRight() {
            throw new NoSuchElementException("Tried to getRight from a Left");
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
        public <T> T fold(Function<L, T> transformLeft, Function<R, T> transformRight) {
            return transformLeft.apply(this.leftValue);
        }

        @Override
        public <T, U> Either<T, U> map(Function<L, T> transformLeft, Function<R, U> transformRight) {
            return Either.left(transformLeft.apply(this.leftValue));
        }

        @Override
        public <T> Either<L, T> mapRight(Function<? super R, ? extends T> rFunc) {
            return Either.left(this.getLeft());
        }

        @Override
        public Either<L, R> peekRight(Consumer<? super R> action) {
            return Either.left(this.getLeft());
        }

        @Override
        public void run(Consumer<L> runLeft, Consumer<R> runRight) {
            runLeft.accept(this.leftValue);
        }

        @Override
        public Optional<R> toOptional() {
            return Optional.empty();
        }

        @Override
        public int hashCode() {
            return this.leftValue.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Left<?, ?>) {
                final Left<?, ?> otherAsLeft = (Left<?, ?>) other;
                return this.leftValue.equals(otherAsLeft.leftValue);
            } else {
                return false;
            }
        }

    }

    public static class Right<L, R> extends Either<L, R> {

        R rightValue;

        private Right(R right) {
            this.rightValue = right;
        }

        @Override
        public L getLeft() {
            throw new NoSuchElementException("Tried to getLeft from a Right");
        }

        @Override
        public R getRight() {
            return rightValue;
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
        public <T> T fold(Function<L, T> transformLeft, Function<R, T> transformRight) {
            Objects.requireNonNull(transformRight);
            return transformRight.apply(this.rightValue);
        }

        @Override
        public <T, U> Either<T, U> map(Function<L, T> transformLeft, Function<R, U> transformRight) {
            Objects.requireNonNull(transformRight);
            return Either.right(transformRight.apply(this.rightValue));
        }

        @Override
        public <T> Either<L, T> mapRight(Function<? super R, ? extends T> rFunc) {
            return Either.right(rFunc.apply(this.getRight()));
        }

        @Override
        public Either<L, R> peekRight(Consumer<? super R> action) {
            action.accept(getRight());
            return this;
        }

        @Override
        public void run(Consumer<L> runLeft, Consumer<R> runRight) {
            runRight.accept(this.rightValue);
        }

        @Override
        public Optional<R> toOptional() {
            return Optional.ofNullable(rightValue);
        }

        @Override
        public int hashCode() {
            return this.rightValue.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Right<?, ?>) {
                final Right<?, ?> otherAsRight = (Right<?, ?>) other;
                return this.rightValue.equals(otherAsRight.rightValue);
            } else {
                return false;
            }
        }

    }
}


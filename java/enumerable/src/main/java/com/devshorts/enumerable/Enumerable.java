package com.devshorts.enumerable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Enumerable<TSource, TResult> implements Iterator<TResult>, Iterable<TResult> {

    protected Iterator<TSource> source;
    protected Supplier<Iterator<TSource>> generator;

    public static <TSource> Enumerable<TSource, TSource> init(Iterable<TSource> source){
        return new Enumerable<>(source);
    }

    public Enumerable(Iterable<TSource> input) {
        generator = () -> input.iterator();

        reset();
    }

    protected void reset(){
        source = generator.get();
    }

    public <TResult2> MapEnumerable<TResult, TResult2> map(Function<TResult, TResult2> mapFunc){
        return new MapEnumerable<>(this, i -> mapFunc.apply(i));
    }

    public <TResult2> Enumerable<TResult, TResult2> flatMap(Function<TResult, List<TResult2>> mapFunc){
        return new FlatMapEnumerable<>(this, i -> mapFunc.apply(i));
    }

    public FilterEnumerable<TResult> filter(Predicate<TResult> filterFunc){
        return new FilterEnumerable<>(this, filterFunc);
    }

    public TakeEnumerable<TResult> take(int n){
        return new TakeEnumerable(this, n);
    }

    public TakeWhileEnumerable<TResult> takeWhile(Predicate<TResult> predicate){
        return new TakeWhileEnumerable(this, predicate);
    }

    public SkipEnumerable<TResult> skip(int n){
        return new SkipEnumerable(this, n);
    }

    public <TProjection> OrderByEnumerable<TResult> orderBy(Function<TSource, TProjection> projection){
        return new OrderByEnumerable(this, projection);
    }

    public List<TResult> toList(){
        List<TResult> r = new ArrayList<>();

        for(TResult item : this){
            r.add(item);
        }

        return r;
    }

    @Override
    public Iterator<TResult> iterator() {
        reset();

        return this;
    }

    @Override
    public boolean hasNext() {
        return source.hasNext();
    }

    @Override
    public TResult next() {
        return (TResult)source.next();
    }
}
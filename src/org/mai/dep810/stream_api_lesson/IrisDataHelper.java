package org.mai.dep810.stream_api_lesson;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class IrisDataHelper {
    private final List<Iris> _dataSet;

    public IrisDataHelper(List<Iris> dataSet) {
        this._dataSet = dataSet;
    }

    public Double getAverage(ToDoubleFunction<Iris> functor) {
        return _dataSet.stream()
                .mapToDouble(functor)
                .average()
                .orElseThrow(() -> new DataCorruptionException("Corrupted data"));
    }

    public List<Iris> filter(Predicate<Iris> predictor) {
        return _dataSet.stream()
                .filter(predictor)
                .collect(Collectors.toList());
    }

    public Double getAverageWithFilter(Predicate<Iris> predictor, ToDoubleFunction<Iris> mapFunction) {
        return _dataSet.stream()
                .filter(predictor)
                .mapToDouble(mapFunction)
                .average()
                .orElseThrow(() -> new DataCorruptionException("Corrupted data"));
    }

    public<T> Map<T, List<Iris>> groupBy(Function<Iris, T> groupFunction) {
        return _dataSet.stream()
                .collect(Collectors.groupingBy(groupFunction));
    }

    public<T> Map<T, Optional<Iris>> maxFromGroupedBy(Function<Iris, T> groupFunction, ToDoubleFunction<Iris> obtainMaxValueFunction) {
        return _dataSet.stream()
                .collect(Collectors.groupingBy(groupFunction,
                        Collectors.maxBy(Comparator.comparingDouble(obtainMaxValueFunction))));
    }
}

package org.mai.dep810.stream_api_lesson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class IrisDataHelperTest {

    IrisDataHelper _helper;
    List<Iris> _iris_list;

    @Before
    public void setUp() throws Exception {
        _iris_list = Files.lines(Paths.get("task_files", "iris.data"))
                .map(Iris::parse) //ref to method, (x) -> parse(x)
                .collect(Collectors.toList());
        _helper = new IrisDataHelper(_iris_list);
    }

    @After
    public void tearDown() {
        _iris_list = null;
        _helper = null;
    }

    @Test
    public void getAverage() throws Exception {
        assertEquals(BigDecimal.valueOf(5.84),
                BigDecimal.valueOf(_helper.getAverage(Iris::getSepalLength))
                        .setScale(2, BigDecimal.ROUND_HALF_UP)
        );
        assertEquals(BigDecimal.valueOf(3.76),
                BigDecimal.valueOf(_helper.getAverage(Iris::getPetalLength))
                        .setScale(2, BigDecimal.ROUND_HALF_UP)
        );
        assertEquals(BigDecimal.valueOf(3.05),
                BigDecimal.valueOf(_helper.getAverage(Iris::getSepalWidth))
                        .setScale(2, BigDecimal.ROUND_HALF_UP)
        );
        assertEquals(BigDecimal.valueOf(1.20).setScale(2, BigDecimal.ROUND_HALF_UP),
                BigDecimal.valueOf(_helper.getAverage(Iris::getPetalWidth))
                        .setScale(2, BigDecimal.ROUND_HALF_UP)
        );
    }

    @Test
    public void filter() {
        Iris fl_0 = _iris_list.get(0);
        Iris fl_142 = _iris_list.get(142);
        List<Iris> filtered = _helper.filter((flower) ->
                flower.getSepalWidth() < _helper.getAverage(Iris::getSepalWidth)
        );
        assertThat(filtered, not(hasItem(fl_0)));
        assertThat(filtered, hasItem(fl_142));

        filtered = _helper.filter((flower) -> flower.getSpecies().equals("Iris-setosa"));
        assertThat(filtered, hasItem(fl_0));
        assertThat(filtered, not(hasItem(fl_142)));
    }

    @Test
    public void getAverageWithFilter() throws Exception {
        assertEquals(BigDecimal.valueOf(4.26),
                BigDecimal.valueOf(_helper.getAverageWithFilter((flower) -> flower.getSpecies().equals("Iris-versicolor"),
                        Iris::getPetalLength))
                        .setScale(2, BigDecimal.ROUND_HALF_UP)
        );
    }

    @Test
    public void groupBy() {
        Iris fl_49 = _iris_list.get(49);
        Iris fl_145 = _iris_list.get(145);
        Map<Petal, List<Iris>> petal_classif_map = _helper.groupBy(Iris::classifyByPetal);
        assertTrue(petal_classif_map.containsKey(Petal.SMALL) &&
                petal_classif_map.containsKey(Petal.MEDIUM) && petal_classif_map.containsKey(Petal.LARGE));
        assertThat(petal_classif_map.get(Petal.LARGE), hasItem(fl_145));
        assertThat(petal_classif_map.get(Petal.LARGE), not(hasItem(fl_49)));
        assertThat(petal_classif_map.get(Petal.SMALL), hasItem(fl_49));
    }

    @Test
    public void maxFromGroupedBy() {
        Iris fl_14 = _iris_list.get(14);
        Map<String, Optional<Iris>> ans = _helper.maxFromGroupedBy(Iris::getSpecies, Iris::getSepalLength);
        assertEquals(3, ans.size());
        assertEquals(fl_14, _helper.maxFromGroupedBy(Iris::getSpecies, Iris::getSepalLength).get("Iris-setosa")
                .orElse(null)
        );

    }
}
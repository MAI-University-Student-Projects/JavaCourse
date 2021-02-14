package org.mai.dep810.threads_lesson;

import java.io.File;
import java.math.BigDecimal;

public interface FileCounter {

    /* Пройти по поддиректории и посчитать размеры каждого файла */
    void CountFileSystem(File f);

    /* Вывести суммарный размер поддиректории */
    BigDecimal getTotalSizeKb();

    /* Вывести файл с наибольшим размером из поддиректории */
    File getMaximumSizeFileKb();
}

package org.mai.dep810.threads_lesson;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

public class FileSimpleCounter implements FileCounter {

    Map<String, Long> _file_system_map;

    public FileSimpleCounter() {
        _file_system_map = new HashMap<>();
    }

    @Override
    public void CountFileSystem(File f) {
        try {
            for(File file_it : Objects.requireNonNull(f.listFiles())) {
                if(!file_it.isDirectory())
                    _file_system_map.put(file_it.getAbsolutePath(), file_it.length());
                else
                    CountFileSystem(file_it);
            }
        }
        catch (NullPointerException ignored) {
        }
    }

    @Override
    public BigDecimal getTotalSizeKb() {
        BigDecimal res = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        for(Long val : _file_system_map.values())
            res = res.add(BigDecimal.valueOf(val.doubleValue() / 1024));
        return res;
    }

    @Override
    public File getMaximumSizeFileKb() {
        return new File(Collections.max(_file_system_map.entrySet(), Comparator.comparingLong(Map.Entry::getValue)).getKey());
    }
}

//если файл то файллист null
//если пустая директория то filelist != null, но length = 0
//если директории нет - то файллист null
//если не пустая директория или если пустая директория то пробежит по всем но при пустой filelist != null, но length = 0

//    public static void main(String[] args) {
//        String path_name = "/";
//        File f_start = new File(path_name);
//        FileSimpleCounter smpl_cnt = new FileSimpleCounter();
//        System.out.println("Started...");
//        long timer_start = System.currentTimeMillis();
//        smpl_cnt.CountFileSystemMap(f_start);
//        System.out.println("Time in working (ms):" + (System.currentTimeMillis() - timer_start));
//        Map.Entry<String, Long> max_file = Collections.max(smpl_cnt._file_system_map.entrySet(), Comparator.comparingLong(Map.Entry::getValue));
//        System.out.println("Biggest accessible file in " + path_name + ": " + max_file.getKey() + "\nwith size:" + max_file.getValue().doubleValue() / 1024 + "kb");
//    }
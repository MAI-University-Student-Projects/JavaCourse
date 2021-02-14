package org.mai.dep810.threads_lesson;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String path_name = "/Users/andreytretyakov";

        FileCounter simple_count = new FileSimpleCounter();
        System.out.println("Started, counting dir " + path_name + " in main thread...");
        long timer_start = System.currentTimeMillis();
        simple_count.CountFileSystem(new File(path_name));
        System.out.println("Time in working (ms):" + (System.currentTimeMillis() - timer_start));
        File heaviest = simple_count.getMaximumSizeFileKb();
        System.out.println("Biggest accessible file in " + path_name + ": " + heaviest.getAbsolutePath() + "\nwith size:" + ((double) heaviest.length()) / 1024 + "kb");

        FileCounter thread_per_dir_count = new FileThreadByDirCounter();
        System.out.println("Started, counting dir " + path_name + " with thread per dir...");
        timer_start = System.currentTimeMillis();
        thread_per_dir_count.CountFileSystem(new File(path_name));
        System.out.println("Time in working (ms):" + (System.currentTimeMillis() - timer_start));
        heaviest = thread_per_dir_count.getMaximumSizeFileKb();
        System.out.println("Biggest accessible file in " + path_name + ": " + heaviest.getAbsolutePath() + "\nwith size:" + ((double) heaviest.length()) / 1024 + "kb");

        FileCounter pool_count = new FileThreadPoolCounter(20);
        System.out.println("Started, counting dir " + path_name + " with thread_pool...");
        timer_start = System.currentTimeMillis();
        pool_count.CountFileSystem(new File(path_name));
        System.out.println("Time in working (ms):" + (System.currentTimeMillis() - timer_start));
        heaviest = pool_count.getMaximumSizeFileKb();
        System.out.println("Biggest accessible file in " + path_name + ": " + heaviest.getAbsolutePath() + "\nwith size:" + ((double) heaviest.length()) / 1024 + "kb");
    }
}

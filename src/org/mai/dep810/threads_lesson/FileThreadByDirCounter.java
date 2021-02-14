package org.mai.dep810.threads_lesson;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FileThreadByDirCounter implements FileCounter {

    ConcurrentMap<String, Long> _file_system_concr_map;

    public FileThreadByDirCounter() {
        _file_system_concr_map = new ConcurrentHashMap<>();
    }

    @Override
    public void CountFileSystem(File f) {
        class DirThread extends Thread {
            final File _currnt_file;
            DirThread(File fl) {
                _currnt_file = fl;
            }

            @Override
            public void run() {
                File[] f_list = _currnt_file.listFiles();
                if(f_list != null) {
                    List<Thread> thrd_list = new ArrayList<>(f_list.length);
                    for(File file_it : f_list) {
                        if(!file_it.isDirectory())
                            _file_system_concr_map.put(file_it.getAbsolutePath(), file_it.length());
                        else {
                            Thread thrd = new DirThread(file_it);
                            thrd.start();
                            thrd_list.add(thrd);
                        }
                    }

                    for(Thread thr : thrd_list) {
                        try {
                            thr.join();
                        }

                        catch(InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }

        Thread dir_thrd = new DirThread(f);
        dir_thrd.start();
        try {
            dir_thrd.join();
        }

        catch(InterruptedException ex) {
            ex.printStackTrace();
        }
//        File[] f_list = f.listFiles();
//        if(f_list != null) {
//            List<Thread> thrd_list = new ArrayList<>(f_list.length);
//
//            for(File file_it : f_list) {
//                if(!file_it.isDirectory())
//                    _file_system_concr_map.put(file_it.getAbsolutePath(), file_it.length());
//                else {
//                    Thread thrd = new Thread(() -> CountFileSystem(file_it));
//                    thrd.start();
//                    thrd_list.add(thrd);
//                }
//            }
//
//            for(Thread thr : thrd_list) {
//                try {
//                    thr.join();
//                }
//
//                catch(InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
    }

    @Override
    public BigDecimal getTotalSizeKb() {
        BigDecimal res = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        for(Long val : _file_system_concr_map.values())
            res = res.add(BigDecimal.valueOf(val.doubleValue() / 1024));
        return res;
    }

    @Override
    public File getMaximumSizeFileKb() {
        return new File(Collections.max(_file_system_concr_map.entrySet(), Comparator.comparingLong(Map.Entry::getValue)).getKey());
    }
}

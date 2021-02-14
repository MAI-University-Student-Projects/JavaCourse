package org.mai.dep810.threads_lesson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class FileThreadPoolCounter extends FileThreadByDirCounter {
    int _pool_size;

    public FileThreadPoolCounter(int pool_prm) {
        _pool_size = Math.max(pool_prm, 16);
        _file_system_concr_map = new ConcurrentHashMap<>(_pool_size, 0.75f, _pool_size);
    }
    @Override
    public void CountFileSystem(File f) {

        class RecursiveFileAction extends RecursiveAction {

            final File _currnt_file;

            RecursiveFileAction(File fl) {
                _currnt_file = fl;
            }

            @Override
            protected void compute() {
                File[] f_list = _currnt_file.listFiles();
                if(f_list != null) {
                    List<RecursiveFileAction> action_list = new ArrayList<>(f_list.length);

                    for (File file_it : f_list) {
                        if (!file_it.isDirectory())
                            _file_system_concr_map.put(file_it.getAbsolutePath(), file_it.length());
                        else {
                            RecursiveFileAction concur_act = new RecursiveFileAction(file_it);
                            concur_act.fork();
                            action_list.add(concur_act);
                        }
                    }

                    for(RecursiveFileAction act : action_list)
                        act.join();
                }
            }
        }

        ForkJoinPool file_walking_pool = new ForkJoinPool(_pool_size);
        file_walking_pool.invoke(new RecursiveFileAction(f));
        //wait here
    }
}

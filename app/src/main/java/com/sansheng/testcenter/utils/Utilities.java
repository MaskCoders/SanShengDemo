package com.sansheng.testcenter.utils;

import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

/**
 * Created by sunshaogang on 12/16/15.
 */
public class Utilities {
    
    public class ExampleTask extends GroundyTask {
        @Override
        protected TaskResult doInBackground() {
            // you can send parameters to the task using a Bundle (optional)
            String exampleParam = getStringArg("arg_name");

            // lots of code

            // return a TaskResult depending on the success of your task
            // and optionally pass some results back
            return succeeded().add("the_result", "some result");
        }
    }
}

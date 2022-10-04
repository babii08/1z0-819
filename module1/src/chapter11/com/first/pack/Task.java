package chapter11.com.first.pack;

import chapter11.com.second.pack.SecondTask;

public class Task {

    public static String getTaskInfo() {
        System.out.println("In the task class");
        SecondTask secondTask = new SecondTask();
        var result = secondTask.generateString();
        System.out.println(result);
        return "In the Task class and also in SecondTask class";
    }

}

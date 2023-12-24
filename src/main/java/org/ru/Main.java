package org.ru;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar YourJarName.jar <folder_path>");
            System.exit(1);
        }

        String folderPath = args[0];
        FileDependencyResolver fileDependencyResolver = new FileDependencyResolver();
        fileDependencyResolver.startResolving(folderPath);
    }
}
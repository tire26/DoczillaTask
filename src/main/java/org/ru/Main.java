package org.ru;

public class Main {

    public static void main(String[] args) {
        FileDependencyResolver fileDependencyResolver = new FileDependencyResolver();
        fileDependencyResolver.startResolving("D:\\Programms\\DoczillaTask\\src\\main\\resources\\test-folder");
    }
}
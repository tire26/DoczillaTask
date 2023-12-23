package org.ru;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *  Класс по анализу файлов указанной папки
 */
public class FileDependencyResolver {

    private LinkedHashMap<String, List<String>> dependencies;
    private Set<String> visited;
    private List<String> result;
    private Logger logger;
    private String rootDirectoryPath;

    public FileDependencyResolver() {
        this.dependencies = new LinkedHashMap<>();
        this.visited = new HashSet<>();
        this.result = new ArrayList<>();
        this.logger = Logger.getLogger(FileDependencyResolver.class.getName());
    }

    /**
     * Метод сканирует указанную папку и строит список файлов по правилу
     * @param folderPath - путь к папке для сканирования
     */
    public void startResolving(String folderPath) {
        clearFields();

        if (folderPath.endsWith("\\")) {
            folderPath = folderPath.substring(0, folderPath.length() - 1);
        }
        rootDirectoryPath = folderPath;

        resolveDependencies(folderPath);

        dependencies = dependencies.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        createFilesList();
    }

    private void resolveDependencies(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    resolveDependencies(file.getAbsolutePath());
                } else if (file.isFile()) {
                    analyzeFile(file);
                }
            }
        }
    }

    private void clearFields() {
        this.dependencies.clear();
        this.visited.clear();
        this.result.clear();
    }

    private void analyzeFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            dependencies.put(file.getAbsolutePath(), new ArrayList<>());
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("require")) {
                    String dependency = extractPath(line);
                    dependencies.get(file.getAbsolutePath()).add(rootDirectoryPath + "\\" + dependency);
                }
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private void createFilesList() {
        List<String> cyclePath = new ArrayList<>();

        for (String file : dependencies.keySet()) {
            if (!visited.contains(file)) {
                if (dfs(file, cyclePath)) {
                    logger.log(Level.SEVERE, "Circular dependency detected in files: " +
                            cyclePath.stream().map(s -> s + " -> ").collect(Collectors.joining()) +
                            cyclePath.get(0));
                    return;
                }
            }
        }

        concatenateFiles(result, rootDirectoryPath + "\\result");
    }

    private boolean dfs(String file, List<String> cyclePath) {
        visited.add(file);
        List<String> dependents = dependencies.getOrDefault(file, Collections.emptyList());

        cyclePath.add(file);

        for (String dependent : dependents) {
            if (!visited.contains(dependent)) {
                if (dfs(dependent, cyclePath)) {
                    return true;
                }
            } else if (!result.contains(dependent)) {
                return true;
            }
        }

        cyclePath.remove(cyclePath.size() - 1); // Убираем текущий файл из пути при возврате из рекурсии
        result.add(file);
        return false;
    }

    private String extractPath(String line) {
        Pattern pattern = Pattern.compile("‘(.*?)’");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            return matcher.group(1).replace('/', '\\');
        } else {
            return null;
        }
    }

    private void concatenateFiles(List<String> files, String outputFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (String file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
